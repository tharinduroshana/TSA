package com.hextech.trainingsignalapp.ui.tabs.charts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hextech.trainingsignalapp.ChartActivity;
import com.hextech.trainingsignalapp.R;
import com.hextech.trainingsignalapp.util.LoadingDialog;
import com.hextech.trainingsignalapp.util.NetworkAdapter;
import com.hextech.trainingsignalapp.util.NetworkCallback;

import java.util.ArrayList;

public class ChartsFragment extends Fragment {

    LineChart chart;
    LoadingDialog loadingDialog;
    TextView symbolTextView;

    public ChartsFragment() {
        // Required empty public constructor
    }

    public static ChartsFragment newInstance() {
        ChartsFragment fragment = new ChartsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_charts, container, false);

        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoading("Fetching Data...");

        String symbol = getActivity().getIntent().getStringExtra("symbol");

        chart = root.findViewById(R.id.chart);
        symbolTextView = root.findViewById(R.id.symbolTextView);

        symbolTextView.setText(symbol + " Stocks Chart");

        if (symbol != null || !symbol.equals("")) {
            getValues(symbol);
        }

        return root;
    }

    private void getValues(String symbol) {

        final NetworkAdapter networkAdapter = new NetworkAdapter();
        networkAdapter.requestChartInfo(symbol, new NetworkCallback() {
            @Override
            public void onResponse() {
                getActivity().runOnUiThread(new Runnable() {
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