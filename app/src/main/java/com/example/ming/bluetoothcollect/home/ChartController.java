package com.example.ming.bluetoothcollect.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.TimePickerView;
import com.example.ming.bluetoothcollect.R;
import com.example.ming.bluetoothcollect.controller.DbManager;
import com.example.ming.bluetoothcollect.custom.MyMarkerView;
import com.example.ming.bluetoothcollect.custom.MyTextView;
import com.example.ming.bluetoothcollect.model.Device;
import com.example.ming.bluetoothcollect.model.NotifyInfo;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Thread.sleep;

public class ChartController extends HomeController {
    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.chart1)
    LineChart chart;
    TimePickerView pvTime;
    private ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(5);
    private Date curDate;

    public ChartController(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.fragment_home2, this);
        ButterKnife.bind(this);
        initTimePicker();
        initTopBar();
        initChart();
//        initGetData();
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchSaveInstanceState(container);
    }

    //重新加载页面
    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
    }

    private void initTopBar() {
        mTopBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.app_color_theme_6));
        mTopBar.setTitle("血糖值记录");
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_date, QMUIViewHelper.generateViewId()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });
    }

    private void initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2013, 0, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2029, 11, 28);
        //时间选择器
        pvTime = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                curDate = date;
//                ui_task.run();
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "", "")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .build();
    }

    private void initChart() {
        //设置表格属性
        {
            // 背景色
            chart.setBackgroundColor(Color.WHITE);
            // 禁用说明文字
            chart.getDescription().setEnabled(false);
            // 启用触摸手势
            chart.setTouchEnabled(true);
            chart.setDrawGridBackground(false);
            //显示边界
            chart.setDrawBorders(false);
            // 设置拖拽、缩放等
            chart.setDragEnabled(true);
//            chart.setScaleEnabled(true);
            chart.setScaleXEnabled(true);
            chart.setScaleYEnabled(false);
            // 设置双指缩放
//            chart.setPinchZoom(true);
            // 创建标记以在选定值时显示框
            MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
            // 将标记设置为图表
            mv.setChartView(chart);
            chart.setMarker(mv);
        }
        //设置X轴属性
        XAxis xAxis;
        {
            xAxis = chart.getXAxis();
            //X轴设置显示位置在底部
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);

            // axis range
            xAxis.setAxisMaximum(24f);
            xAxis.setAxisMinimum(0f);
        }
        //设置y轴属性
        YAxis yAxis;
        {
            yAxis = chart.getAxisLeft();
            //禁用双轴（仅使用左轴）
            chart.getAxisRight().setEnabled(false);
            yAxis.setDrawGridLines(false);

            // axis range
            yAxis.setAxisMaximum(30f);
            yAxis.setAxisMinimum(0f);
        }
        //设置限制线
        {
            LimitLine ll1 = new LimitLine(20f);
            ll1.setLineWidth(1f);
            ll1.enableDashedLine(10f, 10f, 0f);

            LimitLine ll2 = new LimitLine(10f);
            ll2.setLineWidth(1f);
            ll2.enableDashedLine(10f, 10f, 0f);

            // 在数据后面而不是顶部绘制限制线
            yAxis.setDrawLimitLinesBehindData(false);
            xAxis.setDrawLimitLinesBehindData(false);

            // add limit lines
            yAxis.addLimitLine(ll1);
            yAxis.addLimitLine(ll2);
        }
        // 添加数据
        setData();
        // 随着时间推移绘制点
        chart.animateX(1500);
        // 获取图例（仅在设置数据后才可能）
        Legend l = chart.getLegend();
        // 将图例项绘制为线条
        l.setForm(Legend.LegendForm.LINE);
    }

    //对表格设置数据
    private void setData() {
        ArrayList<Entry> values = new ArrayList<>();


        //获取当前日期所有数据
        Calendar calendar = Calendar.getInstance();
        curDate = calendar.getTime();
        Device device = DbManager.getClient().searchDeviceInfo();
        List<NotifyInfo> notifyInfos = DbManager.getClient().searchNotifyInfoByWhere(device.getAddress(), curDate);
        SimpleDateFormat formatHH = new SimpleDateFormat("HH.mm");
        SimpleDateFormat formatSS = new SimpleDateFormat("ss.SSSS");
        for (NotifyInfo notifyInfo : notifyInfos
        ) {
            //获取X值
            Float HH = Float.valueOf(formatHH.format(notifyInfo.getTime()));
            Float SS = Float.valueOf(formatSS.format(notifyInfo.getTime()));
            float xVale = HH + SS / 60;
            Entry entry = new Entry(xVale, notifyInfo.getMessage());
            values.add(entry);
        }

        values.add(new Entry(0,0));
        LineDataSet set1;
        if (chart.getData() == null ) {
            // 创建一个数据集并给它一个类型
            set1 = new LineDataSet(values, "血糖值 nA");
            //顶点图标
            set1.setDrawIcons(false);
            // 画虚线
            set1.enableDashedLine(10f, 5f, 0f);
            // 黑线和黑点
            set1.setColor(Color.rgb(212, 214, 216));
            set1.setCircleColor(Color.rgb(255, 64, 129));
            // 线条粗细和点尺寸
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            // 将点绘制为实心圆
            set1.setDrawCircleHole(false);
            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            //值的文本大小
            set1.setValueTextSize(9f);
            // 将选择线绘制为虚线
            set1.enableDashedHighlightLine(10f, 5f, 0f);
//            // 设置填充区域
//            set1.setDrawFilled(true);
//            set1.setFillFormatter(new IFillFormatter() {
//                @Override
//                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                    return chart.getAxisLeft().getAxisMinimum();
//                }
//            });
//            // 设置填充区域的颜色
//            if (Utils.getSDKInt() >= 18) {
//                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
//                drawable.setAlpha(50);
//                set1.setFillDrawable(drawable);
//            } else {
//                set1.setFillColor(Color.BLACK);
//            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets
            // 使用数据集创建数据对象
            LineData data = new LineData(dataSets);
            // 设置数据
            chart.setData(data);
        }
    }

    /**
     * 为最后一个DataSet添加一个y值随机的Entry
     */
    private void initGetData() {
        Calendar calendar = Calendar.getInstance();
        curDate = calendar.getTime();
        // delay（延时）指的是一次执行终止和下一次执行开始之间的延迟
//        mScheduledExecutorService.scheduleWithFixedDelay(ui_task, 10000, 10000, TimeUnit.MILLISECONDS);
    }

