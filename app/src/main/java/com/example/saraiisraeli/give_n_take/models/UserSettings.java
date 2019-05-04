package com.example.saraiisraeli.give_n_take.models;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class UserSettings
{
    private static final String TAG = "";
    private int distance;
    private String userToken;
    private int  role; // 1 = seller , 0 = buyer , 2 = both

    public UserSettings() {}

    public UserSettings(String i_userToken,int i_distance, int i_Role)
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
}
