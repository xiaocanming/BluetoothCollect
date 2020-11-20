package com.example.ming.bluetoothcollect.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.example.ming.bluetoothcollect.R;


import java.util.ArrayList;
import java.util.List;

public class BerWheel<T> {
    protected View view;
    protected LinearLayout llContent;
    protected List<WheelView> wvList = new ArrayList<>();
    protected List<List<T>> items = new ArrayList<>();

    protected OnItemSelectedListener wheelListener_option1;
    protected OnItemSelectedListener wheelListener_option2;

    //文字的颜色和分割线的颜色
    protected int textColorOut;
    protected int textColorCenter;
    protected int dividerColor;

    protected WheelView.DividerType dividerType;
    protected Context context;

    // 条目间距倍数
    protected float lineSpacingMultiplier = 1.6F;
    protected int total;// 列表数量

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public BerWheel(View view, Context context, int total) {
        super();
        this.view = view;
        this.context = context;
        this.total = total;
        llContent = (LinearLayout) view.findViewById(R.id.optionspicker);

        showWheelView();
    }

    /**
     * 设置展示规则 默认为平均分布展示
     * 可以定义子类来重写该类来制定展示的规则
     */
    protected void showWheelView(){
        for (int i = 0; i < total; i++) {
            WheelView wheelview = new WheelView(context);
            wheelview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
            wvList.add(wheelview);
        }

        View view1 = new View(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,3,1);
        lp.gravity = Gravity.CENTER;
        view1.setLayoutParams(lp);
        view1.setPadding(10,0,10,0);
        view1.setBackgroundResource(R.color.black);

        TextView textview1 = new TextView(context);
        textview1.setText(":");
        LinearLayout.LayoutParams tvlp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvlp1.gravity = Gravity.CENTER;
        textview1.setLayoutParams(tvlp1);
        TextView textview2 = new TextView(context);
        textview2.setText(":");
        LinearLayout.LayoutParams tvlp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvlp2.gravity = Gravity.CENTER;
        textview2.setLayoutParams(tvlp2);


        llContent.addView(wvList.get(0));
        llContent.addView(textview1);
        llContent.addView(wvList.get(1));
        llContent.addView(view1);
        llContent.addView(wvList.get(2));
        llContent.addView(textview2);
        llContent.addView(wvList.get(3));
    }


    //不联动情况下
    public void setNPicker(List<List<T>> items) {
        this.items = items;
        int count = wvList.size() > items.size() ? items.size() : wvList.size();
        for (int i = 0; i < count; i++) {
            if (wvList.get(i) != null) {
                wvList.get(i).setVisibility(View.VISIBLE);
                wvList.get(i).setAdapter(new ArrayWheelAdapter(items.get(i)));
                wvList.get(i).setCurrentItem(0);
                wvList.get(i).setIsOptions(true);
            }else {
                wvList.get(i).setVisibility(View.GONE);
            }
        }
    }


    public void setTextContentSize(int textSize) {
        for (int i = 0; i < wvList.size(); i++) {
            if (wvList.get(i) != null) {
                wvList.get(i).setTextSize(textSize);
            }
        }
    }

    private void setTextColorOut() {
        for (int i = 0; i < wvList.size(); i++) {
            if (wvList.get(i) != null) {
                wvList.get(i).setTextColorOut(textColorOut);
            }
        }
    }

    private void setTextColorCenter() {
        for (int i = 0; i < wvList.size(); i++) {
            if (wvList.get(i) != null) {
                wvList.get(i).setTextColorCenter(textColorCenter);
            }
        }
    }

    private void setDividerColor() {
        for (int i = 0; i < wvList.size(); i++) {
            if (wvList.get(i) != null) {
                wvList.get(i).setDividerColor(dividerColor);
            }
        }
    }

    private void setDividerType() {
        for (int i = 0; i < wvList.size(); i++) {
            if (wvList.get(i) != null) {
                wvList.get(i).setDividerType(dividerType);
            }
        }
    }

    private void setLineSpacingMultiplier() {
        for (int i = 0; i < wvList.size(); i++) {
            if (wvList.get(i) != null) {
                wvList.get(i).setLineSpacingMultiplier(lineSpacingMultiplier);
            }
        }
    }

    /**
     * 设置选项的单位
     */
    public void setLabels(String...label) {
        int count = label.length > wvList.size() ? wvList.size() : label.length;
        for (int i = 0; i < count; i++) {
            if (wvList.get(i) != null && label[i] != null) {
                wvList.get(i).setLabel(label[i]);
            }
        }
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic 是否循环
     */
    public void setCyclic(boolean...cyclic) {
        int count = cyclic.length > wvList.size() ? wvList.size() : cyclic.length;
        for (int i = 0; i < count; i++) {
            if (wvList.get(i) != null) {
                wvList.get(i).setCyclic(cyclic[i]);
            }
        }
    }

    /**
     * 设置字体样式
     *
     * @param font 系统提供的几种样式
     */
    public void setTypeface (Typeface font) {
        for (int i = 0; i < wvList.size(); i++) {
            if (wvList.get(i) != null) {
                wvList.get(i).setTypeface(font);
            }
        }
    }

    /**
     * 分别设置第一二三级是否循环滚动
     */
//    public void setCyclic(boolean...cyclic) {
//        int count = cyclic.length > wvList.size() ? wvList.size() : cyclic.length;
//        for (int i = 0; i < count; i++) {
//            if (wvList.get(i) != null) {
//                wvList.get(i).setCyclic(cyclic[i]);
//            }
//        }
//    }



    public void setCurrentItems(int...option) {
        int count = option.length > wvList.size() ? wvList.size() : option.length;
        for (int i = 0; i < count; i++) {
            if (wvList.get(i) != null) {
                wvList.get(i).setCurrentItem(option[i]);
            }
        }
    }


    /**
     * 设置间距倍数,但是只能在1.2-2.0f之间
     *
     * @param lineSpacingMultiplier
     */
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
        setLineSpacingMultiplier();
    }

    /**
     * 设置分割线的颜色
     *
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        setDividerColor();
    }

    /**
     * 设置分割线的类型
     *
     * @param dividerType
     */
    public void setDividerType(WheelView.DividerType dividerType) {
        this.dividerType = dividerType;
        setDividerType();
    }
    /**
     * 设置分割线之间的文字的颜色
     *
     * @param textColorCenter
     */
    public void setTextColorCenter(int textColorCenter) {
        this.textColorCenter = textColorCenter;
        setTextColorCenter();
    }

    /**
     * 设置分割线以外文字的颜色
     *
     * @param textColorOut
     */
    public void setTextColorOut(int textColorOut) {
        this.textColorOut = textColorOut;
        setTextColorOut();
    }

    /**
     * Label 是否只显示中间选中项的
     *
     * @param isCenterLabel
     */

    public void isCenterLabel(Boolean isCenterLabel) {
        for (int i = 0; i < wvList.size(); i++) {
            if (wvList.get(i) != null) {
                wvList.get(i).isCenterLabel(isCenterLabel);
            }
        }
    }

    public List<T> getCurrentItems() {
        List<T> datalist = new ArrayList<>();
        for (int i = 0; i < wvList.size(); i++) {
            if (wvList.get(i) != null) {
                datalist.add(items.get(i).get(wvList.get(i).getCurrentItem()));
            }
        }
        return datalist;
    }

}
