package com.example.saraiisraeli.give_n_take.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.saraiisraeli.give_n_take.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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

   /* public void onPause()
    {
        super.onPause();
        returnToLogin();
    }*/

    private void returnToLogin()
    {
        Log.d(TAG, "Start Method: returnToLogin");
        Intent myIntent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(myIntent);
    }

    private void  createAccount()
    {
        Log.d(TAG, "Start Method: createAccount");
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
                        try
                        {
                            throw task.getException();
                        }
                        catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                        {
                            Log.d(TAG, "onComplete: malformed_email");
                            Toast.makeText(SignUpActivity.this, "email address is malformed",
                            Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        catch (FirebaseAuthUserCollisionException existEmail)
                        {
                            Log.d(TAG, "onComplete: exist_email");
                            Toast.makeText(SignUpActivity.this, "Email Address already exists!",
                            Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        catch (Exception e)
                        {
                            Log.d(TAG, "onComplete: " + e.getMessage());
                        }
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());

                    }
                }
            });
    }

    private boolean validateForm()
    {
        Log.d(TAG, "Start Method: validateForm");
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
        Log.d(TAG, "End Method: validateForm");
            return valid;
    }
    private void updateUI(FirebaseUser user)
    {
        Log.d(TAG, "Start Method: updateUI");
        if (user != null)
        {
            myIntnet = new Intent(SignUpActivity.this ,EditProfile.class);
            startActivity(myIntnet);
            finish();
        }
        else
        {
            findViewById(R.id.btn_signup).setVisibility(View.VISIBLE);
            findViewById(R.id.Input_email).setVisibility(View.VISIBLE);
            findViewById(R.id.input_password).setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "End Method: updateUI");
    }

}
