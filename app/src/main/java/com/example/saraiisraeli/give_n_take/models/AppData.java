package com.example.saraiisraeli.give_n_take.models;


import android.util.Log;

import com.example.saraiisraeli.give_n_take.activity.Search;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AppData {
    private DatabaseReference mDatabase;
    private FirebaseUser mAuth;
    private static final String TAG = "appData";
    private String Username = null;

    public AppData() {
        try {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setDistanceSettings(Map<String, Object> SettingsValues) {
        Log.d(TAG, "Start Method: SaveDistanceSettings");
        String userToken = (String) SettingsValues.get("userToken");
        String distance = SettingsValues.get("distance").toString();
        String productName = SettingsValues.get("prodQuery").toString();
        if ((!userToken.isEmpty()) && (Integer.valueOf(distance) > 0) ) {
            try {
                mDatabase.child("userSettings").child(userToken).child("distance").setValue(distance);
                if(!productName.isEmpty())
                mDatabase.child("userSettings").child(userToken).child("prodQuery").setValue(productName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        Log.d(TAG, "End Method: SaveDistanceSettings");
    }

    public void getDistanceSettings(final String userToken, final Search mSearch) {
        final Map<String, Object> settingsValues = new HashMap<>();
        DatabaseReference settingsRef = FirebaseDatabase.getInstance().getReference().child("userSettings").child(userToken);
        if (settingsRef == null) {
            Log.i(TAG, "no settings for:" + userToken);
        } else {
            Log.i(TAG, "user settings for:" + userToken);
            settingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> settings = (Map<String, Object>) dataSnapshot.getValue();
                    Log.i(TAG, "dataSnapshot:" + dataSnapshot.getValue());
                    if (settings != null)
                    {
                        settingsValues.put("distance", settings.get("distance").toString());
                        if(settings.get("prodQuery") == null)
                        {
                            settingsValues.put("prodQuery", "");
                        }
                        else
                        {
                            settingsValues.put("prodQuery", Objects.requireNonNull(settings.get("prodQuery")).toString());
                        }

                        //Log.i(TAG, "settings values appdata: " + settingsValues.get("prodQuery").toString() + " ," + settingsValues.get("distance").toString());
                        mSearch.setDataFromDB(settingsValues);
                    } else {
                        Log.i(TAG, "settings is null! ");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }
    }

    public FirebaseUser getCurrentUser() {
        Log.d(TAG, "Start Method: getCurrentUser");
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (mAuth != null) {
            Log.i(TAG, "CurrentUser = " + mAuth.getUid());
            return mAuth;
        }
        return null;
    }

    public void SavetNewItem(Map<String, Object> ItemValues, String i_userToken) {
        Log.d(TAG, "Start Method: SaveNewItem");
        String itemName = (String) ItemValues.get("itemName");
        String userToken = i_userToken;
        String itemLocation = ItemValues.get("itemLocation").toString();
        String itemDescription = ItemValues.get("itemDescription").toString();
            try {
                mDatabase.child("items").child(userToken).child("itemName").setValue(itemName);
                mDatabase.child("items").child(userToken).child("itemLocation").setValue(itemLocation);
                mDatabase.child("items").child(userToken).child("itemDescription").setValue(itemDescription);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        Log.d(TAG, "End Method: SaveNewItem");
    }
/*
    public String getCurrentUserName(final String userToken) {
        //final Map<String, Object> usersValues = new HashMap<>();
        DatabaseReference userSettings = FirebaseDatabase.getInstance().getReference().child("users").child(userToken).child("name");
        userSettings.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> settings = (Map<String, Object>) dataSnapshot.getValue();
                if (settings != null)
                {
                    Username = settings.get("name").toString();
                }
        }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return Username;
    }
    */
}



