package com.example.saraiisraeli.give_n_take.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private CheckBox give_Checkbox,get_Checkbox;
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
        give_Checkbox = findViewById(R.id.Role_Give_checkbox_Edit);
        get_Checkbox = findViewById(R.id.Role_Get_checkbox_Edit);
        disconnect = findViewById(R.id.disconnect);
        DatabaseReference mDatabaseUser_name = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("name");
        DatabaseReference mDatabaseUser_phone = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("phoneNumber");
        DatabaseReference mDatabaseUser_Role = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("role");
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
        mDatabaseUser_Role.addValueEventListener(new ValueEventListener()
        {
            String role="";
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                role = dataSnapshot.getValue().toString();
                if (role.equals("2"))
                {
                    get_Checkbox.setChecked(true);
                    give_Checkbox.setChecked(true);
                }
                else if (role.equals("1"))
                {
                    give_Checkbox.setChecked(true);
                }
                else
                {
                    get_Checkbox.setChecked(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


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
                    String role = GetRoleValue();
                    User user = new User(m_nameStr, m_phoneNumberStr,role);
                    dbRef.child("users").child(userId).setValue(user);
                    Log.d(TAG, "update user in Firebase - " + "user name: " +  user.getName()+
                            " user phone number: "  + user.getPhoneNumber() +"User Role "+ user.getRole());
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

    private boolean ValidateRoleField()
    {
        Log.d(TAG, "Start Method: ValidateRoleField");
        boolean checked = true;
        if (!(get_Checkbox.isChecked()) && (!give_Checkbox.isChecked())) // both not checked
        {
            m_start.setError("Must choose at least one");
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
    @Override
    public void onPause()
    {
        super.onPause();
        ReturnToMain();
    }

    private void ReturnToMain()
    {
        Log.d(TAG, "Start Method: ReturnToMain");
        Intent myIntent = new Intent(EditProfile.this, MainActivity.class);
        startActivity(myIntent);
        finish();
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
        boolean isRoleValid = ValidateRoleField();
        if (!isRoleValid)
        {
            isValid = false;
            Log.d(TAG, "Must Choose At Least 1 Role");
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
