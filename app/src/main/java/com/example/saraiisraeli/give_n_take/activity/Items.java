package com.example.saraiisraeli.give_n_take.activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.saraiisraeli.give_n_take.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Items extends AppCompatActivity implements View.OnClickListener{

    String sssss;
    private static final String TAG = "my profile";
    String userId;
    EditText m_itemName;
    EditText m_itemDesc;
    EditText m_location;
    Button m_newItemBtn;
    Button m_historyItemsBtn;
    Button m_backBtn;
    RadioButton m_currentLocation;
    Intent myIntnet;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    private DatabaseReference dbRef;
    String m_itemNameStr, m_itemDecStr, m_locationStr;
    Boolean m_ischecked;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        Log.d(TAG, "entered items" );
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

        m_historyItemsBtn = (Button)findViewById(R.id.HistoryBtn);
        m_newItemBtn = (Button)findViewById(R.id.ItemsBtn);
        m_currentLocation = (RadioButton)findViewById(R.id.LocationRB);
        m_location = (EditText)findViewById(R.id.LocationET);
        m_itemName = (EditText)findViewById(R.id.ItemName);
        m_itemDesc = (EditText)findViewById(R.id.ItemDesc);
        m_backBtn = (Button)findViewById(R.id.Back);

        m_newItemBtn.setOnClickListener(this);
        m_historyItemsBtn.setOnClickListener(this);
        m_backBtn.setOnClickListener(this);
        m_currentLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    m_location.setEnabled(false);
                    m_location.setInputType(InputType.TYPE_NULL);
                    m_location.setFocusableInTouchMode(false);
                    m_ischecked = isChecked;
                } else {
                    m_location.setEnabled(false);
                    m_location.setFocusableInTouchMode(false);
                    m_ischecked = isChecked;
                }
            }
        });
        events();
    }

    private void events()
    {
        getItemDetails();
    }

    private void getItemDetails()
    {
        m_itemDecStr = m_itemDesc.getText().toString();
        m_itemNameStr = m_itemName.getText().toString();
        if (m_ischecked == true){
            m_locationStr = getCurrentLocation();
        }
        else{
            m_locationStr = m_location.getText().toString();
        }
    }

    private String getCurrentLocation()
    {
      String location = "";

      return location;
    }
// test 
    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.HistoryBtn:
                Log.d(TAG, "HistoryItems button pressed" );
                myIntnet = new Intent(Items.this ,HistoryItems.class);
                startActivity(myIntnet);
                finish();
                break;
            case R.id.ItemsBtn:
                Log.d(TAG, "Items button pressed" );
                myIntnet = new Intent(Items.this ,Items.class);
                startActivity(myIntnet);
                finish();
                break;
            case R.id.Back:
                Log.d(TAG, "Back button pressed" );
                myIntnet = new Intent(Items.this ,MainActivity.class);
                startActivity(myIntnet);
                finish();
                break;
            default:
                break;
        }
    }
}
