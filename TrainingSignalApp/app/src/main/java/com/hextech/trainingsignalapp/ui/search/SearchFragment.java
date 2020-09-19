package com.hextech.trainingsignalapp.ui.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.hextech.trainingsignalapp.ChartActivity;
import com.hextech.trainingsignalapp.R;
import com.hextech.trainingsignalapp.util.NetworkAdapter;
import com.hextech.trainingsignalapp.util.NetworkCallback;

public class SearchFragment extends Fragment {

    AutoCompleteTextView searchTextView;
    String[] autoCompletes = new String[0];

    Button getChartButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_search, container, false);
        searchTextView = root.findViewById(R.id.searchTextView);
        getChartButton = root.findViewById(R.id.getChartButton);

        getChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChartActivity.class);
                intent.putExtra("symbol", searchTextView.getText().toString());
                startActivity(intent);
            }
        });

        searchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i("TSA", editable.toString());
                final NetworkAdapter networkAdapter = new NetworkAdapter();
                networkAdapter.requestAutoCompleteInfo(String.valueOf(editable.toString()), new NetworkCallback() {
                    @Override
                    public void onResponse() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                autoCompletes = networkAdapter.autoCompleteArray;
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, autoCompletes);
                                searchTextView.setAdapter(adapter);
                            }
                        });
                    }
                });
            }
        });
        return root;
    }
}