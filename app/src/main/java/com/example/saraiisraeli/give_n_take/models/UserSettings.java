package com.example.saraiisraeli.give_n_take.models;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class UserSettings
{
    private static final String TAG = "";
    private String distance;
    private String userToken;
    private String  role; // 1 = seller , 0 = buyer , 2 = both

    public UserSettings() {}

    public UserSettings(String i_userToken,String i_distance, String i_Role)
    {
        this.distance = i_distance;
        this.userToken = i_userToken;
        this.role = i_Role;
    }

    public Map<String,Object> toMap()
    {
        Log.d(TAG, "Start Method: toMap");
        HashMap<String, Object> result = new HashMap<>();
        result.put("userToken",userToken);
        result.put("distance",distance);
        result.put("Role",role);

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
