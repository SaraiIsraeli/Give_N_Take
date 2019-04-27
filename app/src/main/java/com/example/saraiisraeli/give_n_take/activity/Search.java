package com.example.saraiisraeli.give_n_take.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.widget.Button;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.saraiisraeli.give_n_take.R;
import com.example.saraiisraeli.give_n_take.models.AppData;
import com.example.saraiisraeli.give_n_take.models.User;
import com.example.saraiisraeli.give_n_take.models.UserSettings;

import java.util.Map;


public class Search extends AppCompatActivity implements View.OnClickListener
{
    private String dis = " KM";
    private Button Back, SaveSettings_btn;
    private SeekBar sBar;
    private TextView tView;
    private CheckBox give_Checkbox,get_Checkbox;

    AppData data = new AppData();
    //User user = new User();
    //UserSettings us = new UserSettings();

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
        Back.setOnClickListener(this);

        give_Checkbox = findViewById(R.id.Role_Give_checkbox);
        get_Checkbox = findViewById(R.id.Role_Get_checkbox);

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
    }
//
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState)
//    {
//        int dis = GetDisValue();
//        int role = GetRoleValue();
//
//        savedInstanceState.putString(“Name”, strName);
//        savedInstanceState.putString(“Email”, strEmail);
//        savedInstanceState.putBoolean(“TandC”, blnTandC);
//
//        super.onSaveInstanceState(savedInstanceState);
//    }

    @Override
    public void onClick(View view)
    {
        if (view == Back)
        {
            ReturnToMain();
        }
        if (view == SaveSettings_btn)
        {
            SaveSettingsToDB();
        }
    }

    private void ReturnToMain()
    {
        Intent myIntent = new Intent(Search.this, MainActivity.class);
        startActivity(myIntent);
    }

    private void SaveSettingsToDB()
    {
        //validate Distance field contains a valid value
        //save the settings to DB
        boolean isRoleValid = ValidateRoleField();
        boolean isDistanceValid = ValidateDistanceField();
        if(isDistanceValid && isRoleValid)
        {
            try
            {
                String userToken = (data.getCurrentUser().getUid());
                int dis = GetDisValue();
                int role = GetRoleValue();
                UserSettings us = new UserSettings(userToken,dis,role);
                Map<String, Object> SettingsValues = us.toMap();
                if (!SettingsValues.isEmpty())
                {
                    data.SaveDistanceSettings(SettingsValues);
                }

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private boolean ValidateRoleField()
    {
        boolean checked = true;
        if (!(get_Checkbox.isChecked()) && (!give_Checkbox.isChecked())) // both not checked
        {
            SaveSettings_btn.setError("Must choose at least one");
            checked = false;
        }
        return checked;
    }
    private int GetRoleValue()
    {
        int role = 0; // by defualt - Buyer
        boolean RoleCheck = ValidateRoleField();
        if (RoleCheck)
        {
            if (get_Checkbox.isChecked() && give_Checkbox.isChecked())
            {// User is Seller and Buyer
                role = 2;
            }
            if (!(get_Checkbox.isChecked()) && give_Checkbox.isChecked())
            {//User is Seller only
                role = 1;
            }
            else
            {//User is Buyer
                role = 0;
            }
        }
        return role;
    }
    private int GetDisValue()
    {
        int dis = 0;
        int progress = sBar.getProgress();
        if(progress > 0)
        {
            dis = progress;
        }
        return dis;
    }
    private boolean ValidateDistanceField()
    {
        boolean isValid = false;
        int progress = sBar.getProgress();
        if(progress > 0)
        {
            isValid = true;
        }
        else
        {
            SaveSettings_btn.setError("Distance cant be 0");
        }
        return isValid;
    }

}

