package com.example.ming.bluetoothcollect.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.qmuiteam.qmui.arch.QMUIActivity;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.List;

/**
 * Created by LY on 2019/3/21.
 */
@SuppressLint("Registered")
public class BaseActivity extends QMUIActivity  {

    @Override
    protected int backViewInitOffset() {
        return QMUIDisplayHelper.dp2px(MainApplication.getContext(), 100);
    }

}
