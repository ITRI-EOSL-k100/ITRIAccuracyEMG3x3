package com.itri.accuracyemg3x3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class MainActivity extends AppCompatActivity {

    //activity_chart.xml
    LineChart lineChartAccuracy, lineChartRMS;
    Button btn1x3, btn3x3;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //viewBinding
        lineChartAccuracy = findViewById(R.id.linechart_accuracy);
        lineChartRMS = findViewById(R.id.linechart_rms);
        btn1x3 = findViewById(R.id.button_1x3);
        btn3x3 = findViewById(R.id.button_3x3);

        lineChartAccuracy.setNoDataText("No Data available");

//        lineChartAccuracy.getDescription().setEnabled(true);
//        lineChartAccuracy.getDescription().setText("Real Time EMG Accuracy Plot  (%) ");
        lineChartAccuracy.setTouchEnabled(false); // 設定是否可以觸控
        lineChartAccuracy.setDragEnabled(true); // 是否可以拖拽
        lineChartAccuracy.setScaleEnabled(false); // 是否可以縮放 x和y軸, 預設是true
        lineChartAccuracy.setDrawGridBackground(true); // 背景網格
        lineChartAccuracy.setPinchZoom(false); //設定x軸和y軸能否同時縮放。預設是否
        lineChartAccuracy.setBackgroundColor(Color.WHITE);

        LineData lineData = new  LineData();
        lineData.setValueTextColor(Color.BLACK);
        lineChartAccuracy.setData(lineData);

        Legend legendAccuracy = lineChartAccuracy.getLegend();
        legendAccuracy.setForm(Legend.LegendForm.CIRCLE);
        legendAccuracy.setTextColor(Color.BLUE);

        XAxis xAxis = lineChartAccuracy.getXAxis();
        xAxis.setTextColor(Color.BLACK);
        xAxis.setAvoidFirstLastClipping(true);

        YAxis yAxis = lineChartAccuracy.getAxisLeft();
        yAxis.setTextColor(Color.BLACK);
        yAxis.setDrawGridLines(true);
//        yAxis.setAxisMinimum(0f);
//        yAxis.setAxisMaximum(100f);

        lineChartAccuracy.setDrawBorders(true);
        lineChartAccuracy.getXAxis().setDrawGridLines(true);
//        chartAccuracyStartPlot();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run () {
//                            addAccuracyEntry();
                            addEntry();
                        }
                    });
                    try {
                        Thread.sleep(100);
                    }catch (InterruptedException e){

                    }

                }
            }
        }).start();
    }

    private void addEntry(){
        LineData data = lineChartAccuracy.getData();
        if (data != null){
            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
            if (set == null){
                set = createSet();
                data.addDataSet(set);
            }
            data.addXValue("");
//            data.addEntry(
//                    new Entry((float) (Math.random()*120) + 5f, set.getEntryCount()),0);
            int random = 100 -(int)(Math.random()*100+1);
            data.addEntry(
                    new Entry(random , set.getEntryCount()), 0);
            lineChartAccuracy.notifyDataSetChanged();
            lineChartAccuracy.setVisibleXRange(100,100);
            lineChartAccuracy.moveViewToX(data.getXValCount()-99);
        }

    }

    private LineDataSet createSet() {
        LineDataSet set  = new LineDataSet(null,"Dyanmic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.MAGENTA);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(1f);
        return set;
    }
}
