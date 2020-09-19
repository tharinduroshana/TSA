package com.hextech.trainingsignalapp.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hextech.trainingsignalapp.NewsDetailActivity;
import com.hextech.trainingsignalapp.R;
import com.hextech.trainingsignalapp.util.GenericAdapter;
import com.hextech.trainingsignalapp.util.LoadingDialog;
import com.hextech.trainingsignalapp.util.NetworkAdapter;
import com.hextech.trainingsignalapp.util.NetworkCallback;
import com.hextech.trainingsignalapp.util.NewsItem;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    RecyclerView newsRecyclerView;
    GenericAdapter adapter;
    LoadingDialog loadingDialog;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_news, container, false);

        newsRecyclerView = root.findViewById(R.id.newsRecyclerView);

        final NetworkAdapter networkAdapter = new NetworkAdapter();
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoading("Fetching News...");

        networkAdapter.requestNews(new NetworkCallback() {
            @Override
            public void onResponse() {
                final ArrayList<NewsItem> newsItems = networkAdapter.newsList;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter = new GenericAdapter<NewsItem>(root.getContext(), newsItems) {
                            @Override
                            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                                View view = LayoutInflater.from(root.getContext()).inflate(R.layout.news_recyclerview_row, parent, false);
                                ViewHolder viewHolder = new ViewHolder(view);
                                return viewHolder;
                            }

                            @Override
                            public void onBindData(RecyclerView.ViewHolder holder, NewsItem item) {
                                ((ViewHolder)holder).titleTextView.setText(item.title);
                                String footerString = "";
                                if(item.author != null && !item.author.equals("")) {
                                    footerString = item.author + ", ";
                                }
                                footerString += item.publisher;
                                ((ViewHolder)holder).footerTextView.setText(footerString);
                            }
                        };

                        newsRecyclerView.setAdapter(adapter);
                        loadingDialog.stopLoading();
                    }
                });
            }
        });

        return root;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView, footerTextView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            footerTextView = itemView.findViewById(R.id.footerTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra("news_item", (NewsItem) adapter.getItem(getAdapterPosition()));
            startActivity(intent);
        }
    }
}