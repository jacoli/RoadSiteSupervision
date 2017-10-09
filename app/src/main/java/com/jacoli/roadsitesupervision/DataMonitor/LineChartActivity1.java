
package com.jacoli.roadsitesupervision.DataMonitor;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.hh.timeselector.timeutil.datedialog.DateListener;
import com.hh.timeselector.timeutil.datedialog.TimeConfig;
import com.hh.timeselector.timeutil.datedialog.TimeSelectorDialog;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.Utils.CommonUtils;
import com.mingle.widget.LoadingView;
//import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView;
//import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineChartActivity1 extends CommonActivity implements
        OnChartGestureListener, OnChartValueSelectedListener {

    private LineChart mChart;
    //private SeekBar mSeekBarX, mSeekBarY;
    //private TextView tvX, tvY;
    @BindView(R.id.text)
    TextView textView;

    @BindView(R.id.submit_btn)
    Button button;

    @BindView(R.id.text_placehoder)
    TextView placeHolderTextView;

    @BindView(R.id.loadView)
    LoadingView loadingView;

    @BindView(R.id.chart_container)
    LinearLayout chartContainer;

    private String selectedDayStr;
    private boolean isSpecialType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);

        ButterKnife.bind(this);

        createTitleBar();
        titleBar.setTitle(getIntent().getStringExtra("title"));

        isSpecialType = getIntent().getBooleanExtra("special", false);

        mChart = (LineChart) findViewById(R.id.chart1);


        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);
        mChart.setOnChartGestureListener(this);

        // no description text
        mChart.getDescription().setEnabled(false);
        //mChart.getDescription().setText("测点数据");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        //mChart.setBackgroundColor(Color.LTGRAY);

        mChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(LegendForm.LINE);
        //l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        XAxis xAxis = mChart.getXAxis();
        //xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        mChart.getAxisRight().setEnabled(false);


        loadDataForDay(CommonUtils.getCurrentDayStr());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditDayClicked();
            }
        });
    }

    void loadDataForDay(String dayStr) {
        loadingView.setVisibility(View.VISIBLE);
        placeHolderTextView.setVisibility(View.INVISIBLE);
        chartContainer.setVisibility(View.INVISIBLE);

        selectedDayStr = dayStr;

        DataMonitorService.getInstance().GetHistroySensorData(getIntent().getStringExtra("id"), dayStr, new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                GetHistroySensorDataModel model = (GetHistroySensorDataModel)responseModel;
                setDataFromModel(model);
            }

            @Override
            public void onFailed(String error) {

            }
        });

        textView.setText("日期选择：" + dayStr);
    }

    void setDataFromModel(GetHistroySensorDataModel model) {
        loadingView.setVisibility(View.INVISIBLE);
        if (model.getItems().size() == 0) {
            placeHolderTextView.setVisibility(View.VISIBLE);
            return;
        } else {
            chartContainer.setVisibility(View.VISIBLE);
        }

        final int color1 = ContextCompat.getColor(this, R.color.material_blue_500);
        final int color2 = ContextCompat.getColor(this, R.color.material_green_500);
        final int color3 = ContextCompat.getColor(this, R.color.material_brown_500);

        final int errorColor = ContextCompat.getColor(this, R.color.material_red_500);
        final int warnColor = ContextCompat.getColor(this, R.color.material_orange_500);

        LimitLine ll1 = new LimitLine(Float.valueOf(model.getEarlyWarningThreshold()), "预警值");
        ll1.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
        ll1.setLineColor(warnColor);
        ll1.setTextColor(warnColor);
        ll1.setLineWidth(2f);

        LimitLine ll2 = new LimitLine(Float.valueOf(model.getThreshold()), "报警值(" + model.getThreshold() + ")");
        ll2.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
        ll2.setLineColor(errorColor);
        ll2.setTextColor(errorColor);
        ll2.setLineWidth(2f);

        LimitLine ll3 = new LimitLine(-Float.valueOf(model.getEarlyWarningThreshold()), "预警值");
        ll3.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
        ll3.setLineColor(warnColor);
        ll3.setTextColor(warnColor);
        ll3.setLineWidth(2f);

        LimitLine ll4 = new LimitLine(-Float.valueOf(model.getThreshold()), "报警值(-" + model.getThreshold() + ")");
        ll4.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
        ll4.setLineColor(errorColor);
        ll4.setTextColor(errorColor);
        ll4.setLineWidth(2f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.addLimitLine(ll3);
        leftAxis.addLimitLine(ll4);

        if (isSpecialType) {
            List<Entry> yVals1 = new ArrayList<>();
            for (int i = model.getItems().size() - 1; i >= 0; i--) {
                GetHistroySensorDataModel.Item item = model.getItems().get(i);
                float yValue = Double.valueOf(item.getCol1()).floatValue();
                float xValue = Double.valueOf(CommonUtils.hoursInDay(item.getAddTime())).floatValue();
                yVals1.add(new Entry(xValue, yValue));
            }

            List<Entry> yVals2 = new ArrayList<>();
            for (int i = model.getItems().size() - 1; i >= 0; i--) {
                GetHistroySensorDataModel.Item item = model.getItems().get(i);
                float yValue = Double.valueOf(item.getCol2()).floatValue();
                float xValue = Double.valueOf(CommonUtils.hoursInDay(item.getAddTime())).floatValue();
                yVals2.add(new Entry(xValue, yValue));
            }

            List<Entry> yVals3 = new ArrayList<>();
            for (int i = model.getItems().size() - 1; i >= 0; i--) {
                GetHistroySensorDataModel.Item item = model.getItems().get(i);
                float yValue = Double.valueOf(item.getCol3()).floatValue();
                float xValue = Double.valueOf(CommonUtils.hoursInDay(item.getAddTime())).floatValue();
                yVals3.add(new Entry(xValue, yValue));
            }

            LineDataSet set1, set2, set3;

            if (mChart.getData() != null &&
                    mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
                set3 = (LineDataSet) mChart.getData().getDataSetByIndex(2);
                set1.setValues(yVals1);
                set2.setValues(yVals2);
                set3.setValues(yVals3);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                // create a dataset and give it a type
                set1 = new LineDataSet(yVals1, "X轴");

                set1.enableDashedLine(10f, 5f, 0f);
                set1.enableDashedHighlightLine(10f, 5f, 0f);
                set1.setColor(color1);
                set1.setCircleColor(color1);
                //set1.setLineWidth(1f);
                set1.setCircleRadius(4f);
                set1.setDrawCircleHole(false);
                //set1.setValueTextSize(14f);
                set1.setFormLineWidth(1f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set1.setFormSize(15.f);

                // create a dataset and give it a type
                set2 = new LineDataSet(yVals2, "Y轴");
                set2.enableDashedLine(10f, 5f, 0f);
                set2.enableDashedHighlightLine(10f, 5f, 0f);
                set2.setColor(color2);
                set2.setCircleColor(color2);
                //set2.setLineWidth(1f);
                set2.setCircleRadius(4f);
                //set2.setDrawCircleHole(false);
                //set2.setValueTextSize(9f);
                set2.setFormLineWidth(1f);
                set2.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set2.setFormSize(15.f);

                set3 = new LineDataSet(yVals3, "Z轴");
                set3.enableDashedLine(10f, 5f, 0f);
                set3.enableDashedHighlightLine(10f, 5f, 0f);
                set3.setColor(color3);
                set3.setCircleColor(color3);
                //set3.setLineWidth(1f);
                set3.setCircleRadius(4f);
                set3.setDrawCircleHole(false);
                //set3.setValueTextSize(9f);
                set3.setFormLineWidth(1f);
                set3.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set3.setFormSize(15.f);

                // create a data object with the datasets
                LineData data = new LineData(set1, set2, set3);
                data.setValueTextColor(Color.BLACK);
                data.setValueTextSize(9f);
                data.setDrawValues(false);

                // set data
                mChart.setData(data);
            }
        } else {
            List<Entry> yVals1 = new ArrayList<>();
            for (int i = model.getItems().size() - 1; i >= 0; i--) {
                GetHistroySensorDataModel.Item item = model.getItems().get(i);
                float yValue = Double.valueOf(item.getCol1()).floatValue();
                float xValue = Double.valueOf(CommonUtils.hoursInDay(item.getAddTime())).floatValue();
                yVals1.add(new Entry(xValue, yValue));
            }

            LineDataSet set1;

            if (mChart.getData() != null &&
                    mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                set1.setValues(yVals1);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                // create a dataset and give it a type
                set1 = new LineDataSet(yVals1, "X轴");

                set1.enableDashedLine(10f, 5f, 0f);
                set1.enableDashedHighlightLine(10f, 5f, 0f);
                set1.setColor(color1);
                set1.setCircleColor(color1);
                set1.setLineWidth(1f);
                set1.setCircleRadius(4f);
                set1.setDrawCircleHole(false);
                //set1.setValueTextSize(9f);
                set1.setFormLineWidth(1f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set1.setFormSize(15.f);

                // create a data object with the datasets
                LineData data = new LineData(set1);
                data.setValueTextColor(Color.BLACK);
                data.setValueTextSize(9f);
                data.setDrawValues(false);

                // set data
                mChart.setData(data);
            }
        }

        mChart.setVisibleXRangeMaximum(6);
        mChart.getAxisLeft().setAxisMinimum(1.3f * Math.min(mChart.getData().getYMin(), -Float.valueOf(model.getThreshold())));
        mChart.getAxisLeft().setAxisMaximum(1.3f * Math.max(mChart.getData().getYMax(), Float.valueOf(model.getThreshold())));
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)

        if (mChart.getScaleY() > 1.5) {
            mChart.getData().setDrawValues(true);
        } else {
            mChart.getData().setDrawValues(false);
        }
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY + "mChart.getScaleY()" + mChart.getScaleY());

        if (mChart.getScaleY() > 1.5) {
            mChart.getData().setDrawValues(true);
        } else {
            mChart.getData().setDrawValues(false);
        }

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleX() + ", high: " + mChart.getHighestVisibleX());
        Log.i("MIN MAX", "xmin: " + mChart.getXChartMin() + ", xmax: " + mChart.getXChartMax() + ", ymin: " + mChart.getYChartMin() + ", ymax: " + mChart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    void onEditDayClicked() {
        TimeSelectorDialog dialog = new TimeSelectorDialog(this);
        //设置标题
        dialog.setTimeTitle("选择时间:");
        //显示类型
        dialog.setIsShowtype(TimeConfig.YEAR_MONTH_DAY);
        //默认时间
        dialog.setCurrentDate(selectedDayStr);
        //隐藏清除按钮
        dialog.setEmptyIsShow(false);
        //设置起始时间
        dialog.setStartYear(2010);
        dialog.setDateListener(new DateListener() {
            @Override
            public void onReturnDate(String time,int year, int month, int day, int hour, int minute, int isShowType) {
                loadDataForDay(time);
            }
            @Override
            public void onReturnDate(String empty) {
            }
        });
        dialog.show();
    }
}