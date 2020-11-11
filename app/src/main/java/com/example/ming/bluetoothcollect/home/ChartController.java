package com.example.ming.bluetoothcollect.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.TimePickerView;
import com.example.ming.bluetoothcollect.R;
import com.example.ming.bluetoothcollect.custom.MyMarkerView;
import com.example.ming.bluetoothcollect.custom.MyTextView;
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
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartController extends HomeController {
    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.chart1)
    LineChart chart;
    @BindView(R.id.qmuidemo_floatlayout)
    QMUIFloatLayout mFloatLayout;
    TimePickerView pvTime;
    //血糖最大值
    private  MyTextView MaxTextView;
    //血糖最小值
    private  MyTextView MinTextView;
    //血糖平均值
    private  MyTextView AverageTextView;

    public ChartController(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.fragment_home2, this);
        ButterKnife.bind(this);
        initTimePicker();
        initTopBar();
        initText();
        initChart();
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
    private void initText(){
        MinTextView = new MyTextView(getContext());
        MinTextView.setTitleText("最小值");
        MaxTextView = new MyTextView(getContext());
        MaxTextView.setTitleText("最大值");
        AverageTextView = new MyTextView(getContext());
        AverageTextView.setTitleText("平均值");

        mFloatLayout.addView(MinTextView);
        mFloatLayout.addView(MaxTextView);
        mFloatLayout.addView(AverageTextView);
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
                setData(25, 150);
                //刷新
                chart.animateX(1500);
                Legend l = chart.getLegend();
                l.setForm(Legend.LegendForm.LINE);
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
    private void initChart(){
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
            chart.setDragEnabled(false);
            chart.setScaleEnabled(false);
            chart.setScaleXEnabled(false);
            chart.setScaleYEnabled(false);
            // 设置双指缩放
            chart.setPinchZoom(false);
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
            yAxis.setAxisMaximum(150f);
            yAxis.setAxisMinimum(0f);
        }
        //设置限制线
        {
            LimitLine ll1 = new LimitLine(120f);
            ll1.setLineWidth(1f);
            ll1.enableDashedLine(10f, 10f, 0f);

            LimitLine ll2 = new LimitLine(65f);
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
        setData(25, 150);
        // 随着时间推移绘制点
        chart.animateX(1500);
        // 获取图例（仅在设置数据后才可能）
        Legend l = chart.getLegend();
        // 将图例项绘制为线条
        l.setForm(Legend.LegendForm.LINE);
    }
    //对表格设置数据
    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            float val = (float) (40+Math.random()*(140-40+1));
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // 创建一个数据集并给它一个类型
            set1 = new LineDataSet(values, "血糖值 mg/dl");
            //顶点图标
            set1.setDrawIcons(false);
            // 画虚线
            set1.enableDashedLine(10f, 5f, 0f);
            // 黑线和黑点
            set1.setColor(Color.rgb(212,214,216));
            set1.setCircleColor(Color.rgb(255,64,129));
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
            // 设置填充区域
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });
            // 设置填充区域的颜色
            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
                drawable.setAlpha(50);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets
            // 使用数据集创建数据对象
            LineData data = new LineData(dataSets);
            // 设置数据
            chart.setData(data);
        }
    }


    private String getTimes(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

}
