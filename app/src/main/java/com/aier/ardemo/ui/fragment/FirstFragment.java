package com.aier.ardemo.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.adapter.CourierAdapter;
import com.aier.ardemo.adapter.ProduceAdapter;
import com.aier.ardemo.bean.Produces;
import com.aier.ardemo.bean.ResultBean;
import com.aier.ardemo.ui.activity.MainActivity;
import com.aier.ardemo.ui.activity.ScanActivity;
import com.aier.ardemo.ui.base.BaseFragment;
import com.aier.ardemo.ui.contract.FirstContract;
import com.aier.ardemo.ui.presenter.FirstPresenter;
import com.aier.ardemo.utils.NetUtil;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class FirstFragment extends BaseFragment implements TabLayout.OnTabSelectedListener, FirstContract.View {
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.iv_weather)
    ImageView iv_weather;
    @BindView(R.id.tv_wendu)
    TextView tv_wendu;
    @BindView(R.id.rv_order_info)
    RecyclerView mRecyclerView;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.rv_courier)
    RecyclerView rv_courier;
    @BindView(R.id.tv_toutiao_content)
    TextView tv_toutiao_content;

    private MainActivity ac;
    ProduceAdapter mAdapter;
    CourierAdapter courierAdapter;
    List<Produces> list = new ArrayList();
    private  int total;
    private  int currentIndex;
    List<ResultBean.DataBean> dataBeanList;
    private FirstPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    protected void init() {
        ac = (MainActivity) getActivity();
        presenter = new FirstPresenter( this);
        initData();
    }

    private void initData() {
        if(NetUtil.isConnected(mActivity)){
            presenter.getWeatherData();
            presenter.getWenzhangData();
            initTablayout();
            initRecycView();
        }
    }

    private void initTablayout() {
        tablayout.addTab(tablayout.newTab().setText("设计"));
        tablayout.addTab(tablayout.newTab().setText("生产"));
        tablayout.addTab(tablayout.newTab().setText("物流"));
        tablayout.addTab(tablayout.newTab().setText("服务"));
        tablayout.setOnTabSelectedListener(this);

    }

    private void initRecycView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ac, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ProduceAdapter(getDataOrder());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        rv_courier.setLayoutManager(new LinearLayoutManager(ac, LinearLayoutManager.VERTICAL, false));
        courierAdapter = new CourierAdapter(getTab1Data());
        rv_courier.setItemAnimator(new DefaultItemAnimator());
        rv_courier.setAdapter(courierAdapter);
    }

    private List<Produces> getDataOrder() {
        List<Produces> list = new ArrayList();
        list.add(new Produces("2019-5-27 10:20:25","中国家具智能制造创新中心开始生产您的椅子备料，您可以点击VR按钮查看生产过程"));
        list.add(new Produces("2019-5-27 10:18:32","您的智能实木椅子设计完毕"));
        list.add(new Produces("2019-5-27 09:15:16","江西分寸制造所开始设计您的个性化椅子"));
        list.add(new Produces("2019-5-27 09:10:32","您定制了高端智能实木椅"));
        return list;
    }

    private List<Produces> getTab1Data() {
        list.add(new Produces("2019-5-27 18:10:07","客户最终确认设计方案"));
        list.add(new Produces("2019-5-20 18:10:07","修改设计方案完成"));
        list.add(new Produces("2019-5-15 12:10:07","客户提出修改设计方案"));
        list.add(new Produces("2019-5-11 16:12:07","设计初步完成"));
        return list;
    }

    private List<Produces> getTab2Data() {
        list.add(new Produces("2019-5-27 18:10:07","您的订单已组装生成完成"));
        list.add(new Produces("2019-5-25 12:10:07","排程完成"));
        list.add(new Produces("2019-5-20 16:12:07","备料完成"));
        return list;
    }
    private List<Produces> getTab3Data() {
        list.add(new Produces("","暂无数据"));
        return list;
    }

    /**
     * 发送心跳数据
     */
    private void heartinterval() {
        Observable.interval(2, 10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    currentIndex ++;
                    if(currentIndex == total){
                        currentIndex =0;
                    }
                    try {
                        tv_toutiao_content.setText(dataBeanList.get(currentIndex).getTitle());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @OnClick({R.id.iv_check,R.id.iv_zhen,R.id.tv_vr_video,R.id.toutiao_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_check:
                /*以下是启动我们自定义的扫描活动*/
                IntentIntegrator intentIntegrator = new IntentIntegrator(ac);
                intentIntegrator.setBeepEnabled(true);
                /*设置启动我们自定义的扫描活动，若不设置，将启动默认活动*/
                intentIntegrator.setCaptureActivity(ScanActivity.class);
                intentIntegrator.initiateScan();
                break;
            case R.id.iv_zhen:
                if(!NetUtil.isConnected(mActivity)){
                    showToast("网络异常，请检查网络！");
                    return;
                }
                ac.goToArActivity();
                break;
            case R.id.tv_vr_video:
                ac.goToVRVideoActivity();
                break;
            case R.id.toutiao_item:
                if(dataBeanList!=null){
                  ResultBean.DataBean bean =dataBeanList.get(currentIndex);
                  ac.goToWebActivity(bean.getUrl(),bean.getTitle());
                }
                break;
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
    //选中了tab的逻辑
        int position =tab.getPosition();
           switch (position){
               case 0:
                   list.clear();
                   getTab1Data();
                   courierAdapter.notifyDataSetChanged();
                   break;
               case 1:
                   list.clear();
                   getTab2Data();
                   courierAdapter.notifyDataSetChanged();
                   break;
               case 2:
               case 3:
                   list.clear();
                   getTab3Data();
                   courierAdapter.notifyDataSetChanged();
                   break;
           }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void getWeatherDataSuccess(String city, String wendu) {
        tv_city.setText(city);
        tv_wendu.setText(wendu);//dataBean.getWea()天气
    }

    @Override
    public void getWenzhangDataSuccess(ResultBean bean) {
          dataBeanList = bean.getData();
        if(dataBeanList.size()>0){
            total =dataBeanList.size();
            currentIndex= 0;
            tv_toutiao_content.setText(dataBeanList.get(currentIndex).getTitle());
            heartinterval();
        }
    }

    @Override
    public void getDataFail() {
        showToast("请求失败");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dispose();
    }
}
