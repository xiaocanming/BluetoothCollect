package com.example.ming.bluetoothcollect.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;


import com.inuker.bluetooth.library.BluetoothContext;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;




import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


/**
 * Created by LY on 2019/3/19.
 */
public class MainApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return context;
    }

    private static MainApplication instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance = this;
        // QMUI 框架初始化
        QMUISwipeBackActivityManager.init(this);
        // Bluetoothkit初始化
        BluetoothContext.set(this);
    }

}
