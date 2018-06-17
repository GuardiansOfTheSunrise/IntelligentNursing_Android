package com.gots.intelligentnursing.customview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.gots.intelligentnursing.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhqy
 * @date 2018/6/14
 */

public class LineChartPager extends LinearLayout {

    private static final String LABEL_AVERAGE = "平均值";
    private static final String LABEL_MAX = "上限预警";
    private static final String LABEL_MIN = "下限预警";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private ViewPagerAdapter mViewPagerAdapter;

    public LineChartPager(Context context) {
        super(context);
        setOrientation(VERTICAL);
        initTabLayout(context);
        initViewPager(context);
    }

    public LineChartPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        initTabLayout(context);
        initViewPager(context);
    }

    public LineChartPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        initTabLayout(context);
        initViewPager(context);
    }

    private void initTabLayout(Context context) {
        mTabLayout = new TabLayout(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mTabLayout.setLayoutParams(lp);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        addView(mTabLayout);
    }

    private void initViewPager(Context context) {
        mViewPager = new ViewPager(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(lp);
        mViewPagerAdapter = new ViewPagerAdapter();
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        addView(mViewPager);
    }


    private void initLineChart(LineChart lineChart, LineChartData data) {
        Description description = new Description();
        description.setText(data.mDescribe);
        lineChart.setDescription(description);

        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(true);
        lineChart.setBackgroundColor(Color.WHITE);

        XAxis xAxis = lineChart.getXAxis();
        if (data.mXAxisValue.length > 1) {
            xAxis.setGranularity(data.mXAxisValue[1] - data.mXAxisValue[0]);
        } else {
            xAxis.setGranularity(1f);
        }
        xAxis.setAxisMinimum(data.mXAxisValue[0]);
        xAxis.setAxisMaximum(data.mXAxisValue[data.mXAxisValue.length - 1]);
        xAxis.setLabelCount(data.mXAxisValue.length, true);
        xAxis.setTextColor(Color.BLUE);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setEnabled(true);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        if (data.mXFormatter != null) {
            xAxis.setValueFormatter(data.mXFormatter);
        }

        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setDrawLabels(true);
        leftYAxis.setTextColor(Color.BLUE);
        leftYAxis.setSpaceTop(20);
        leftYAxis.setSpaceBottom(20);
        leftYAxis.setDrawGridLines(true);
        leftYAxis.enableGridDashedLine(10f, 10f, 0f);
        if (data.mYFormatter != null) {
            leftYAxis.setValueFormatter(data.mYFormatter);
        }

        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);

        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
    }


    private void initLineDataSet(LineDataSet lineDataSet) {
        int color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> String.valueOf((int) value));
    }

    private void initWarningDataSet(LineDataSet lineDataSet, int color) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(0.8f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(0f);
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    }


    private void createWarningLineDataSet(LineChartData lineChartData, List<ILineDataSet> dataSets) {
        List<Entry> minValues = new ArrayList<>(lineChartData.mYAxisValue.length);
        List<Entry> maxValues = new ArrayList<>(lineChartData.mYAxisValue.length);
        for (int i = 0; i < lineChartData.mYAxisValue.length; i++) {
            minValues.add(new Entry(lineChartData.mXAxisValue[i], lineChartData.mMinValue));
            maxValues.add(new Entry(lineChartData.mXAxisValue[i], lineChartData.mMaxValue));
        }
        LineDataSet minDataSet = new LineDataSet(minValues, LABEL_MIN + "(" + (int) lineChartData.mMinValue + ")");
        LineDataSet maxDataSet = new LineDataSet(maxValues, LABEL_MAX + "(" + (int) lineChartData.mMaxValue + ")");
        initWarningDataSet(minDataSet, Color.rgb(0xEE, 0x00, 0xEE));
        initWarningDataSet(maxDataSet, Color.RED);
        dataSets.add(minDataSet);
        dataSets.add(maxDataSet);
    }

    public void setData(List<LineChartData> chartDataList) {
        for (LineChartData chartData : chartDataList) {
            LineChart lineChart = new LineChart(getContext());
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lineChart.setLayoutParams(lp);
            initLineChart(lineChart, chartData);
            mViewPagerAdapter.addView(lineChart);

            List<ILineDataSet> dataSets = new ArrayList<>(1);
            List<Entry> values = new ArrayList<>(chartData.mYAxisValue.length);
            for (int i = 0; i < chartData.mYAxisValue.length; i++) {
                values.add(new Entry(chartData.mXAxisValue[i], chartData.mYAxisValue[i]));
            }

            LineDataSet lineDataSet = new LineDataSet(values, LABEL_AVERAGE);
            initLineDataSet(lineDataSet);
            dataSets.add(lineDataSet);

            createWarningLineDataSet(chartData, dataSets);

            LineData lineData = new LineData(dataSets);
            lineChart.setData(lineData);

            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setText(chartData.mDescribe);
            mTabLayout.addTab(tab);
        }
        mViewPagerAdapter.notifyDataSetChanged();
    }
    
    public void removeAll() {
        mTabLayout.removeAllTabs();
        mViewPager.removeAllViews();
        mViewPagerAdapter.mViews.clear();
    }

    public static class LineChartData {
        private String mDescribe;
        private float[] mXAxisValue;
        private float[] mYAxisValue;
        private float mMinValue;
        private float mMaxValue;
        private IAxisValueFormatter mXFormatter;
        private IAxisValueFormatter mYFormatter;

        public LineChartData(String describe, float[] xAxisValue, float[] yAxisValue, float minValue, float maxValue,
                             IAxisValueFormatter xFormatter, IAxisValueFormatter yFormatter) {
            mDescribe = describe;
            mXAxisValue = xAxisValue;
            mYAxisValue = yAxisValue;
            mMinValue = minValue;
            mMaxValue = maxValue;
            mXFormatter = xFormatter;
            mYFormatter = yFormatter;
        }
    }

    private static class ViewPagerAdapter extends PagerAdapter {
        private List<View> mViews = new ArrayList<>();

        private void addView(View view) {
            mViews.add(view);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
