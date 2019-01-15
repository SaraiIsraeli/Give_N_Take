package com.example.saraiisraeli.give_n_take.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.saraiisraeli.give_n_take.R;
import com.example.saraiisraeli.give_n_take.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyProfileActivity extends AppCompatActivity {

    private static final String TAG = "my profile";
    String userId;
    EditText m_name;
    EditText m_phoneNumber;
    Button m_start;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    private DatabaseReference dbRef;
    String m_nameStr, m_phoneNumberStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Log.d(TAG, "entered my profile" );
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef=firebaseDatabase.getReference();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        userId=user.getUid();
        firebaseAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
            }
        };
        m_name = (EditText) findViewById(R.id.Name);
        m_phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        m_start = (Button) findViewById(R.id.submit_Profile);

        m_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    Log.d(TAG, "form is filled successfuly" );
                    User user = new User(m_nameStr, m_phoneNumberStr);
                    dbRef.child("users").child(userId).setValue(user);
                    Log.d(TAG, "update user in Firebase - " + "user name: " +  user.getName()+
                            " user phone number: "  + user.getPhoneNumber());
                }
            }
        });

    }

    private boolean validateFields() {
        boolean isValid = true;

        m_nameStr = m_name.getText().toString();
        m_phoneNumberStr = m_phoneNumber.getText().toString();

        if (TextUtils.isEmpty(m_nameStr)) {
            m_name.setError("נא להכניס שם");
            Log.d(TAG, "name isn't filled");
        }
        else if (!isAlpha(m_nameStr)) {
                isValid = false;
                m_name.setError("נא להכניס אותיות בלבד");
                Log.d(TAG, "name isn't only letters");
            }
            else m_name.setError(null);

        if (!(TextUtils.isDigitsOnly(m_phoneNumberStr)) || (m_phoneNumberStr.length() != 10)) {
            m_phoneNumber.setError("נא להכניס נייד בן 10 ספרות בלבד");
            {
                isValid = false;
                Log.d(TAG, "phone isn't 10 digits only");
            }
        } else {
            m_phoneNumber.setError(null);
        }

        return  isValid;
    }
    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
}
