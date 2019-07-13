package com.example.saraiisraeli.give_n_take.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saraiisraeli.give_n_take.R;
import com.example.saraiisraeli.give_n_take.models.AppData;
import com.example.saraiisraeli.give_n_take.models.UserSettings;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;

public class Search extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "";
    Map<String, Object> SettingsValues;
    AppData mAppData = new AppData();

    String userToken = (mAppData.getCurrentUser().getUid());
    private String dis = " KM";
    private String afterSaveMsg = "Save Succeed ";
    private Button Back, SaveSettings_btn;
    private SeekBar sBar;
    private TextView tView;
    private CheckBox give_Checkbox,get_Checkbox;
    private Snackbar saveMsg;
    private View currView;

@SuppressLint("WrongConstant")
@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        sBar = findViewById(R.id.seekBar);
        tView = findViewById(R.id.SliderValue);

        Back = findViewById(R.id.Back);
        SaveSettings_btn = findViewById(R.id.SaveSettings_btn);

        SaveSettings_btn.setOnClickListener(this);

        give_Checkbox = findViewById(R.id.Role_Give_checkbox);
        get_Checkbox = findViewById(R.id.Role_Get_checkbox);

        currView = findViewById(R.id.SearchSettingsLayout);


        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            int pval = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                pval = progress;
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tView.setText(pval + dis);
            }
        });
        mAppData.getDistanceSettings(userToken,this);
    }
    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View view)
    {
        Log.d(TAG, "Start Method: onClick");
        if (view == Back)
        {
            ReturnToMain();
        }
        if (view == SaveSettings_btn)
        {
            SaveSettingsToDB();
            Snackbar.make(currView, afterSaveMsg, LENGTH_LONG).setDuration(30000).show();
        }
    }

    private void ReturnToMain()
    {
        Log.d(TAG, "Start Method: ReturnToMain");
        Intent myIntent = new Intent(Search.this, MainActivity.class);
        startActivity(myIntent);
        finish();
    }

    private void SaveSettingsToDB()
    {
        Log.d(TAG, "Start Method: SaveSettingsToDB");
        //validate Distance field contains a valid value
        //save the settings to DB
        boolean isRoleValid = ValidateRoleField();
        boolean isDistanceValid = ValidateDistanceField();
        if(isDistanceValid && isRoleValid)
        {
            try
            {
                userToken = (mAppData.getCurrentUser().getUid());
                String dis = GetDisValue();
                String role = GetRoleValue();
                UserSettings us = new UserSettings(userToken,dis,role);
                SettingsValues = us.toMap();
                if (!SettingsValues.isEmpty())
                {
                    mAppData.setDistanceSettings(SettingsValues);
                    saveMsg.show();
                    Log.d(TAG, "End Method: SaveSettingsToDB");

                    mAppData.setDistanceSettings(SettingsValues);
                }

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        ReturnToMain();
    }

    private boolean ValidateRoleField()
    {
        Log.d(TAG, "Start Method: ValidateRoleField");
        boolean checked = true;
        if (!(get_Checkbox.isChecked()) && (!give_Checkbox.isChecked())) // both not checked
        {
            SaveSettings_btn.setError("Must choose at least one");
            checked = false;
        }
        Log.d(TAG, "End Method: ValidateRoleField");
        return checked;

    }
    private String GetRoleValue()
    {
        Log.d(TAG, "Start Method: GetRoleValue");
        String role = "0"; // by defualt - Buyer
        boolean RoleCheck = ValidateRoleField();
        if (RoleCheck)
        {
            if (get_Checkbox.isChecked() && give_Checkbox.isChecked())
            {// User is Seller and Buyer
                role = "2";
            }
            else if (!(get_Checkbox.isChecked()) && give_Checkbox.isChecked())
            {//User is Seller only
                role = "1";
            }
            else
            {//User is Buyer
                role = "0";
            }
        }
        Log.d(TAG, "End Method: GetRoleValue");
        return role;
    }
    private String GetDisValue()
    {
        Log.d(TAG, "Start Method: GetDisValue");
        String dis = "0";
        int progress = sBar.getProgress();
        if(progress > 0)
        {
            dis = String.valueOf(progress);
        }
        Log.d(TAG, "End Method: GetDisValue");
        return dis;
    }
    private boolean ValidateDistanceField()
    {
        Log.d(TAG, "Start Method: ValidateDistanceField");
        boolean isValid = false;
        int progress = sBar.getProgress();
        if(progress > 0)
        {
            isValid = true;
        }
        else
        {
            SaveSettings_btn.setError("Distance can't be 0");
        }
        Log.d(TAG, "End Method: ValidateDistanceField");
        return isValid;
    }


    public void setDataFromDB(Map<String, Object> settingsValues)
    {
        Log.i (TAG,"setting values:" + settingsValues.get("Role") + settingsValues.get("distance"));
        String distance = settingsValues.get("distance").toString();
        String role = settingsValues.get("Role").toString();
        switch (role){
            case "0":
            {
                get_Checkbox.setChecked(true);
                break;
            }
            case "1":
            {
                give_Checkbox.setChecked(true);
                break;
            }
            case "2":
            {
                get_Checkbox.setChecked(true);
                give_Checkbox.setChecked(true);
                break;
            }
            default: break;
        }

        sBar.setProgress(Integer.valueOf(distance));
        tView.setText(distance);
    }
}

