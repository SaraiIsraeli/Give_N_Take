package com.example.saraiisraeli.give_n_take.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {
    private static final String TAG = "edit profile";
    String userId;
    EditText m_name;
    EditText m_phoneNumber;
    Button m_start, m_back, disconnect;
    Intent myIntnet;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    private DatabaseReference dbRef;
    String m_nameStr, m_phoneNumberStr;
    private DatabaseReference mDatabaseUser_name,mDatabaseUser_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Log.d(TAG, "entered edit profile" );
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
        m_name = (EditText) findViewById(R.id.name);
        m_phoneNumber = (EditText) findViewById(R.id.phone);
        m_start = (Button) findViewById(R.id.btnSave);
        m_back = (Button)findViewById(R.id.btnBack);
        disconnect = findViewById(R.id.disconnect);
        mDatabaseUser_name = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("name");
        mDatabaseUser_phone = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("phoneNumber");

        mDatabaseUser_name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Toast.makeText(getApplicationContext(),dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                m_name.setText(dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabaseUser_phone.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                m_phoneNumber.setText(dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        m_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    Log.d(TAG, "form is filled successfuly" );
                    User user = new User(m_nameStr, m_phoneNumberStr);
                    dbRef.child("users").child(userId).setValue(user);
                    Log.d(TAG, "update user in Firebase - " + "user name: " +  user.getName()+
                            " user phone number: "  + user.getPhoneNumber());
                    myIntnet = new Intent(EditProfile.this ,MainActivity.class);
                    startActivity(myIntnet);
                    finish();
                }
            }
        });

        m_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myIntnet = new Intent(EditProfile.this ,MainActivity.class);
                startActivity(myIntnet);
                finish();
            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
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
    private void signOut() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.logout);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseAuth.signOut();
                myIntnet = new Intent(EditProfile.this ,LoginActivity.class);
                startActivity(myIntnet);
                finish();
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }
}
