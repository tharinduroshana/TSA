package com.hextech.trainingsignalapp.ui.screeners;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.hextech.trainingsignalapp.R;
import com.hextech.trainingsignalapp.ScreenersListActivity;

public class ScreenersFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_screeners, container, false);

        Button topDayGainersButton = root.findViewById(R.id.topDayGainersButton);

        topDayGainersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScreenersListActivity.class);
                intent.putExtra("screener_type", "gainers");
                startActivity(intent);
            }
        });

        Button topDayLosersButton = root.findViewById(R.id.topDayLosersButton);

        topDayLosersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScreenersListActivity.class);
                intent.putExtra("screener_type", "losers");
                startActivity(intent);
            }
        });

        Button mostActivesButton = root.findViewById(R.id.mostActivesButton);

        mostActivesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScreenersListActivity.class);
                intent.putExtra("screener_type", "actives");
                startActivity(intent);
            }
        });

        return root;
    }
}