package com.example.saraiisraeli.give_n_take.models;

import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private static final String TAG = "";
    private String itemName,itemLocation,itemDescription,userToken;
    private Uri photoURL;

    public Item(String itemName, String itemLocation, String itemDescription, String userToken, Uri photoURL) {
        this.itemName = itemName;
        this.itemLocation = itemLocation;
        this.itemDescription = itemDescription;
        this.userToken = userToken;
        this.photoURL = photoURL;
    }

    public static String getTAG() {
        return TAG;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Uri getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(Uri photoURL) {
        this.photoURL = photoURL;
    }

    public Item(String itemName, String itemLocation, String itemMoreInfo, Uri photoURL) {
        this.itemName = itemName;
        this.itemLocation = itemLocation;
        this.itemDescription = itemMoreInfo;
        this.photoURL = photoURL;
    }

    public Item(String itemName, String itemLocation, String itemMoreInfo) {
        this.itemName = itemName;
        this.itemLocation = itemLocation;
        this.itemDescription = itemMoreInfo;
    }

    public Item() {
    }

    public Map<String,Object> ItemToMap()
    {
        Log.d(TAG, "Start Method: toMap");
        HashMap<String, Object> result = new HashMap<>();
        result.put("itemName",itemName);
        result.put("itemLocation",itemLocation);
        result.put("itemDescription",itemDescription);
        result.put("photoURL",photoURL);

        Log.d(TAG, "End Method: toMap");
        return result;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemMoreInfo() {
        return itemDescription;
    }

    public void setItemMoreInfo(String itemMoreInfo) {
        this.itemDescription = itemMoreInfo;
    }

    public Uri getPhotoStr() {
        return photoURL;
    }

    public void setPhotoStr(Uri photoURL) {
        this.photoURL = photoURL;
    }
}