//    Runnable ui_task = new Runnable() {
//        public void run() {
//            //获取设备信息
//            Device device = DbManager.getClient().searchDeviceInfo();
//            //获取当前日期所有数据
//            List<NotifyInfo> notifyInfos = DbManager.getClient().searchNotifyInfoByWhere(device.getAddress(), curDate);
//            List<Entry> entryList = new ArrayList<>();
//            SimpleDateFormat formatHH = new SimpleDateFormat("HH.mm");
//            SimpleDateFormat formatSS = new SimpleDateFormat("ss.SSSS");
//            for (NotifyInfo notifyInfo : notifyInfos
//            ) {
//                //获取X值
//                Float HH = Float.valueOf(formatHH.format(notifyInfo.getTime()));
//                Float SS = Float.valueOf(formatSS.format(notifyInfo.getTime()));
//                float xVale = HH + SS / 60;
//                Entry entry = new Entry(xVale, notifyInfo.getMessage());
//                entryList.add(entry);
//            }
//            chart.post(new Runnable() {
//                @Override
//                public void run() {
//
//
//                    LineDataSet set1;
//
//                    if (chart.getData() != null &&
//                            chart.getData().getDataSetCount() > 0) {
//                        set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
//                        set1.setValues(values);
//                        set1.notifyDataSetChanged();
//                        chart.getData().notifyDataChanged();
//                        chart.notifyDataSetChanged();
//                    } else {
//                        // create a dataset and give it a type
//                        set1 = new LineDataSet(values, "DataSet 1");
//
//                        set1.setDrawIcons(false);
//
//                        // draw dashed line
//                        set1.enableDashedLine(10f, 5f, 0f);
//
//                        // black lines and points
//                        set1.setColor(Color.BLACK);
//                        set1.setCircleColor(Color.BLACK);
//
//                        // line thickness and point size
//                        set1.setLineWidth(1f);
//                        set1.setCircleRadius(3f);
//
//                        // draw points as solid circles
//                        set1.setDrawCircleHole(false);
//
//                        // customize legend entry
//                        set1.setFormLineWidth(1f);
//                        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//                        set1.setFormSize(15.f);
//
//                        // text size of values
//                        set1.setValueTextSize(9f);
//
//                        // draw selection line as dashed
//                        set1.enableDashedHighlightLine(10f, 5f, 0f);
//
//                        // set the filled area
//                        set1.setDrawFilled(true);
//                        set1.setFillFormatter(new IFillFormatter() {
//                            @Override
//                            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                                return chart.getAxisLeft().getAxisMinimum();
//                            }
//                        });
//
//                        // set color of filled area
//                        if (Utils.getSDKInt() >= 18) {
//                            // drawables only supported on api level 18 and above
//                            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
//                            set1.setFillDrawable(drawable);
//                        } else {
//                            set1.setFillColor(Color.BLACK);
//                        }
//
//                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//                        dataSets.add(set1); // add the data sets
//
//                        // create a data object with the data sets
//                        LineData data = new LineData(dataSets);
//
//                        // set data
//                        chart.setData(data);
//                    }
//
//
//                    if(entryList.size()>0){
//                        LineData data = chart.getData();
//                        // 生成随机测试数
//                        for (Entry entry : entryList
//                        ) {
//                            data.addEntry(entry, 0);
//                        }
//                        LineDataSet set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
//                        chart.getData().notifyDataChanged();
//                        chart.notifyDataSetChanged();
//                        chart.invalidate();
//                        Log.d("XCM", String.valueOf(entryList.size()));
//                    }
//                }
//            });
//        }
//    };
}
