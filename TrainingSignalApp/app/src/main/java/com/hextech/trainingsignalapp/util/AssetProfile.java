package com.hextech.trainingsignalapp.util;

import java.io.Serializable;
import java.util.ArrayList;

public class AssetProfile implements Serializable {
    public String zip, sector, longBusinessSummary, city, phone, state, country, website, address1, industry;
    public ArrayList<CompanyOfficer> companyOfficers;
}