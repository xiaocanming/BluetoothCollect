package com.example.ming.bluetoothcollect.home;

import android.content.Context;
import android.content.Intent;

import com.example.ming.bluetoothcollect.base.BaseFragment;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;

public class HomeController extends QMUIWindowInsetLayout {
    private HomeControlListener mHomeControlListener;

    public HomeController(Context context) {
        super(context);
    }

    protected void startFragment(BaseFragment fragment) {
        if (mHomeControlListener != null) {
            mHomeControlListener.startFragment(fragment);
        }
    }

    protected void startActivity(Intent intent) {
        if (mHomeControlListener != null) {
            mHomeControlListener.startActivity(intent);
        }
    }

    public void setHomeControlListener(HomeControlListener homeControlListener) {
        mHomeControlListener = homeControlListener;
    }
    public interface HomeControlListener {
        void startFragment(BaseFragment fragment);
        void startActivity(Intent intent);
    }

}
