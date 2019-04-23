package com.example.saraiisraeli.give_n_take.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.widget.Button;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.saraiisraeli.give_n_take.R;


public class Search extends AppCompatActivity
{
    private String dis = " KM";
    private Button Back;
    private SeekBar sBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        sBar = findViewById(R.id.seekBar);
        final TextView tView = (TextView) findViewById(R.id.SliderValue);
        Back = findViewById(R.id.Back);

        //tView.setText(sBar.getProgress());
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            int pval = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                pval = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
//                pval = progress;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                tView.setText(String.valueOf(pval) + dis );
            }

        });
        Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                Intent myIntent = new Intent(Search.this, MainActivity.class);
                startActivity(myIntent);
                //checkHistory();
            }
        });
    }
}
