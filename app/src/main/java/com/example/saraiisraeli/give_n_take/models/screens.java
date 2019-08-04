package com.example.saraiisraeli.give_n_take.models;

import android.content.Context;
import android.content.Intent;


import static androidx.core.content.ContextCompat.startActivity;

public class screens {
    public String screen_name;
    public String base_context;
    Intent myIntent;

    public screens (){}

    public  screens (String screen_name,String base_context)
    {
        this.base_context=base_context;
        this.screen_name=screen_name;
    }
    public void move_to(Class screen_name,Context base_context)
    {
    //    myIntent = new Intent(base_context,screen_name);
      //  startActivity(myIntent);
    }
}
