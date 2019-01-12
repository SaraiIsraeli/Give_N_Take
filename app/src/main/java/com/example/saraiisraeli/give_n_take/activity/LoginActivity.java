package com.example.saraiisraeli.give_n_take.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saraiisraeli.give_n_take.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

    Intent myIntnet;
    public TextView linkToRegister;
    private Button sign;
    private EditText UserEmail;
    private EditText UserPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UserEmail = findViewById(R.id.input_email);
        UserPassword = findViewById(R.id.input_password);
        linkToRegister = findViewById(R.id.link_signup);
        linkToRegister.setOnClickListener(this);
        sign = findViewById(R.id.btn_login);
        sign.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

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
                        Toast.makeText(LoginActivity.this, "log in failed",
                        Toast.LENGTH_SHORT).show();
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
    private void updateUI(FirebaseUser user) {
        if (user != null)
        {
            // need to move to the main App!
            myIntnet = new Intent(LoginActivity.this ,MyProfileActivity.class);
            startActivity(myIntnet);
            finish();
        }
        else
        {
            findViewById(R.id.btn_login).setVisibility(View.GONE);
            findViewById(R.id.input_email).setVisibility(View.VISIBLE);
            findViewById(R.id.input_password).setVisibility(View.VISIBLE);
        }
    }

}
