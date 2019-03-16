        package com.example.saraiisraeli.give_n_take.activity;

        import android.content.ClipData;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import com.example.saraiisraeli.give_n_take.R;
        import com.example.saraiisraeli.give_n_take.models.User;

        import android.content.Context;
        import android.util.Log;
        import android.view.GestureDetector;
        import android.view.GestureDetector.SimpleOnGestureListener;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.View.OnTouchListener;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        public class MainActivity extends AppCompatActivity implements View.OnClickListener {

            private static final String TAG = "activity main";
            Intent myIntnet;
            ImageButton itemsButton,profileButton,searchButton;
            Button noButton,yesButton;
            ImageView imageViewNoItems;
            TextView noItemsMessage;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                itemsButton = (ImageButton) findViewById(R.id.itemsButton);
                profileButton = (ImageButton) findViewById(R.id.myProfileButton);
                searchButton = (ImageButton) findViewById(R.id.searchButton);
                noButton =  (Button) findViewById(R.id.noButoon);
                yesButton =  (Button) findViewById(R.id.yesButton);
                imageViewNoItems = (ImageView) findViewById(R.id.logo);
                noItemsMessage = (TextView) findViewById(R.id.noItemsErrorMessage);
                if (imageViewNoItems.getTag()!= "noItemsToShow") {
                  noButton.setVisibility(View.VISIBLE);
                  yesButton.setVisibility(View.VISIBLE);
                  noItemsMessage.setVisibility(View.INVISIBLE);
                  imageViewNoItems.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
                  public void onSwipeRight() {
                    Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                  }
                  public void onSwipeLeft() {
                    Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                  }
                  });
                }
                searchButton.setOnClickListener(this); // calling onClick() method
                profileButton.setOnClickListener(this);
                itemsButton.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.myProfileButton:
                        Log.d(TAG, "profile button pressed" );
                        myIntnet = new Intent(MainActivity.this ,EditProfile.class);
                        startActivity(myIntnet);
                        finish();
                        break;
                    case R.id.searchButton:
                        Log.d(TAG, "search button pressed" );
                        myIntnet = new Intent(MainActivity.this ,Search.class);
                        startActivity(myIntnet);
                        finish();
                        break;
                    case R.id.itemsButton:
                        Log.d(TAG, "items button pressed" );
                        myIntnet = new Intent(MainActivity.this ,Items.class);
                        startActivity(myIntnet);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        }


