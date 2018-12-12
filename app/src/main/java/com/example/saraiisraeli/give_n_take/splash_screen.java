package com.example.saraiisraeli.give_n_take;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.saraiisraeli.give_n_take.MainActivity;

/**
 * Created by ssaurel on 02/12/2016.
 */

public class splash_screen extends AppCompatActivity {
    private int SPLASH_TIME = 3000;
    Intent myIntent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(SPLASH_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    myIntent = new Intent(getBaseContext(),MainActivity.class);
                    startActivity(myIntent);
                }
            }
        };
        timer.start();
    }
}