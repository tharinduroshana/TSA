package com.hextech.trainingsignalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.hextech.trainingsignalapp.util.LoadingDialog;
import com.hextech.trainingsignalapp.util.NetworkAdapter;
import com.hextech.trainingsignalapp.util.NetworkCallback;
import com.hextech.trainingsignalapp.util.QuoteItem;

public class QuoteDetailActivity extends AppCompatActivity {

    TextView symbolTextView, shortNameTextView, regionTextView, currencyTextView, exchangeTimeZoneTextView, exchangeTextView, fullExchangeNameTextView,
            marketStateTextView, priceToBookTextView, forwardPETextView, marketCapTextView, fiftyDayAverageTextView, bookValueTextView,
            sharesOutstandingTextView, priceEpsCurrentTextView, priceEpsNextQuarterTextView, priceToSalesTextView, revenueTextView,
            regularMarketOpenTextView, regularMarketPriceTextView, postMarketPriceTextView;

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_detail);

        loadingDialog = new LoadingDialog(QuoteDetailActivity.this);
        loadingDialog.startLoading("Fetching Data...");

        String symbol = getIntent().getStringExtra("symbol");
        initTextViews();

        final NetworkAdapter networkAdapter = new NetworkAdapter();
        networkAdapter.requestQuoteInfo(symbol, new NetworkCallback() {
            @Override
            public void onResponse() {
                final QuoteItem item = networkAdapter.quoteItem;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        populateTextViews(item);
                        loadingDialog.stopLoading();
                    }
                });

            }
        });

    }

    private void populateTextViews(QuoteItem quoteItem) {
        String symbol = quoteItem.symbol;
        if (!isNullOrEmpty(symbol)) {
            symbolTextView.setText(symbol);
        } else {
            setTextViewToEmpty(symbolTextView);
        }

        String shortName = quoteItem.shortName;
        if (!isNullOrEmpty(shortName)) {
            shortNameTextView.setText(shortName);
        } else {
            setTextViewToEmpty(shortNameTextView);
        }

        String region = quoteItem.region;
        if (!isNullOrEmpty(region)) {
            regionTextView.setText(region);
        } else {
            setTextViewToEmpty(regionTextView);
        }

        String currency = quoteItem.currency;
        if (!isNullOrEmpty(currency)) {
            currencyTextView.setText(currency);
        } else {
            setTextViewToEmpty(currencyTextView);
        }

        String exchangeTimeZone = quoteItem.exchangeTimezoneName;
        if (!isNullOrEmpty(exchangeTimeZone)) {
            exchangeTimeZoneTextView.setText(exchangeTimeZone);
        } else {
            setTextViewToEmpty(exchangeTimeZoneTextView);
        }

        String exchange = quoteItem.exchange;
        if (!isNullOrEmpty(exchange)) {
            exchangeTextView.setText(currency + " " + exchange);
        } else {
            setTextViewToEmpty(exchangeTextView);
        }

        String fullExchangeName = quoteItem.fullExchangeName;
        if (!isNullOrEmpty(fullExchangeName)) {
            fullExchangeNameTextView.setText(fullExchangeName);
        } else {
            setTextViewToEmpty(fullExchangeNameTextView);
        }

        String marketState = quoteItem.marketState;
        if (!isNullOrEmpty(marketState)) {
            marketStateTextView.setText(marketState);
        } else {
            setTextViewToEmpty(marketStateTextView);
        }

        String priceToBook = quoteItem.priceToBook;
        if (!isNullOrEmpty(priceToBook)) {
            priceToBookTextView.setText( currency + " " + priceToBook);
        } else {
            setTextViewToEmpty(priceToBookTextView);
        }

        String forwardPE = quoteItem.forwardPE;
        if (!isNullOrEmpty(forwardPE)) {
            forwardPETextView.setText(forwardPE);
        } else {
            setTextViewToEmpty(forwardPETextView);
        }

        String marketCap = quoteItem.marketCap;
        if (!isNullOrEmpty(marketCap)) {
            marketCapTextView.setText(marketCap);
        } else {
            setTextViewToEmpty(marketCapTextView);
        }

        String fiftyDayAvg = quoteItem.fiftyDayAverage;
        if (!isNullOrEmpty(fiftyDayAvg)) {
            fiftyDayAverageTextView.setText(fiftyDayAvg);
        } else {
            setTextViewToEmpty(fiftyDayAverageTextView);
        }

        String bookValue = quoteItem.bookValue;
        if (!isNullOrEmpty(bookValue)) {
            bookValueTextView.setText(bookValue);
        } else {
            setTextViewToEmpty(bookValueTextView);
        }

        String sharesOutstanding = quoteItem.sharesOutstanding;
        if (!isNullOrEmpty(sharesOutstanding)) {
            sharesOutstandingTextView.setText(sharesOutstanding);
        } else {
            setTextViewToEmpty(sharesOutstandingTextView);
        }

        String priceEpsCurrent = quoteItem.priceEpsCurrentYear;
        if (!isNullOrEmpty(priceEpsCurrent)) {
            priceEpsCurrentTextView.setText(currency + " " + priceEpsCurrent);
        } else {
            setTextViewToEmpty(priceEpsCurrentTextView);
        }

        String priceEpsNxtQuarter = quoteItem.priceEpsNextQuarter;
        if (!isNullOrEmpty(priceEpsNxtQuarter)) {
            priceEpsNextQuarterTextView.setText(priceEpsNxtQuarter);
        } else {
            setTextViewToEmpty(priceEpsNextQuarterTextView);
        }

        String priceToSale = quoteItem.priceToSales;
        if (!isNullOrEmpty(priceToSale)) {
            priceToSalesTextView.setText(currency + " " + priceToSale);
        } else {
            setTextViewToEmpty(priceToSalesTextView);
        }

        String revenue = quoteItem.revenue;
        if (!isNullOrEmpty(revenue)) {
            revenueTextView.setText(currency + " " + revenue);
        } else {
            setTextViewToEmpty(revenueTextView);
        }

        String regularMarketOpen = quoteItem.regularMarketOpen;
        if (!isNullOrEmpty(regularMarketOpen)) {
            regularMarketOpenTextView.setText(regularMarketOpen);
        } else {
            setTextViewToEmpty(regularMarketOpenTextView);
        }

        String regularMarketPrice = quoteItem.regularMarketPrice;
        if (!isNullOrEmpty(regularMarketPrice)) {
            regularMarketPriceTextView.setText(currency + " " + regularMarketPrice);
        } else {
            setTextViewToEmpty(regularMarketPriceTextView);
        }

        String postMarketPrice = quoteItem.postMarketPrice;
        if (!isNullOrEmpty(postMarketPrice)) {
            postMarketPriceTextView.setText(currency + " " + postMarketPrice);
        } else {
            setTextViewToEmpty(postMarketPriceTextView);
        }
    }

    private boolean isNullOrEmpty(String text) {
        return text == null || text.equals("");
    }

    private void setTextViewToEmpty(TextView textView) {
        textView.setText("-");
    }

    private void initTextViews() {
        symbolTextView = findViewById(R.id.symbolTextView);
        shortNameTextView = findViewById(R.id.shortNameTextView);
        regionTextView = findViewById(R.id.regionTextView);
        currencyTextView = findViewById(R.id.currencyTextView);
        exchangeTimeZoneTextView = findViewById(R.id.exchangeTimeZoneTextView);
        exchangeTextView = findViewById(R.id.exchangeTextView);
        fullExchangeNameTextView = findViewById(R.id.fullExchangeNameTextView);
        marketStateTextView = findViewById(R.id.marketStateTextView);
        priceToBookTextView = findViewById(R.id.priceToBookTextView);
        forwardPETextView = findViewById(R.id.forwardPETextView);
        marketCapTextView = findViewById(R.id.marketCapTextView);
        fiftyDayAverageTextView = findViewById(R.id.fiftyDayAverageTextView);
        bookValueTextView = findViewById(R.id.bookValueTextView);
        sharesOutstandingTextView = findViewById(R.id.sharesOutstandingTextView);
        priceEpsCurrentTextView = findViewById(R.id.priceEpsCurrentTextView);
        priceEpsNextQuarterTextView = findViewById(R.id.priceEpsNextQuarterTextView);
        priceToSalesTextView = findViewById(R.id.priceToSalesTextView);
        revenueTextView = findViewById(R.id.revenueTextView);
        regularMarketOpenTextView = findViewById(R.id.regularMarketOpenTextView);
        regularMarketPriceTextView = findViewById(R.id.regularMarketPriceTextView);
        postMarketPriceTextView = findViewById(R.id.postMarketPriceTextView);
    }
}