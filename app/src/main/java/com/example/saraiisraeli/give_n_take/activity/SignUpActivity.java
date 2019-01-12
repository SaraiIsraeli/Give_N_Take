package com.example.saraiisraeli.give_n_take.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "";
    private EditText RegEmail;
    private EditText RegPassword;
    private FirebaseAuth mAuth;
    private Button  btn_CreateAccount;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        RegEmail = findViewById(R.id.input_email);
        RegPassword = findViewById(R.id.input_password);
        mAuth = FirebaseAuth.getInstance();
        btn_CreateAccount = findViewById(R.id.btn_signup);
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


    public void  createAccount()
        {
        String Email = RegEmail.getText().toString().trim();
        String Pass = RegPassword.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(Email, Pass)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser FbUser = mAuth.getCurrentUser();
                    }
                    else
                    {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUpActivity.this, "Something Went Wrong..",
                                Toast.LENGTH_SHORT).show();
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
            if (password.length() < 6)
            {
                RegPassword.setError("Password Length must be over 6 characters");
                valid = false;
            }
            else
            {
                RegPassword.setError(null);
            }
            return valid;
    }


}
