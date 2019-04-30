package com.example.saraiisraeli.give_n_take.models;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class AppData
{
    private static final String TAG = "";
    private DatabaseReference mDatabase;
    private FirebaseUser mAuth;

    public AppData()
    {
        try
        {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void SaveDistanceSettings(Map<String, Object> SettingsValues)
    {
        Log.d(TAG, "Start Method: SaveDistanceSettings");
        String userToken = (String) SettingsValues.get("userToken");
        int  distance  = (int) SettingsValues.get("distance");
        int role  = (int) SettingsValues.get("Role");
        if((!userToken.isEmpty()) && (distance > 0)  && (role <= 2))
        { 
            try
            {
                mDatabase.child("userSettings").setValue(userToken);
                mDatabase.child("userSettings").child(userToken).child("distance").setValue(distance);
                mDatabase.child("userSettings").child(userToken).child("Role").setValue(role);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        Log.d(TAG, "End Method: SaveDistanceSettings");
    }
    public FirebaseUser getCurrentUser()
    {
        Log.d(TAG, "Start Method: getCurrentUser");
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (mAuth != null)
        {
            return mAuth;
        }
        return null;
    }
}



