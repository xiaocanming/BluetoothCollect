package com.example.ming.bluetoothcollect.custom;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.ming.bluetoothcollect.R;

public class MyTextView extends LinearLayout {
    private TextView mTvTitle = null;
    private TextView mTvContent = null;
    public MyTextView(Context context) {
        super(context);
        //将打气筒根据自定义控件的布局文件，创建的view 对象挂载到当前类上面，然后显示
        View view = (View) View.inflate(context, R.layout.custom_text_view, this);

        //获取子控件对象
        mTvTitle = (TextView) view.findViewById(R.id.mytexttitle);
        mTvContent = (TextView) view.findViewById(R.id.mytextcontent);
    }
    public void setTitleText(String text) {
         mTvTitle.setText(text);
    }
    public void setContentText(String text) {
        mTvContent.setText(text);
    }

}
