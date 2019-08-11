package com.example.saraiisraeli.give_n_take.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.saraiisraeli.give_n_take.activity.Items;
import com.example.saraiisraeli.give_n_take.activity.MainActivity;
import com.example.saraiisraeli.give_n_take.activity.Search;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AppData {
    private DatabaseReference mDatabase;
    private DatabaseReference itemsRef;
    private FirebaseUser mAuth;
    private int maxID;
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
        if ((!userToken.isEmpty()) && (Integer.valueOf(distance) > 0)) {
            try {
                mDatabase.child("userSettings").child(userToken).child("distance").setValue(distance);
                if (!productName.isEmpty())
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
                    if (settings != null) {
                        settingsValues.put("distance", settings.get("distance").toString());
                        if (settings.get("prodQuery") == null) {
                            settingsValues.put("prodQuery", "");
                        } else {
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
        String photoURL = ItemValues.get("photoURL").toString();

        try {
            mDatabase.child("items").child(userToken).child("itemName").setValue(itemName);
            mDatabase.child("items").child(userToken).child("itemLocation").setValue(itemLocation);
            mDatabase.child("items").child(userToken).child("itemDescription").setValue(itemDescription);
            mDatabase.child("items").child(userToken).child("photoURL").setValue(photoURL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Log.d(TAG, "End Method: SaveNewItem");
}

    public int setMaxID(String userToken) throws InterruptedException {
        itemsRef = FirebaseDatabase.getInstance().getReference().child("items");
        maxID = 0;
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Log.d(TAG,"insert ondatachange");
                    try {
                        Thread.sleep(10000);

                        maxID = collectAllTokens((Map<String, Object>) dataSnapshot.getValue());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG,"Finished onDataChange");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
        return maxID;
    }

    private int collectAllTokens(Map<String,Object> tokens)
    {
        Log.d(TAG,"Insert get all tokens");
        ArrayList<String> tokensList = new ArrayList<>();
        for(Map.Entry<String,Object> entry : tokens.entrySet())
        {
            tokensList.add(entry.getKey());
        }

        for (int counter = 0; counter < tokensList.size(); counter++) {
            String currToken = tokensList.get(counter);
            String[] lastIdAray = currToken.split("id");
            if (lastIdAray != null) {
                for (String str : lastIdAray) {

                    int currid = Integer.valueOf(str);

                    if (currid > maxID) {
                        maxID = currid;
                    }
                }
            }
        }
        return maxID;
    }

    public void getAllItems(List<Map<String, Object>> itemsValues, MainActivity mainActivity, String distance,String prodQuery) {
        List<String> tokensList = new ArrayList<>();
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("items");
        if (itemsRef == null) {
            Log.i(TAG, "no items found");
        } else {
            Log.i(TAG, "items found");

            itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> items = (Map<String, Object>) dataSnapshot.getValue();
                    Log.i(TAG, "dataSnapshot:" + dataSnapshot.getValue());
                    if (items != null)
                    {
                        for (DataSnapshot dataSnapshotItem : dataSnapshot.getChildren()){
                            items = (Map<String, Object>) dataSnapshotItem.getValue();
                            itemsValues.add(items);
                            tokensList.add(dataSnapshotItem.getKey());
                        }
                        try {
                            mainActivity.checkItemsToShow(itemsValues,distance,tokensList,prodQuery);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i(TAG, "items is null! ");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }
    }



    public void getUserItems(final String userToken, Items mItems) {
        Map<String, Object> itemValues = new HashMap<>();
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("items").child(userToken);
        if (itemsRef == null) {
            Log.i(TAG, "no items for:" + userToken);
        } else {
            Log.i(TAG, "user items for:" + userToken);

            itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> items = (Map<String, Object>) dataSnapshot.getValue();
                    Log.i(TAG, "dataSnapshot:" + dataSnapshot.getValue());
                    if (items != null)
                    {
                        itemValues.put("itemName",items.get("itemName").toString());
                        itemValues.put("itemDescription",items.get("itemDescription").toString());
                        itemValues.put("itemLocation",items.get("itemLocation").toString());
                        mItems.getItem(itemValues);
                    } else {
                        Log.i(TAG, "items is null! ");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }
    }

    public void getUserDistanceAndNameToSearch(String userToken, MainActivity mainActivity)
    {
        final Map<String, Object> settingsValues = new HashMap<>();
        DatabaseReference settingsRef = FirebaseDatabase.getInstance().getReference().child("userSettings").child(userToken);
        Log.i(TAG, "user settings for:" + userToken);
        settingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> settings = (Map<String, Object>) dataSnapshot.getValue();
                Log.i(TAG, "dataSnapshot:" + dataSnapshot.getValue());
                if (settings != null) {
                    settingsValues.put("distance", settings.get("distance").toString());
                    settingsValues.put("prodQuery", settings.get("prodQuery").toString());
                    mainActivity.getDistance(settingsValues.get("distance").toString(),settingsValues.get("prodQuery").toString());
                } else {
                    Log.i(TAG, "settings is null! ");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public String getUserRole(String userToken)
    {
        final String[] str = new String[1];
        final Map<String, Object> userRole = new HashMap<>();
        DatabaseReference userListener = FirebaseDatabase.getInstance().getReference().child("users").child(userToken);
        Log.i(TAG, "user settings for:" + userToken);
        userListener.addListenerForSingleValueEvent (new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> dataMap = (Map<String, Object>) Objects.requireNonNull(dataSnapshot).getValue();
                Log.i(TAG, "dataSnapshot:" + dataSnapshot.getValue());
                try
                {
                    if (dataMap != null)
                    {
                        userRole.put("role", Objects.requireNonNull(Objects.requireNonNull(dataMap).get("role")).toString());
                        str[0] = getUserRoleValue(userRole);
                    }
                    else
                    {
                        userRole.put("role","");
                        str[0] = getUserRoleValue(userRole);
                    }
                }
                catch (ClassCastException cce)
                {}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "getUserRole:onCancelled", databaseError.toException());
            }

        });
        return str[0];
    }


    public String getUserRoleValue(Map<String, Object> userRole)
    {
        String role = "";
        if (!userRole.isEmpty())
        {
            return (String) userRole.get("role");
        }
        return role;
    }

    public void getUserNameAndPhoneNumber(String userToken,MainActivity mainActivity) {
        final Map<String, Object> userNameAndPhone = new HashMap<>();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userToken);
        Log.i(TAG, "userNameAndPhone: " + userToken);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> userValues = (Map<String, Object>) dataSnapshot.getValue();
                Log.i(TAG, "dataSnapshot:" + dataSnapshot.getValue());
                if (userValues != null) {
                    userNameAndPhone.put("phoneNumber", userValues.get("phoneNumber").toString());
                    userNameAndPhone.put("name", userValues.get("name").toString());
                    mainActivity.sendSms(userNameAndPhone);
                } else {
                    Log.i(TAG, "userValues is null! ");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
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



