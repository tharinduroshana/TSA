package com.hextech.trainingsignalapp.ui.watchlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hextech.trainingsignalapp.R;
import com.hextech.trainingsignalapp.WatchListInfoActivity;
import com.hextech.trainingsignalapp.util.DBHelper;
import com.hextech.trainingsignalapp.util.NetworkAdapter;
import com.hextech.trainingsignalapp.util.NetworkCallback;

import java.util.ArrayList;

public class WatchListFragment extends Fragment {

    ListView watchList;
    FloatingActionButton addWatchItem;
    ArrayList<String> records;

    String[] autoCompletes = new String[0];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_watch_list, container, false);
        watchList = root.findViewById(R.id.watchList);
        addWatchItem = root.findViewById(R.id.addWatchItem);

        loadRecords();
        setOnClickListener();

        addWatchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                final AutoCompleteTextView symbolSearchTextView = new AutoCompleteTextView(getActivity());
                symbolSearchTextView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        final NetworkAdapter networkAdapter = new NetworkAdapter();
                        networkAdapter.requestAutoCompleteInfo(String.valueOf(editable.toString()), new NetworkCallback() {
                            @Override
                            public void onResponse() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        autoCompletes = networkAdapter.autoCompleteArray;
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, autoCompletes);
                                        symbolSearchTextView.setAdapter(adapter);
                                    }
                                });
                            }
                        });
                    }
                });
                symbolSearchTextView.setHint("Symbol");
                builder1.setTitle("Search Symbol");
                builder1.setCancelable(true);
                builder1.setView(symbolSearchTextView);
                builder1.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String symbol = symbolSearchTextView.getText().toString();
                        boolean status = DBHelper.insertEntry(getContext().getApplicationContext(), symbol);

                        if (status) {
                            Toast.makeText(getActivity(), "WatchList Item Added!", Toast.LENGTH_SHORT).show();
                            loadRecords();
                        } else {
                            Toast.makeText(getActivity(), "WatchList Item Adding Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        return root;
    }

    private void loadRecords() {
        records = DBHelper.getAllRecords(getContext().getApplicationContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, records);
        watchList.setAdapter(adapter);
    }

    private void setOnClickListener() {
        watchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), WatchListInfoActivity.class);
                intent.putExtra("symbol", records.get(i));
                startActivity(intent);
            }
        });
    }
}