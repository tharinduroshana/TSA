package com.hextech.trainingsignalapp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hextech.trainingsignalapp.R;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoading(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View loadingView = inflater.inflate(R.layout.loading_dialog, null);
        builder.setView(loadingView);
        builder.setCancelable(false);

        ((TextView) loadingView.findViewById(R.id.loadingText)).setText(text);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void stopLoading() {
        alertDialog.dismiss();
    }

}
