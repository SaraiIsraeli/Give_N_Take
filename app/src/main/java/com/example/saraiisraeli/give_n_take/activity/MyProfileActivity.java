package com.example.saraiisraeli.give_n_take.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.saraiisraeli.give_n_take.R;
import com.example.saraiisraeli.give_n_take.models.User;

public class MyProfileActivity extends AppCompatActivity {

    EditText m_name;
    EditText m_city;
    EditText m_phoneNumber;
    Button m_start;

    String m_nameStr, m_cityStr, m_phoneNumberStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        m_name = (EditText) findViewById(R.id.Name);
        m_city = (EditText) findViewById(R.id.city);
        m_phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        m_start = (Button) findViewById(R.id.submit_Profile);

        m_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    User user = new User(m_nameStr, m_phoneNumberStr, m_cityStr);
                }
            }
        });

    }

    private boolean validateFields() {
        boolean isValid = true;

        m_nameStr = m_name.getText().toString();
        m_cityStr = m_city.getText().toString();
        m_phoneNumberStr = m_phoneNumber.getText().toString();

        if (TextUtils.isEmpty(m_nameStr)) {
            m_name.setError("נא להכניס שם");
            isValid = false;
        } else {
            m_name.setError(null);
        }

        if (TextUtils.isEmpty(m_cityStr)) {
            m_city.setError("נא לקליד את שם העיר");
            isValid = false;
        } else {
            m_city.setError(null);
        }

        if (!(TextUtils.isDigitsOnly(m_phoneNumberStr)) || (m_phoneNumberStr.length() != 10)) {
            m_phoneNumber.setError("נא להכניס מספר טלפון בן 10 ספרות בלבד");
            isValid = false;
        } else {
            m_phoneNumber.setError(null);
        }

        return  isValid;
    }
}
