package com.hextech.trainingsignalapp.util;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkAdapter {

    public ScreenerCollection collection;
    public ArrayList<NewsItem> newsList;
    public QuoteItem quoteItem;
    public String[] autoCompleteArray;
    public ArrayList<Entry> dataArray;
    public CompanyProfile companyProfile;

    public void requestMovers(final NetworkCallback callback) {
        final OkHttpClient client = new OkHttpClient();

        collection = new ScreenerCollection();

        final Request request = createRequest(RequestPaths.MOVERS_PATH);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    String jsonString = response.body().string();
                    try {
                        JSONObject json = new JSONObject(jsonString);
                        JSONArray results = json.getJSONObject("finance").getJSONArray("result");
                        JSONArray topGainersJsonArray = results.getJSONObject(0).getJSONArray("quotes");
                        JSONArray topLosersJsonArray = results.getJSONObject(1).getJSONArray("quotes");
                        JSONArray mostActiveJsonArray = results.getJSONObject(2).getJSONArray("quotes");
                        ArrayList<ScreenerItem> gainersArrayList = convertScreenersJsonToArrayList(topGainersJsonArray);
                        ArrayList<ScreenerItem> losersArrayList = convertScreenersJsonToArrayList(topLosersJsonArray);
                        ArrayList<ScreenerItem> mostActiveArrayList = convertScreenersJsonToArrayList(mostActiveJsonArray);

                        collection.mostActivitiesList = mostActiveArrayList;
                        collection.topGainersList = gainersArrayList;
                        collection.topLosersList = losersArrayList;

                        callback.onResponse();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TSA", "Error retrieving data.");
                }
            }
        }).start();

    }

    public void requestNews(final NetworkCallback callback) {
        final OkHttpClient client = new OkHttpClient();

        collection = new ScreenerCollection();

        final Request request = createRequest(RequestPaths.NEWS_PATH);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    String jsonString = response.body().string();

                    JSONObject json = new JSONObject(jsonString);
                    JSONArray results = json.getJSONObject("items").getJSONArray("result");
                    newsList = test(results, NewsItem.class);

                    callback.onResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void requestQuoteInfo(String symbol, final NetworkCallback callback) {
        final OkHttpClient client = new OkHttpClient();

        final Request request = createRequest(RequestPaths.QUOTE_PATH + symbol);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    String jsonString = response.body().string();

                    JSONObject json = new JSONObject(jsonString);
                    JSONObject result = json.getJSONObject("quoteResponse").getJSONArray("result").getJSONObject(0);
                    quoteItem = convertJsonObjToObj(result, QuoteItem.class);

                    callback.onResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void requestAutoCompleteInfo(String text, final NetworkCallback callback) {
        final OkHttpClient client = new OkHttpClient();

        final Request request = createRequest(RequestPaths.AUTOCOMPLETE_PATH + text);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    String jsonString = response.body().string();
                    JSONArray quotesArray = new JSONObject(jsonString).getJSONArray("quotes");

                    autoCompleteArray = new String[quotesArray.length()];

                    for (int i = 0; i < quotesArray.length(); i++) {
                        String symbol = quotesArray.getJSONObject(i).getString("symbol");
                        autoCompleteArray[i] = symbol;
                    }

                    callback.onResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void requestChartInfo(String symbol, final NetworkCallback callback) {
        final OkHttpClient client = new OkHttpClient();

        final Request request = createRequest(RequestPaths.CHARTS_PATH + symbol + "&range=1mo");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    String jsonString = response.body().string();
                    JSONObject json = new JSONObject(jsonString);
                    JSONArray chartData = json.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("indicators").getJSONArray("quote").getJSONObject(0).getJSONArray("open");
                    JSONArray timestamps = json.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONArray("timestamp");
                    dataArray = new ArrayList<>();
                    for (int i = 0; i < chartData.length(); i++) {
                        //Date time=new Date((long)timestamps.get(i)*1000);
                        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        //String strDate = dateFormat.format(time);
                        //System.out.println(strDate);
                        dataArray.add(new Entry(i, Float.valueOf(String.valueOf(chartData.get(i)))));
                    }
                    callback.onResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void requestProfileInfo(String symbol, final NetworkCallback callback) {
        final OkHttpClient client = new OkHttpClient();

        final Request request = createRequest(RequestPaths.PROFILE_INFO_PATH + symbol);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    String jsonString = response.body().string();
                    JSONObject json = new JSONObject(jsonString);
                    companyProfile = convertJsonObjToObj(json, CompanyProfile.class);
                    callback.onResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Request createRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", Privacy.HOST)
                .addHeader("x-rapidapi-key", Privacy.API_KEY)
                .build();
        return request;
    }

    public ArrayList<ScreenerItem> convertScreenersJsonToArrayList(JSONArray array) {

        ArrayList<ScreenerItem> itemArrayList = new ArrayList<>();

        for(int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                Gson gson = new Gson();
                ScreenerItem item = gson.fromJson(obj.toString(), ScreenerItem.class);
                itemArrayList.add(item);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return itemArrayList;
    }

    public <T> ArrayList<T> test(JSONArray array, Class<T> cls) {
        ArrayList<T> itemArrayList = new ArrayList<>();

        for(int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                itemArrayList.add(convertJsonObjToObj(obj, cls));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return itemArrayList;
    }

    public <T> T convertJsonObjToObj(JSONObject object, Class<T> tClass) {
        Gson gson = new Gson();
        T item = gson.fromJson(object.toString(), tClass);
        return item;
    }

    public static void sendChatMessage(ChatMessage chatMessage) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chatroom");

        myRef.push().setValue(chatMessage);
    }

}
