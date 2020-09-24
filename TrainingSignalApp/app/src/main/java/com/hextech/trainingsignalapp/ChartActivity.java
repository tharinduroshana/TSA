package com.hextech.trainingsignalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hextech.trainingsignalapp.util.LoadingDialog;
import com.hextech.trainingsignalapp.util.NetworkAdapter;
import com.hextech.trainingsignalapp.util.NetworkCallback;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    LineChart chart;
    LoadingDialog loadingDialog;
    TextView symbolTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        loadingDialog = new LoadingDialog(ChartActivity.this);
        loadingDialog.startLoading("Fetching Data...");

        String symbol = getIntent().getStringExtra("symbol");

        chart = findViewById(R.id.chart);
        symbolTextView = findViewById(R.id.symbolTextView);

        symbolTextView.setText(symbol + " Stocks Chart");

        if (symbol != null || !symbol.equals("")) {
            getValues(symbol);
        }


    }

    private void getValues(String symbol) {

        final NetworkAdapter networkAdapter = new NetworkAdapter();
        networkAdapter.requestChartInfo(symbol, new NetworkCallback() {
            @Override
            public void onResponse() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LineDataSet dataSet = new LineDataSet(networkAdapter.dataArray, "Stocks");

                        dataSet.setLineWidth(1.7f);
                        dataSet.setCircleRadius(5.5f);
                        dataSet.setCircleHoleRadius(3.5f);
                        dataSet.setColor(Color.parseColor("#D503DAC5"));
                        dataSet.setCircleColor(Color.parseColor("#D503DAC5"));
                        dataSet.setValueTextColor(Color.parseColor("#FFFFFF"));
                        dataSet.setValueTextSize(10f);

                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(dataSet);

                        LineData data = new LineData(dataSets);
                        chart.setData(data);
                        chart.getDescription().setText("USD");
                        chart.getDescription().setTextColor(Color.parseColor("#FFFFFF"));
                        chart.getAxisLeft().setTextColor(Color.parseColor("#D503DAC5"));
                        chart.getAxisRight().setTextColor(Color.parseColor("#D503DAC5"));// left y-axis
                        chart.getXAxis().setTextColor(Color.parseColor("#D503DAC5"));
                        chart.getLegend().setTextColor(Color.parseColor("#FFFFFF"));
                        chart.getXAxis().setTextSize(10);
                        chart.getAxisLeft().setTextSize(14);
                        chart.getAxisRight().setTextSize(14);
                        chart.getLegend().setTextSize(14);
                        chart.getDescription().setTextSize(14);
                        chart.invalidate();
                        loadingDialog.stopLoading();
                    }
                });
            }
        });
    }
}

//    private LineDataSet createSet() {
//
//        LineDataSet set = new LineDataSet(null, "DataSet 1");
//        set.setLineWidth(2.5f);
//        set.setCircleRadius(6.5f);
//        set.setCircleHoleRadius(3.5f);
//        set.setColor(Color.parseColor("#ffffff"));
//        set.setCircleColor(Color.parseColor("#ffffff"));
//        set.setCircleColorHole(Color.parseColor("#039BE5"));
//        set.setHighLightColor(Color.rgb(190, 190, 190));
//        set.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set.setValueTextColor(Color.parseColor("#ffffff"));
//        set.setValueTextSize(10f);
//
//        return set;
//    }`