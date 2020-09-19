package com.hextech.trainingsignalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hextech.trainingsignalapp.util.NetworkCallback;
import com.hextech.trainingsignalapp.util.NewsItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NewsDetailActivity extends AppCompatActivity {

    TextView titleTextView, summaryTextView, publisherTextView;
    ImageView imageView;
    static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        NewsItem newsItem = (NewsItem) getIntent().getSerializableExtra("news_item");

        titleTextView = findViewById(R.id.titleTextView);
        summaryTextView = findViewById(R.id.summaryTextView);
        publisherTextView = findViewById(R.id.publisherTextView);

        imageView = findViewById(R.id.imageView);

        if (newsItem != null) {
            titleTextView.setText(newsItem.title);
            summaryTextView.setText(newsItem.summary);
            String publisherText = "";
            if (newsItem.author != null && !newsItem.author.equals("")) {
                publisherText += newsItem.author + ", ";
            }
            publisherText += newsItem.publisher;
            publisherTextView.setText(publisherText);
            if (newsItem.main_image != null && newsItem.main_image.original_url != null && !newsItem.main_image.original_url.equals("")) {
                getBitmapFromURL(newsItem.main_image.original_url, new NetworkCallback() {
                    @Override
                    public void onResponse() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }
                });

            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }

    public static void getBitmapFromURL(final String src, final NetworkCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(src);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(input);
                    callback.onResponse();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }
}