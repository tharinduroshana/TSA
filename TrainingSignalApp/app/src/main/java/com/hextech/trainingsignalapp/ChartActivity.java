package com.hextech.trainingsignalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
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
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(dataSet);

                        LineData data = new LineData(dataSets);
                        chart.setData(data);
                        chart.getDescription().setText("USD");
                        chart.invalidate();
                        loadingDialog.stopLoading();
                    }
                });
            }
        });
    }
}