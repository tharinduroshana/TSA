package com.hextech.trainingsignalapp.ui.tabs.profileinfo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hextech.trainingsignalapp.R;
import com.hextech.trainingsignalapp.util.CompanyProfile;
import com.hextech.trainingsignalapp.util.LoadingDialog;
import com.hextech.trainingsignalapp.util.NetworkAdapter;
import com.hextech.trainingsignalapp.util.NetworkCallback;

public class ProfileInfoFragment extends Fragment {

    TextView shortName, longNameTextView, symbolTextView, quoteTypeTextView, timeZoneTextView, marketTextView, zipTextView, sectorTextView, cityTextView, phoneTextView,
            stateTextView, countryTextView, websiteTextView, addressTextView, industryTextView, businessSummaryTextView;

    LoadingDialog loadingDialog;

    public ProfileInfoFragment() {
    }

    public static ProfileInfoFragment newInstance() {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoading("Fetching Data...");

        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_profile_info, container, false);

        initializeTextViews(root);

        String symbol = getActivity().getIntent().getStringExtra("symbol");
        final NetworkAdapter networkAdapter = new NetworkAdapter();
        networkAdapter.requestProfileInfo(symbol, new NetworkCallback() {
            @Override
            public void onResponse() {
                final CompanyProfile companyProfile = networkAdapter.companyProfile;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shortName.setText(companyProfile.quoteType.shortName);
                        longNameTextView.setText(companyProfile.quoteType.longName);
                        symbolTextView.setText(companyProfile.symbol);
                        quoteTypeTextView.setText(companyProfile.quoteType.quoteType);
                        timeZoneTextView.setText(companyProfile.quoteType.exchangeTimezoneName);
                        marketTextView.setText(companyProfile.quoteType.market);
                        zipTextView.setText(companyProfile.assetProfile.zip);
                        sectorTextView.setText(companyProfile.assetProfile.sector);
                        cityTextView.setText(companyProfile.assetProfile.city);
                        phoneTextView.setText(companyProfile.assetProfile.phone);
                        stateTextView.setText(companyProfile.assetProfile.state);
                        countryTextView.setText(companyProfile.assetProfile.country);
                        websiteTextView.setText(companyProfile.assetProfile.website);
                        addressTextView.setText(companyProfile.assetProfile.address1);
                        industryTextView.setText(companyProfile.assetProfile.industry);
                        businessSummaryTextView.setText(companyProfile.assetProfile.longBusinessSummary);

                        loadingDialog.stopLoading();
                    }
                });
            }
        });

        return root;
    }

    private void initializeTextViews(View root) {
        shortName = root.findViewById(R.id.shortNameTextView);
        longNameTextView = root.findViewById(R.id.longNameTextView);
        symbolTextView = root.findViewById(R.id.symbolTextView);
        quoteTypeTextView = root.findViewById(R.id.quoteTypeTextView);
        timeZoneTextView = root.findViewById(R.id.timeZoneTextView);
        marketTextView = root.findViewById(R.id.marketTextView);
        zipTextView = root.findViewById(R.id.zipTextView);
        sectorTextView = root.findViewById(R.id.sectorTextView);
        cityTextView = root.findViewById(R.id.cityTextView);
        phoneTextView = root.findViewById(R.id.phoneTextView);
        stateTextView = root.findViewById(R.id.stateTextView);
        countryTextView = root.findViewById(R.id.countryTextView);
        websiteTextView = root.findViewById(R.id.websiteTextView);
        addressTextView = root.findViewById(R.id.addressTextView);
        industryTextView = root.findViewById(R.id.industryTextView);
        businessSummaryTextView = root.findViewById(R.id.businessSummaryTextView);
    }

    private void populateFields(CompanyProfile profile) {

    }
}