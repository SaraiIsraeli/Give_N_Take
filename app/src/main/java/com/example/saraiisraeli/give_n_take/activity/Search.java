package com.example.saraiisraeli.give_n_take.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.saraiisraeli.give_n_take.R;


public class Search extends AppCompatActivity
{
    private String dis = " KM";
//    Button SaveSettings_btn = (Button) findViewById(R.id.SaveSettings_btn);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SeekBar sBar = findViewById(R.id.seekBar);
        final TextView tView = (TextView) findViewById(R.id.SliderValue);


        //tView.setText(sBar.getProgress());

        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
//                pval = progress;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tView.setText(String.valueOf(pval) + dis );
            }

        });
    }
}
