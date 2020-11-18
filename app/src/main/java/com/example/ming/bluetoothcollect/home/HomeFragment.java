package com.example.ming.bluetoothcollect.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.ming.bluetoothcollect.R;
import com.example.ming.bluetoothcollect.base.BaseFragment;
import com.example.ming.bluetoothcollect.controller.ClientManager;
import com.example.ming.bluetoothcollect.controller.DbManager;
import com.example.ming.bluetoothcollect.custom.NoSlidingViewPager;
import com.example.ming.bluetoothcollect.model.Device;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class  HomeFragment extends BaseFragment {

    @BindView(R.id.main_view_pager)
    NoSlidingViewPager mViewPager;
    @BindView(R.id.tabs)
    QMUITabSegment mTabSegment;
    private HashMap<Pager, HomeController> mPages;
    //标签页适配器
    private PagerAdapter mPagerAdapter = new PagerAdapter() {

        private int mChildCount = 0;

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mPages.size();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            HomeController page = mPages.get(Pager.getPagerFromPositon(position));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(page, params);
            return page;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount == 0) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }
    };
    @Override
    protected View onCreateView() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, layout);
        initTabs();
        initPagers();
        return layout;
    }

    private void initTabs() {
        QMUITabBuilder builder = mTabSegment.tabBuilder();
        builder.setTypeface(null, Typeface.DEFAULT_BOLD);
        builder.setSelectedIconScale(1.2f)
                .setTextSize(QMUIDisplayHelper.sp2px(getContext(), 13), QMUIDisplayHelper.sp2px(getContext(), 15))
                .setDynamicChangeIconColor(false);
        QMUITab component = builder
                .setNormalDrawable(androidx.core.content.ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_setting))
                .setSelectedDrawable(androidx.core.content.ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_setting_selected))
                .setText("设备设置")
                .build(getContext());
        QMUITab util = builder
                .setNormalDrawable(androidx.core.content.ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_chart))
                .setSelectedDrawable(androidx.core.content.ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_chart_selected))
                .setText("数据查看")
                .build(getContext());
        QMUITab lab = builder
                .setNormalDrawable(androidx.core.content.ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_file))
                .setSelectedDrawable(androidx.core.content.ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_file_selected))
                .setText("日志管理")
                .build(getContext());

        mTabSegment.addTab(component)
                .addTab(util)
                .addTab(lab);
    }

    private void initPagers() {
        HomeController.HomeControlListener listener = new HomeController.HomeControlListener() {
            @Override
            public void startFragment(BaseFragment fragment) {
                HomeFragment.this.startFragment(fragment);

            }
            @Override
            public void startActivity(Intent intent) {
                HomeFragment.this.startActivity(intent);
            }
        };

        mPages = new HashMap<>();

        HomeController homeComponentsController = new SettingController(getActivity());
        homeComponentsController.setHomeControlListener(listener);
        mPages.put(Pager.COMPONENT, homeComponentsController);

        HomeController homeUtilController = new ChartController(getActivity());
        homeUtilController.setHomeControlListener(listener);
        mPages.put(Pager.UTIL, homeUtilController);

        HomeController homeLabController = new FileController(getActivity());
        homeLabController.setHomeControlListener(listener);
        mPages.put(Pager.LAB, homeLabController);

        mViewPager.setAdapter(mPagerAdapter);
        mTabSegment.setupWithViewPager(mViewPager, false);
    }

    enum Pager {
        COMPONENT, UTIL, LAB;

        public static Pager getPagerFromPositon(int position) {
            switch (position) {
                case 0:
                    return COMPONENT;
                case 1:
                    return UTIL;
                case 2:
                    return LAB;
                default:
                    return COMPONENT;
            }
        }
    }
    @Override
    protected boolean canDragBack() {
        return false;
    }

    @Override
    public Object onLastFragmentFinish() {
        return null;
    }

    //断开蓝牙连接
    @Override
    public void onPause() {
        super.onPause();
        ClientManager.getClient().stopSearch();
    }

    //断开蓝牙连接
    @Override
    public void onDestroy() {
        Device newUseDevice= DbManager.getClient().searchDeviceInfo();
        ClientManager.getClient().disconnect(newUseDevice.getAddress());
        super.onDestroy();
    }
}
