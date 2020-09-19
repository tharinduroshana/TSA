package com.hextech.trainingsignalapp.util;

public class RequestPaths {
    static final String MOVERS_PATH = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-movers?region=US&lang=en";
    static final String NEWS_PATH = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/get-news?region=US&category=NBEV";
    static final String QUOTE_PATH = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-quotes?region=US&lang=en&symbols=";
    static final String AUTOCOMPLETE_PATH = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/auto-complete?region=US&q=";
    static final String CHARTS_PATH = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v2/get-chart?region=US&interval=1d&symbol=";
}
