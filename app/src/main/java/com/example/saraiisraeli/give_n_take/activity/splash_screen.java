package com.example.saraiisraeli.give_n_take.activity;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;

import com.example.saraiisraeli.give_n_take.R;

public class splash_screen extends AppCompatActivity {
    private static int splashTimeOut=4500;
    Intent myIntent;
    ImageView logo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        logo = (ImageView)findViewById(R.id.logo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Calling Login page:
                Intent i = new Intent(splash_screen.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        },splashTimeOut);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Path path = new Path();
            //    path.arcTo(200, 0f, 1000f, 1000f, 0f, 180f, true);
            path.moveTo(170,1200);
            path.lineTo(170,400);
            ObjectAnimator animator = ObjectAnimator.ofFloat(logo, logo.X, logo.Y, path);
            animator.setDuration(1500);
            animator.start();
        } else {
            // Create animator without using curved path
        }
    }
}