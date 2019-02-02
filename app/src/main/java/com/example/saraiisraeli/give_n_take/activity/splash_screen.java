package com.example.saraiisraeli.give_n_take.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.saraiisraeli.give_n_take.R;

public class splash_screen extends AppCompatActivity {
    private static int splashTimeOut=4000;
    Intent myIntent;
    ImageView logo;
    Animation moveUp;
    LinearLayout logo_layout;
    private static final String TAG = "splash";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Log.d(TAG, "entered splash screen" );
            logo_layout = (LinearLayout) findViewById(R.id.logo_layout);
            moveUp = AnimationUtils.loadAnimation(this,R.anim.moveup);
            logo_layout.setAnimation(moveUp);
            Log.d(TAG, "logo animation" );

         new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Calling Login page:
                Intent i = new Intent(splash_screen.this,intro.class);
                startActivity(i);
                finish();
            }
        },splashTimeOut);
    }
}