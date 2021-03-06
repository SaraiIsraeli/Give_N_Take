package com.example.saraiisraeli.give_n_take.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.saraiisraeli.give_n_take.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class intro extends AppCompatActivity {

   com.example.saraiisraeli.give_n_take.models.PreferenceManager preferenceManager;
    LinearLayout Layout_bars;
    TextView[] bottomBars;
    int[] screens;
    Button Skip, Next,back;
    ViewPager vp;
    MyViewPagerAdapter myvpAdapter;
    private static final String TAG = "intro";
    private FirebaseUser user;
    private Intent myIntnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        Log.d(TAG, "entered intro" );
        user = FirebaseAuth.getInstance().getCurrentUser();
        //Presist Login:
        if (user != null) {
            // User is signed in
            myIntnet = new Intent(intro.this ,MainActivity.class);
            startActivity(myIntnet);
            finish();
        } else {
            // User is signed out
            Log.d(TAG, "User not found - will show Intro");
        }
        vp = (ViewPager) findViewById(R.id.view_pager);
        Layout_bars = (LinearLayout) findViewById(R.id.layoutBars);
        Skip = (Button) findViewById(R.id.skip);
        Next = (Button) findViewById(R.id.next);
        back =  (Button) findViewById(R.id.back);
        myvpAdapter = new MyViewPagerAdapter();
        screens = new int[]{
                R.layout.intro_screen1,
                R.layout.intro_screen2,
                R.layout.intro_screen3
        };
        vp.setAdapter(myvpAdapter);
        preferenceManager = new com.example.saraiisraeli.give_n_take.models.PreferenceManager(this);
        vp.addOnPageChangeListener(viewPagerPageChangeListener);
       // if (!preferenceManager.FirstLaunch()) {
         //   Log.d(TAG, "already saw intro-not firstlaunch");
           // launchMain();
            //finish();
        //}
        ColoredBars(0);
    }

    public void next(View v) {
        int i = getItem(+1);
        if (i < screens.length) {
            vp.setCurrentItem(i);
        } else {
            launchMain();
        }
    }

    public void back(View v) {
        int i = getItem(-1);
        if (i > -1) {
            vp.setCurrentItem(i);
        } else {
            launchMain();
        }
    }

    public void skip(View v) {
        launchMain();
    }

    private void ColoredBars(int thisScreen) {
        int[] colorsInactive = new int []
                {
                        R.color.colorIntroInActive,
                        R.color.colorIntroInActive,
                        R.color.colorIntroInActive
                };
        int[] colorsActive = new int []
                {
                        R.color.colorIntroActive,
                        R.color.colorIntroActive,
                        R.color.colorIntroActive
                };

        bottomBars = new TextView[screens.length];
        Layout_bars.removeAllViews();
        for (int i = 0; i < bottomBars.length; i++) {
            bottomBars[i] = new TextView(this);
            bottomBars[i].setTextSize(100);
            bottomBars[i].setText(Html.fromHtml(("¯")));
            Layout_bars.addView(bottomBars[i]);
            bottomBars[i].setTextColor(colorsActive[i]);
        }
        if (bottomBars.length > 0)
            bottomBars[thisScreen].setTextColor(colorsInactive[0]);
    }

    private int getItem(int i) {
        return vp.getCurrentItem() + i;
    }

    private void launchMain() {
        preferenceManager.setFirstTimeLaunch(false);
        startActivity(new Intent(intro.this, LoginActivity.class));
        finish();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
           ColoredBars(position);
           if (position == 0){
               back.setVisibility(View.INVISIBLE);
           } else {
               back.setVisibility(View.VISIBLE);
           }
            if (position == 1) {
                Log.d(TAG, "3rd intro screen" );
                Next.setText("התחל");
            } else if (position==1){ Log.d(TAG, "2nd intro screen" );}
                Next.setText(getString(R.string.next));
                Skip.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(screens[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return screens.length;
       }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }

        @Override
        public boolean isViewFromObject(View v, Object object) {
            return v == object;
        }
    }
}