package com.example.saraiisraeli.give_n_take.activity;

import android.content.Intent;
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

public class EditProfile extends AppCompatActivity {
    String userId;
    EditText name;
    EditText phone;
    String nameStr;
    String phoneStr;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    private DatabaseReference dbRef;
    String namestr, phonestr;
    private static final String TAG = "edit profile";
    Button save;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Button save = (Button) findViewById(R.id.save);
        Button back = (Button) findViewById(R.id.back);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
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
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                }

            } ;
            name.setText(user.getDisplayName());
            phone.setText(user.getPhoneNumber());
            //back.setOnClickListener(ne);

        back.setOnClickListener(new View.OnClickListener() {
            private Intent myIntent;

            @Override
            public void onClick(View v) {
                myIntent = new Intent(Items.this, MainActivity.class);
                startActivity(myIntent);
                checkHistory();
            }
            private void checkHistory() {
            }
        });
                 save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        if (validateFields()) {
                            Log.d(TAG, "form is filled successfuly");
                            User user = new User(namestr, phonestr);
                            dbRef.child("users").child(userId).setValue(user);
                            Log.d(TAG, "update user in Firebase - " + "user name: " + user.getName() +
                                    " user phone number: " + user.getPhoneNumber());
                            finish();

                        }
                    }
                    });
                }


        private boolean validateFields() {
            {
                boolean isValid = true;
                nameStr = name.getText().toString();
                phoneStr = phone.getText().toString();
                if (TextUtils.isEmpty(nameStr)) {
                    name.setError("נא להכניס שם");
                    Log.d(TAG, "name isn't filled");
                } else if (!isAlpha(nameStr)) {
                    isValid = false;
                    name.setError("נא להכניס אותיות בלבד");
                    Log.d(TAG, "name isn't only letters");
                } else name.setError(null);

                if (!(TextUtils.isDigitsOnly(phoneStr)) || (phoneStr.length() != 10)) {
                    phone.setError("נא להכניס נייד בן 10 ספרות בלבד");
                    {
                        isValid = false;
                        Log.d(TAG, "phone isn't 10 digits only");
                    }
                } else {
                    phone.setError(null);
                }
                return isValid;

            }
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
