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

        lineChartAccuracySetting();
        lineChartRMSSetting();

        LineData lineDataAccuracy = new  LineData();
        lineDataAccuracy.setValueTextColor(Color.BLACK);
        lineChartAccuracy.setData(lineDataAccuracy);

        LineData lineDataRMS = new  LineData();
        lineDataRMS.setValueTextColor(Color.BLACK);
        lineChartRMS.setData(lineDataRMS);





        Legend legendAccuracy = lineChartAccuracy.getLegend();
        legendAccuracy.setForm(Legend.LegendForm.CIRCLE);
        legendAccuracy.setTextColor(Color.BLUE);

        Legend legendRMS = lineChartRMS.getLegend();
        legendRMS.setForm(Legend.LegendForm.LINE);
        legendRMS.setTextColor(Color.BLACK);

        XAxis xAxisAccuracy = lineChartAccuracy.getXAxis();
        xAxisAccuracy.setTextColor(Color.BLACK);
        xAxisAccuracy.setAvoidFirstLastClipping(true);

        XAxis xAxisRMS = lineChartRMS.getXAxis();
        xAxisRMS.setTextColor(Color.BLUE);
        xAxisRMS.setAvoidFirstLastClipping(true);

        YAxis yAxisAccuracy = lineChartAccuracy.getAxisLeft();
        yAxisAccuracy.setTextColor(Color.BLACK);
        yAxisAccuracy.setDrawGridLines(true);
        yAxisAccuracy.setAxisMinimum(0f);
        yAxisAccuracy.setAxisMaximum(100f);

        YAxis yAxisRMS = lineChartRMS.getAxisLeft();
        yAxisRMS.setTextColor(Color.BLUE);
        yAxisRMS.setDrawGridLines(true);
        yAxisRMS.setAxisMinimum(0f);
        yAxisRMS.setAxisMaximum(100f);

        YAxis leftAxisAccuracy = lineChartAccuracy.getAxisRight();
        leftAxisAccuracy.setEnabled(false);

        YAxis leftAxisRMS = lineChartRMS.getAxisRight();
        leftAxisRMS.setEnabled(false);

        lineChartAccuracy.setDrawBorders(true);
        lineChartAccuracy.getXAxis().setDrawGridLines(true);

        lineChartRMS.setDrawBorders(true);
        lineChartRMS.getXAxis().setDrawGridLines(true);
    }



    private void lineChartAccuracySetting() {
        lineChartAccuracy.setNoDataText("No Data available");
        lineChartAccuracy.getDescription().setEnabled(true);
        lineChartAccuracy.getDescription().setText("Real Time EMG Accuracy Plot  (%) ");
//        lineChartRMS.setDescription("Real Time EMG Accuracy Plot  (%) ");//2.1.5
        lineChartAccuracy.setTouchEnabled(false); // 設定是否可以觸控
        lineChartAccuracy.setDragEnabled(true); // 是否可以拖拽
        lineChartAccuracy.setScaleEnabled(false); // 是否可以縮放 x和y軸, 預設是true
        lineChartAccuracy.setDrawGridBackground(true); // 背景網格
        lineChartAccuracy.setPinchZoom(false); //設定x軸和y軸能否同時縮放。預設是否
        lineChartAccuracy.setBackgroundColor(Color.WHITE);
    }

    private void lineChartRMSSetting() {
        lineChartRMS.setNoDataText("No Data available");
        lineChartRMS.getDescription().setEnabled(true);
        lineChartRMS.getDescription().setText("Real Time RMS data  ");
//        lineChartRMS.setDescription("Real Time EMG Accuracy Plot  (%) ");//2.1.5
        lineChartRMS.setTouchEnabled(false); // 設定是否可以觸控
        lineChartRMS.setDragEnabled(true); // 是否可以拖拽
        lineChartRMS.setScaleEnabled(false); // 是否可以縮放 x和y軸, 預設是true
        lineChartRMS.setDrawGridBackground(true); // 背景網格
        lineChartRMS.setPinchZoom(false); //設定x軸和y軸能否同時縮放。預設是否
        lineChartRMS.setBackgroundColor(Color.WHITE);
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
                            addAccuracyEntry();
                            addRMSEntry();
                        }
                    });
                    try {
                        Thread.sleep(50);
                    }catch (InterruptedException e){

                    }

                }
            }
        }).start();
    }


    private void addAccuracyEntry(){
        LineData dataAccuracy = lineChartAccuracy.getData();
        if (dataAccuracy != null){
            LineDataSet setAccuracy = (LineDataSet) dataAccuracy.getDataSetByIndex(0);
            if (setAccuracy == null){
                setAccuracy = createSetAccuracy();
                dataAccuracy.addDataSet(setAccuracy);
            }
            int random = 100 - (int)(Math.random()*100+1);
            dataAccuracy.addEntry(new Entry(setAccuracy.getEntryCount(), random), 0);
            lineChartAccuracy.notifyDataSetChanged();

            dataAccuracy.notifyDataChanged();
            lineChartAccuracy.setVisibleXRange(100,100);
            lineChartAccuracy.setMaxVisibleValueCount(150);
            lineChartAccuracy.moveViewToX(dataAccuracy.getEntryCount());
//            moveViewTo(float xIndex, float yValue, AxisDependency axis): 这将移动当前视口的左侧（边缘）到指定的x索引在x轴上，并且中心视口提供的y轴上指定的y值
//                                                          （相当于结合了setVisibleXRange(...)和setVisibleYRange(...)）
        }

    }

    private void addRMSEntry() {
        LineData dataRMS = lineChartRMS.getData();
        if (dataRMS != null){
            LineDataSet setRMS = (LineDataSet) dataRMS.getDataSetByIndex(0);
            if (setRMS == null){
                setRMS = createSetRMS();
                dataRMS.addDataSet(setRMS);
            }
            int random = (int)(Math.random()*100+1);
            dataRMS.addEntry(new Entry(setRMS.getEntryCount(), random), 0);
            lineChartRMS.notifyDataSetChanged();

            dataRMS.notifyDataChanged();
            lineChartRMS.setVisibleXRange(100,100);
            lineChartRMS.setMaxVisibleValueCount(150);
            lineChartRMS.moveViewToX(dataRMS.getEntryCount());
    }
}

    private LineDataSet createSetAccuracy() {
        LineDataSet set  = new LineDataSet(null,"EMG Accuracy Data");
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

    private LineDataSet createSetRMS() {
        LineDataSet set  = new LineDataSet(null,"RMS Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.BLACK);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(1f);
        return set;
    }
}
