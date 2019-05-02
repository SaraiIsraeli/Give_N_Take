package com.example.saraiisraeli.give_n_take.models;

import java.util.HashMap;
import java.util.Map;

public class UserSettings
{
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
        HashMap<String, Object> result = new HashMap<>();
        result.put("userToken",userToken);
        result.put("distance",distance);
        result.put("Role",role);

        return result;
    }
}
