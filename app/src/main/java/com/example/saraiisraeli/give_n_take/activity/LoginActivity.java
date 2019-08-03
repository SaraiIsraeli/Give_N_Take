package com.example.saraiisraeli.give_n_take.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saraiisraeli.give_n_take.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG ="";
    Intent myIntnet;
    private Button sign,register;
    private EditText UserEmail;
    private EditText UserPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UserEmail = findViewById(R.id.Input_email);
        UserPassword = findViewById(R.id.input_password);
        sign = findViewById(R.id.btn_login);
        sign.setOnClickListener(this);
        register = findViewById(R.id.link_signup);
        register.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        //Presist Login:
        if (user != null) {
            // User is signed in
            myIntnet = new Intent(LoginActivity.this ,MainActivity.class);
            startActivity(myIntnet);
            finish();
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }
    @Override
    public void onBackPressed() { }
    @Override
    public void onClick(View view)
    {
        int i = view.getId();
        if (i == R.id.link_signup)
        {
            registerUser();
        }
        else if (i == R.id.btn_login)
        {
            SignUser(UserEmail.getText().toString(),UserPassword.getText().toString());//login process .
        }
    }

    void registerUser()
    {
        //move to Register screen .
        myIntnet = new Intent(getBaseContext(),SignUpActivity.class);
        startActivity(myIntnet);
        finish();
    }

    void SignUser(String email,String pass)
    {
        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (!task.isSuccessful())
                    {
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(LoginActivity.this);
                        dlgAlert.setMessage("Wrong Password or email");
                        dlgAlert.setTitle("Log In Failed");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }});
                        updateUI(null);
                    }
                    else
                    {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateUI(user);
                    }
                }
            });
    }
    private void updateUI(FirebaseUser user)
    {
        if (user != null)
        {
            myIntnet = new Intent(LoginActivity.this ,MainActivity.class);
            startActivity(myIntnet);

            finish();
        }
        else
        {
            findViewById(R.id.btn_login).setVisibility(View.VISIBLE);
            findViewById(R.id.Input_email).setVisibility(View.VISIBLE);
            findViewById(R.id.input_password).setVisibility(View.VISIBLE);
        }
    }

}
