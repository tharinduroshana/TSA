package com.hextech.trainingsignalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.hextech.trainingsignalapp.util.GenericAdapter;
import com.hextech.trainingsignalapp.util.LoadingDialog;
import com.hextech.trainingsignalapp.util.NetworkAdapter;
import com.hextech.trainingsignalapp.util.NetworkCallback;
import com.hextech.trainingsignalapp.util.ScreenerCollection;
import com.hextech.trainingsignalapp.util.ScreenerItem;

import java.util.ArrayList;

public class ScreenersListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ScreenerItem> screenerItems = new ArrayList<>();
    LoadingDialog loadingDialog;

    GenericAdapter genericAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screeners_list);

        final String type = getIntent().getStringExtra("screener_type");

        recyclerView = findViewById(R.id.screenersRecyclerView);

        loadingDialog = new LoadingDialog(ScreenersListActivity.this);
        loadingDialog.startLoading("Fetching Data...");

        final NetworkAdapter networkAdapter = new NetworkAdapter();

        networkAdapter.requestMovers(new NetworkCallback() {
            @Override
            public void onResponse() {
                ScreenerCollection collection = networkAdapter.collection;
                if (type != null) {
                    if (type.equals("gainers")) {
                        screenerItems = collection.topGainersList;
                    } else if (type.equals("losers")) {
                        screenerItems = collection.topLosersList;
                    } else {
                        screenerItems = collection.mostActivitiesList;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setLayoutManager(new LinearLayoutManager(ScreenersListActivity.this));
                            genericAdapter = new GenericAdapter<ScreenerItem>(getApplicationContext(), screenerItems) {
                                @Override
                                public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.screener_recyclerview_row, parent, false);
                                    ViewHolder viewHolder = new ViewHolder(view);
                                    return viewHolder;
                                }

                                @Override
                                public void onBindData(RecyclerView.ViewHolder holder, ScreenerItem item) {
                                    ((ViewHolder)holder).nameTextView.setText(item.symbol);
                                    ((ViewHolder)holder).marketTextView.setText(item.market);
                                    ((ViewHolder)holder).quoteTypeTextView.setText(item.quoteType);
                                    ((ViewHolder)holder).regionTextView.setText(item.region);
                                    if(type.equals("gainers")) {
                                        ((ViewHolder)holder).colorBar.setImageResource(R.drawable.gainers_drawable);
                                    } else if (type.equals("losers")) {
                                        ((ViewHolder)holder).colorBar.setImageResource(R.drawable.losers_drawable);
                                    } else {
                                        ((ViewHolder)holder).colorBar.setImageResource(R.drawable.normal_drawable);
                                    }
                                }
                            };

                            recyclerView.setAdapter(genericAdapter);
                            loadingDialog.stopLoading();
                        }
                    });
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView, quoteTypeTextView, marketTextView, regionTextView;
        ImageView colorBar;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            quoteTypeTextView = itemView.findViewById(R.id.quoteTypeTextView);
            marketTextView = itemView.findViewById(R.id.marketTextView);
            regionTextView = itemView.findViewById(R.id.regionTextView);
            colorBar = itemView.findViewById(R.id.colorBar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ScreenersListActivity.this, QuoteDetailActivity.class);
            intent.putExtra("symbol", ((ScreenerItem)genericAdapter.getItem(getAdapterPosition())).symbol);
            startActivity(intent);
        }
    }
}