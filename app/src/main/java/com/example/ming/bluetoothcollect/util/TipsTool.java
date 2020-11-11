package com.example.ming.bluetoothcollect.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.example.ming.bluetoothcollect.base.MainApplication;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TipsTool {
    public  static  void showtipDialog(Context context,String tip,@QMUITipDialog.Builder.IconType int iconType){
        final QMUITipDialog tipDialog;
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(iconType)
                .setTipWord(tip)
                .create();
        tipDialog.show();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
            }
        }, 1500);
    }


}
