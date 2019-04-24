package com.aier.ardemo.ui.fragment;

import android.arch.lifecycle.ViewModel;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aier.ardemo.bean.BannerItems;
import com.aier.ardemo.ui.base.BaseFragment;
import com.aier.ardemo.R;
import com.aier.ardemo.weight.banner.BannerView;
import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.banner)
    BannerView banner;
    List<BannerItems> list = new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {

        list.clear();
        BannerItems item1 = new BannerItems();
        item1.setId("1");
        item1.setImage_mip(R.drawable.banner_yuyue_1);
        item1.setTitle("第一张展示图片");
        list.add(item1);
        BannerItems item2 = new BannerItems();
        item2.setId("2");
        item2.setImage_mip(R.drawable.banner_yuyue_2);
        item2.setTitle("第二张展示图片");
        list.add(item2);
        BannerItems item3 = new BannerItems();
        item3.setId("3");
        item3.setImage_mip(R.drawable.banner_yuyue_3);
        item3.setTitle("第三张展示图片");
        list.add(item3);

        banner.setViewFactory(new BannerViewFactory());
        banner.setDataList(list);
        banner.start();
    }


    @Override
    protected ViewModel initViewModel() {
        return null;
    }

    public class BannerViewFactory implements BannerView.ViewFactory<BannerItems> {

        @Override
        public View create(final BannerItems bannerItem, int position, ViewGroup container) {
            ImageView iv = new ImageView(container.getContext());
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(bannerItem.image_mip);
           /// ImageUtils.image(getActivity(), bannerItem.image, iv);
            iv.setOnClickListener(v -> {
//                Intent intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("id", bannerItem.id);
//                intent.putExtra("title", bannerItem.title);
//                intent.putExtra("type", 1);
//                startActivity(intent);
            });
            return iv;
        }
    }



}
