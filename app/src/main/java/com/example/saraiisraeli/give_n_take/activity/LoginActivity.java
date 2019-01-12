package com.example.saraiisraeli.give_n_take.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    Intent myIntnet;
    //private Button reg;
    public TextView linkToRegister;
    private Button sign;
    private EditText userEmail;
    private EditText password;
    private FirebaseAuth firebaseAuth;


    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.LoginActivity);
        userEmail = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        linkToRegister = findViewById(R.id.link_signup);
        sign = findViewById(R.id.btn_login);
        //reg.setOnClickListener(this);
        sign.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view)
    {
        if (view == linkToRegister)
        {
            registerUser();
        }
        if (view == sign)
        {
            SignUser();//login process .
        }
    }

    void registerUser()
    {
        //move to Register screen .
        myIntnet = new Intent(getBaseContext(),SignUpActivity.class);
        startActivity(myIntnet);
    }

    void SignUser()
    {
        String email = userEmail.getText().toString().trim();
        String pass = userEmail.getText().toString().trim();
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
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Welcome Back",
                                Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);//Move to APP !!!!
                        startActivity(myIntent);
                        finish();
                    }
                }
            });
    }





}
