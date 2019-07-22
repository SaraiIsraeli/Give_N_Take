package com.example.saraiisraeli.give_n_take.models;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class UserSettings
{
    private static final String TAG = "";
    private String distance;
    private String userToken;
    private String prodQuery;

    public UserSettings() {}

    public UserSettings(String i_userToken,String i_distance, String i_prodQuery)
    {
        this.distance = i_distance;
        this.userToken = i_userToken;
        this.prodQuery = i_prodQuery;
    }

    public Map<String,Object> toMap()
    {
        Log.d(TAG, "Start Method: toMap");
        HashMap<String, Object> result = new HashMap<>();
        result.put("userToken",userToken);
        result.put("distance",distance);
        result.put("prodQuery",prodQuery);

        Log.d(TAG, "End Method: toMap");
        return result;
    }


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getProdQuery() {
        return prodQuery;
    }

    public void setProdQuery(String prodQuery) {
        this.prodQuery = prodQuery;
    }
}
