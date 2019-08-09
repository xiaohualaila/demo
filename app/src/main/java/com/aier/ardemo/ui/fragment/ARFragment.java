
package com.aier.ardemo.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aier.ardemo.adapter.ArListAdapter;
import com.aier.ardemo.network.schedulers.SchedulerProvider;
import com.aier.ardemo.ui.activity.ARActivity;
import com.aier.ardemo.ui.activity.OrderInfoActivity;
import com.aier.ardemo.ui.activity.ShoppingActivity;
import com.aier.ardemo.ui.contract.ArContract;
import com.aier.ardemo.ui.model.ArModel;
import com.aier.ardemo.ui.presenter.ArPresenter;
import com.aier.ardemo.utils.ToastyUtil;
import com.baidu.ar.ARController;
import com.baidu.ar.DuMixSource;
import com.baidu.ar.DuMixTarget;
import com.baidu.ar.bean.ARConfig;
import com.aier.ardemo.Config;
import com.aier.ardemo.R;
import com.aier.ardemo.callback.PromptCallback;
import com.aier.ardemo.camera.ARCameraManager;
import com.aier.ardemo.draw.ARRenderCallback;
import com.aier.ardemo.draw.ARRenderer;
import com.aier.ardemo.draw.GLConfigChooser;
import com.aier.ardemo.module.MsgType;
import com.aier.ardemo.ui.Prompt;
import com.aier.ardemo.arview.ARControllerManager;
import com.baidu.ar.util.SystemInfoUtil;
import com.baidu.ar.util.UiThreadUtil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ARFragment extends Fragment implements ArContract.View {

    private static final String TAG = "ARFragment";

    /**
     * Fragment 真正的容器
     */
    private FrameLayout mFragmentContainer;

    private View mRootView;
    private GLSurfaceView mArGLSurfaceView;
    private RecyclerView mRecyclerView;
    private TextView tv_order;
    private ImageView btn_show_recycview;
    /**
     * Prompt View 提示层View
     */
    private Prompt mPromptUi;

    /**
     * AR Renderer
     */
    private ARRenderer mARRenderer;

    /**
     * AR相机管理
     */
    private ARCameraManager mARCameraManager;

    /**
     * 需要手动申请的权限
     */
    private static final String[] ALL_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};

    /**
     * ar sdk 接口ARController
     */
    private ARController mARController;
    /**
     * AR输入参数类
     */
    private DuMixSource mDuMixSource;
    /**
     * 返回参数类
     */
    private DuMixTarget mDuMixTarget;

    // 接收 参数
    private String mArKey;
    private int mArTpye;
    private String mArFile;
    private ARActivity arActivity;
    private String current_produce = "";
    private ArPresenter presenter;
    ArListAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        arActivity = (ARActivity) context;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        // 进入时需要先配置环境
        super.onCreate(savedInstanceState);
        // 创建 Fragment root view
        mFragmentContainer = new FrameLayout(getActivity());
        // 初始化ARConfig
        Bundle bundle = getArguments();
        if (bundle != null) {
            mArKey = bundle.getString(Config.AR_KEY);
            mArTpye = bundle.getInt(Config.AR_TYPE);
            mArFile = bundle.getString(Config.AR_FILE);
            ARConfig.setARPath(mArFile);
            current_produce = mArKey;
        }

        mARCameraManager = new ARCameraManager();

        // 6.0以下版本直接同意使用权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            setupViews();
        } else {
            if (hasNecessaryPermission()) {
                setupViews();
            } else {
                requestPermissions(ALL_PERMISSIONS, 1123);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mFragmentContainer;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecycView();
        presenter = new ArPresenter(new ArModel(), this, SchedulerProvider.getInstance());
        presenter.getArListData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mARController != null) {
            mARController.resume();
            mARController.onAppear();
        }
        if (hasNecessaryPermission()) {
            // 打开相机
            startCamera();
        }
        if (mPromptUi != null) {
            mPromptUi.resume();
        }
    }

    private boolean hasNecessaryPermission() {
        List<String> permissionsList = new ArrayList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : ALL_PERMISSIONS) {
                if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(permission);
                }
            }
        }
        return permissionsList.size() == 0;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mARController != null) {
            mARController.pause();
        }
        if (mPromptUi != null) {
            mPromptUi.pause();
        }
        mARCameraManager.stopCamera(null, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPromptUi != null) {
            mPromptUi.release();
            mPromptUi = null;
        }
        if (mARRenderer != null) {
            mARRenderer.release();
            mARRenderer = null;
        }

        ARControllerManager.getInstance(getActivity()).release();

        if (mARController != null) {
            mARController.release();
            mARController = null;
        }
        presenter.despose();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1123) {
            setupViews();
        }
    }

    public boolean onFragmentBackPressed() {
        quit();
        return true;
    }

    /**
     * 退出逻辑
     * 1. 先关闭相机
     * 2. 通知外部回调
     * 3. 释放资源
     */
    private void quit() {
        if (mARCameraManager != null) {
            mARCameraManager.stopCamera(result -> {
                try {
                    UiThreadUtil.runOnUiThread(() -> {
                        if (result) {
                            if (getActivity() != null) {
                                getActivity().finish();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, false);
        }
    }

    private void setupViews() {

        mRootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_layout_arui, null);

        mArGLSurfaceView = mRootView.findViewById(R.id.bdar_view);
        mArGLSurfaceView.setEGLContextClientVersion(2);
        mARRenderer = new ARRenderer(isScreenOrientationLandscape(mRootView.getContext()));
        mARRenderer.setARFrameListener(
                surfaceTexture -> mArGLSurfaceView.requestRender()
        );
        mArGLSurfaceView.setEGLConfigChooser(new GLConfigChooser());
        mArGLSurfaceView.setRenderer(mARRenderer);
        mArGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        mPromptUi = mRootView.findViewById(R.id.bdar_prompt_view);
        mPromptUi.setPromptCallback(promptCallback);
        mFragmentContainer.addView(mRootView);

        mRecyclerView = mRootView.findViewById(R.id.rv);
        tv_order = mRootView.findViewById(R.id.tv_order);
        tv_order.setOnClickListener(v -> {
            ShoppingActivity.starShoppingAc(arActivity, current_produce);
            arActivity.finish();
        });

        btn_show_recycview = mRootView.findViewById(R.id.btn_show_recycview);
        btn_show_recycview.setOnClickListener(v -> {
            mRecyclerView.setVisibility(View.VISIBLE);
            btn_show_recycview.setVisibility(View.GONE);
            tv_order.setVisibility(View.GONE);
        });
    }

    /**
     * 开始打开相机
     */
    private void startCamera() {
        SurfaceTexture surfaceTexture = mARRenderer.getCameraTexture();
        mARCameraManager.startCamera(surfaceTexture, (result, surfaceTexture1, width, height) -> {
            if (result) {
                if (mARController == null) {
                    showArView();
                }
            }
        });
    }

    /**
     * 开始渲染AR View
     */
    public void showArView() {
        mARController = ARControllerManager.getInstance(getActivity()).getArController();
        mArGLSurfaceView.setOnTouchListener((view, motionEvent) -> {
            if (mARController != null) {
                return mARController.onTouchEvent(motionEvent);
            }
            return false;
        });
        mARCameraManager.setPreviewCallback((data, width, height) -> {
            if (mARController != null) {
                mARController.onCameraPreviewFrame(data, width, height);
            }
        });
        setARRead();
    }

    private void setARRead() {
        mARRenderer.setARRenderCallback(new ARRenderCallback() {
            @Override
            public void onCameraDrawerCreated(SurfaceTexture surfaceTexture, int width, int height) {
                mDuMixSource = new DuMixSource(surfaceTexture, width, height);
                if (TextUtils.isEmpty(mArFile)) {
                    //  mDuMixSource.setArKey(mArKey);
                    mDuMixSource.setArKey("000000");
                    mDuMixSource.setArType(mArTpye);
                }
                //   mPromptUi.setDuMixSource(mDuMixSource);

            }

            @Override
            public void onARDrawerCreated(SurfaceTexture surfaceTexture, SurfaceTexture.OnFrameAvailableListener
                    arFrameListener, int width, int height) {
                if (isScreenOrientationLandscape(getContext())) {
                    mDuMixTarget = new DuMixTarget(surfaceTexture, arFrameListener, height, width, true);
                } else {
                    mDuMixTarget = new DuMixTarget(surfaceTexture, arFrameListener, width, height, true);
                }

                if (mDuMixSource != null) {
                    mDuMixSource.setCameraSource(null);
                }

                if (mARController != null) {
                    mARController.setup(mDuMixSource, mDuMixTarget, mPromptUi.getDuMixCallback());
                    mARController.resume();
                }
            }

            @Override
            public void onARDrawerChanged(SurfaceTexture surfaceTexture, int width, int height) {
                if (mARController != null) {
                    if (SystemInfoUtil.isScreenOrientationLandscape(getContext())) {
                        mARController.reSetup(surfaceTexture, height, width);
                    } else {
                        mARController.reSetup(surfaceTexture, width, height);
                    }
                }
            }
        });
    }

    /**
     * 判断屏幕是否是横屏状态
     *
     * @param context
     */
    public boolean isScreenOrientationLandscape(Context context) {
        // 获取设置的配置信息
        Configuration cf = context.getResources().getConfiguration();
        // 获取屏幕方向
        int ori = cf.orientation;
        if (ori == cf.ORIENTATION_LANDSCAPE) {
            return true;
        }
        return false;
    }

    /**
     * prompt ui callback
     */
    PromptCallback promptCallback = new PromptCallback() {

        @Override
        public void onStartOrderAc() {
            startActivity(new Intent(arActivity, OrderInfoActivity.class));
        }


        @Override
        public void onBackPressed() {
            getActivity().onBackPressed();
        }

        @Override
        public void onChangeCase(String key, int type) {
            if (mARController != null) {
                mARController.switchCase(key, type);
            }
        }

        @Override
        public void onCaseChange() {
            // 向Lua发送消息
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", MsgType.MSG_TYPE_THIRD);
            mARController.sendMessage2Lua(map);
            // 向Lua发送消息 end
        }
    };

    private void initRecycView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false));
        mAdapter = new ArListAdapter(arActivity, null);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(arKey -> {
            tv_order.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            btn_show_recycview.setVisibility(View.VISIBLE);
            if (mARController != null && !TextUtils.isEmpty(arKey)) {
                if (current_produce != arKey) {
                    mARController.switchCase(arKey, 5);
                    current_produce = arKey;
                    mPromptUi.setLoadVisible();
                }
            }
        });
    }


    @Override
    public void backArList(List mData) {
        mAdapter.setListData(mData);
    }

    @Override
    public void backDataFail(String error) {
        ToastyUtil.INSTANCE.showError(error);
    }
}
