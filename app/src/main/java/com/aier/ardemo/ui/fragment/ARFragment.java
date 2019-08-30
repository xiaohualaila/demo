
package com.aier.ardemo.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aier.ardemo.adapter.ArListAdapter;
import com.aier.ardemo.adapter.ChildAdapter;
import com.aier.ardemo.bean.DataBean;
import com.aier.ardemo.ui.activity.ARActivity;
import com.aier.ardemo.ui.activity.OrderInfoActivity;
import com.aier.ardemo.ui.activity.ShoppingActivity;
import com.aier.ardemo.ui.contract.ArContract;
import com.aier.ardemo.ui.presenter.ArPresenter;
import com.aier.ardemo.utils.SharedPreferencesUtil;
import com.aier.ardemo.utils.ToastyUtil;
import com.aier.ardemo.weight.AddShoppingBtn;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import android.widget.LinearLayout;
import android.widget.ListView;

public class ARFragment extends Fragment implements ArContract.View, View.OnClickListener,
        AddShoppingBtn.AddShoppingCallBack {

    private static final String TAG = "ARFragment";

    /**
     * Fragment 真正的容器
     */
    private FrameLayout mFragmentContainer;

    private View mRootView;
    private GLSurfaceView mArGLSurfaceView;
    private RecyclerView mRecyclerView;
    private ImageView btn_show_bottom_view;
    private LinearLayout ll_bottom;
    private AddShoppingBtn shoppingBtn;
    private ListView mChildListView;
    private ChildAdapter childAdapter;
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
    private static ARActivity arActivity;
    private String current_produce = "";
    private ArPresenter presenter;
    ArListAdapter mAdapter;
    private DataBean arModel;
    private int shopping_num = 0;

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
        presenter = new ArPresenter(this);
        presenter.getArListData(false, "");
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
        shopping_num = SharedPreferencesUtil.getInt(arActivity, "shoppingData", "shopping_num", 0);
        shoppingBtn.setNumber(shopping_num);
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
        presenter.dispose();
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
        btn_show_bottom_view = mRootView.findViewById(R.id.btn_show_bottom_view);
        btn_show_bottom_view.setOnClickListener(this);
        ll_bottom = mRootView.findViewById(R.id.ll_bottom);
        ll_bottom.setOnClickListener(this);
        shoppingBtn = mRootView.findViewById(R.id.shopping);
        shoppingBtn.setCallBack(this);
        mChildListView = mRootView.findViewById(R.id.child_list);
        childAdapter = new ChildAdapter(arActivity);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_bottom:
                ll_bottom.setVisibility(View.GONE);//隐藏下面列表
                mChildListView.setVisibility(View.VISIBLE);
                btn_show_bottom_view.setVisibility(View.VISIBLE);
                shoppingBtn.setVisibility(View.VISIBLE);

                break;
            case R.id.btn_show_bottom_view:
                mChildListView.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.VISIBLE);//显示下面列表
                btn_show_bottom_view.setVisibility(View.GONE);
                shoppingBtn.setVisibility(View.GONE);
                break;
        }
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
                mPromptUi.setDuMixSource(mDuMixSource);
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
        mAdapter.setOnItemClickListener(ar -> {

            if (mARController != null && !TextUtils.isEmpty(ar.getArkey())) {
                arModel = ar;
                String key = ar.getArkey().trim();
                if (!current_produce.equals(key)) {
                    mARController.switchCase(key, 5);
                    current_produce = key;
                    mPromptUi.setLoadVisible();
                }

                ll_bottom.setVisibility(View.GONE);
                btn_show_bottom_view.setVisibility(View.VISIBLE);
                shoppingBtn.setVisibility(View.VISIBLE);
                presenter.getArListData(true, ar.getGid() + "");
            } else {
                ToastyUtil.INSTANCE.showError("ar key 不能为空！");
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

    @Override
    public void backArChildList(List<DataBean> childList) {
        if (childList.size() > 0) {
            mChildListView.setVisibility(View.VISIBLE);
            childAdapter.setList(childList);
            mChildListView.setAdapter(childAdapter);
            childAdapter.setMyOnItemClickListener(new ChildAdapter.MyOnItemClickListener() {
                @Override
                public void myOnClick(int position) {
                    arModel = childList.get(position);
                    String key = arModel.getArkey().trim();
                    if (!current_produce.equals(key)) {
                        mARController.switchCase(key, 5);
                        current_produce = key;
                        mPromptUi.setLoadVisible();
                    }
                }
            });

        }
    }

    @Override
    public void setCallBack() {
        if (arModel == null) {
            ToastyUtil.INSTANCE.showInfo("请选择要添加的商品！");
            return;
        }
        boolean isAdd = false;
        List<DataBean> arModels;
        String armodels = SharedPreferencesUtil.getString(arActivity, "shoppingData", "shoppings", "");
        if (armodels.isEmpty()) {
            arModels = new ArrayList<>();
            arModel.setNum(1);
            arModels.add(arModel);
            shopping_num = 1;
            SharedPreferencesUtil.putString(arActivity, "shoppingData", "shoppings", new Gson().toJson(arModels));
            SharedPreferencesUtil.putInt(arActivity, "shoppingData", "shopping_num", shopping_num);
        } else {
            Gson gson = new Gson();
            arModels = gson.fromJson(armodels, new TypeToken<List<DataBean>>() {
            }.getType());
            for (DataBean bean : arModels) {
                if (arModel.getArkey().equals(bean.getArkey())) {
                    int num = bean.getNum();
                    bean.setNum(num + 1);
                    isAdd = true;
                }
            }
            if (!isAdd) {
                arModel.setNum(1);
                arModels.add(arModel);
            }
            shopping_num += 1;
        }
        shoppingBtn.setNumber(shopping_num);
        SharedPreferencesUtil.putString(arActivity, "shoppingData", "shoppings", new Gson().toJson(arModels));
        SharedPreferencesUtil.putInt(arActivity, "shoppingData", "shopping_num", shopping_num);
    }

    @Override
    public void setToBuyCallBack() {
        if (shopping_num == 0) {
            ToastyUtil.INSTANCE.showInfo("请先加入购物车");
            return;
        }
        ShoppingActivity.starShoppingAc(arActivity, arModel);
        //arActivity.finish();
    }

    public static void closeActivity() {
        arActivity.finish();
    }


}
