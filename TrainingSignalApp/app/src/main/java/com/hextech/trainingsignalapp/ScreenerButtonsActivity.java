package com.hextech.trainingsignalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class ScreenerButtonsActivity extends AppCompatActivity {

    Button topDayGainersButton, topDayLosersButton, topMostActivesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screener_buttons);
    }
}