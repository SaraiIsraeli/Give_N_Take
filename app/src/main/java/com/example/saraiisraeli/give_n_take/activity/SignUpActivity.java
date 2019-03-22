package com.example.saraiisraeli.give_n_take.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.saraiisraeli.give_n_take.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends LoginActivity implements View.OnClickListener
{
    private static final String TAG = "";
    private EditText RegEmail;
    private EditText RegPassword;
    private FirebaseAuth mAuth;
    private Button  btn_CreateAccount;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Views
        RegEmail = findViewById(R.id.Input_email);
        RegPassword = findViewById(R.id.input_password);
        btn_CreateAccount = findViewById(R.id.btn_signup);

        //Button
        findViewById(R.id.btn_signup).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view)
    {
        if (view == btn_CreateAccount )
        {
            if (!validateForm())
            {
                return;
            }
            else
            {
               createAccount();
            }
        }
    }

    private void  createAccount()
    {
        String Email = RegEmail.getText().toString().trim();
        String Pass = RegPassword.getText().toString().trim();
        Log.d(TAG, "createAccount:" + Email);

        mAuth.createUserWithEmailAndPassword(Email, Pass)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser FbUser = mAuth.getCurrentUser();
                        updateUI(FbUser);
                    }
                    else
                    {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUpActivity.this, "Something Went Wrong..",
                        Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
    }

    private boolean validateForm()
    {
            boolean valid = true;
            String email = RegEmail.getText().toString();
            if (TextUtils.isEmpty(email))
            {
                RegEmail.setError("Required.");
                valid = false;
            }
            if (!email.contains("@"))
            {
                RegEmail.setError("Not Valid Email Address");
                valid = false;
            }
            else
            {
                RegEmail.setError(null);
            }
            String password = RegPassword.getText().toString();
            if (TextUtils.isEmpty(password))
            {
                RegPassword.setError("Required.");
                valid = false;
            }
            if (password.length() < 8)
            {
                RegPassword.setError("Password Length must be over 8 characters");
                valid = false;
            }
            else
            {
                RegPassword.setError(null);
            }
            return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null)
        {
            myIntnet = new Intent(SignUpActivity.this ,MyProfileActivity.class);
            startActivity(myIntnet);
            finish();
        }
        else
        {
            findViewById(R.id.btn_signup).setVisibility(View.VISIBLE);
            findViewById(R.id.Input_email).setVisibility(View.VISIBLE);
            findViewById(R.id.input_password).setVisibility(View.VISIBLE);
        }
    }



}
