package com.example.saraiisraeli.give_n_take.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.saraiisraeli.give_n_take.R;
import com.example.saraiisraeli.give_n_take.models.AppData;
import com.example.saraiisraeli.give_n_take.models.Item;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity<userToken> extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener
{
    /// location
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;
    private long FASTEST_INTERVAL = 1000;
    private LocationManager locationManager;
    //---------------------------------------------//
    protected Location lastLocation;
    private AddressResultReceiver resultReceiver;
    private static final String TAG = "activity main";
    Intent myIntnet;
    ImageButton itemsButton,profileButton,searchButton;
    Button yesButton,noButton;
    ImageView imageView;
    AppData mAppData = new AppData();
    List<Map<String, Object>> itemsValues = new ArrayList<>();
    List<Item> itemsList = new ArrayList<>();
    String userToken = (mAppData.getCurrentUser().getUid());
    Address address;
    int size,counter=0;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView itemsText;

    FirebaseStorage storage;
    StorageReference storageReference;

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        itemsButton = findViewById(R.id.itemsButton);
        profileButton = findViewById(R.id.myProfileButton);
        searchButton = findViewById(R.id.searchButton);
        imageView= findViewById(R.id.noItemsLogo);
        searchButton.setOnClickListener(this); // calling onClick() method
        profileButton.setOnClickListener(this);
        itemsButton.setOnClickListener(this);
        yesButton = findViewById(R.id.yesButton);
        noButton =findViewById(R.id.noButoon);
        itemsText = findViewById(R.id.noItemsErrorMessage);
        /*TextView searchText = findViewById(R.id.searchText);
        TextView itemHeadlineText = findViewById(R.id.itemsText);
        boolean shouldSeeSettings = shouldSeeSearchSettings();
        if(!shouldSeeSettings)
        {
            searchButton.setVisibility(View.INVISIBLE);
            searchText.setVisibility(View.INVISIBLE);
        }*/


        mAppData.getUserDistance(userToken,this);

        // location
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        imageView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
                public void onSwipeRight() {
                    //////// sms to the seller !!!!
                    Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                }
                public void onSwipeLeft() {
                    counter++;
                    if (counter >= size) {
                        itemsText.setText("לא נשארו עוד פריטים מתאימים".concat("\n").concat("עבורך ברגע זה"));
                        imageView.setImageResource(R.drawable.error3);
                    } else {
                        itemsText.setText(itemsList.get(counter).getItemName().concat(" ").concat(itemsList.get(counter).getItemMoreInfo()).
                                concat("\n").concat(itemsList.get(counter).getItemLocation()));
                        Glide.with(MainActivity.this)
                                .load(itemsList.get(counter).getPhotoStr())
                                .into(imageView);
                    }////// delete item from user items list !!!!
                    //Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void checkSwipes() {
        size = itemsList.size();
        if (itemsList.size()>0) {
            size=itemsList.size();
            itemsText.setText(itemsList.get(counter).getItemName().concat(" ").concat(itemsList.get(counter).getItemMoreInfo()).
                    concat("\n").concat(itemsList.get(counter).getItemLocation()));
            Glide.with(this)
                    .load(itemsList.get(counter).getPhotoStr())
                    .into(imageView);
        }
    }

    public void getDistance (String distanceToSearch){
        mAppData.getAllItems(itemsValues,this,distanceToSearch);
    }
    public void checkItemsToShow(List<Map<String, Object>> itemsValues,String distanceToSearch) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        float [] result = new float[1];
        List<Address> inputAddressFragments;

        for (Map<String,Object> itemsMap : itemsValues)
        {
            inputAddressFragments = geocoder.getFromLocationName(itemsMap.get("itemLocation").toString(),2);
            Location.distanceBetween(address.getLatitude(),address.getLongitude(),
                    inputAddressFragments.get(0).getLatitude(),inputAddressFragments.get(0).getLongitude(),result);
            Log.i(TAG,"Found address , distance = "  +  result[0]/1000);
            if (distanceToSearch != null) {
                int d = Integer.parseInt(distanceToSearch);
                if (result[0]/1000 <= d) {
                    Item item = new Item(itemsMap.get("itemName").toString(), itemsMap.get("itemLocation").toString(),
                            itemsMap.get("itemDescription").toString(), Uri.parse(String.valueOf(itemsMap.get("photoURL"))));
                    itemsList.add(item);
                }
            }
        }
        checkSwipes();
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
    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, lastLocation);
        startService(intent);
    }
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        } }

    @SuppressLint("MissingPermission")
    private void fetchAddressButtonHander(View view) {
        startIntentService();
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                       Location lastKnownLocation = location;
                       Log.i("location=",lastKnownLocation.toString());
                        // In some rare cases the location returned can be null
                        if (lastKnownLocation == null) {
                            return;
                        }

                        if (!Geocoder.isPresent()) {
                            Toast.makeText(MainActivity.this,
                                    "No gecoder available",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Start service and update UI to reflect new location
                        updateUI();
                    }
                });
    }

    private void updateUI() {
    }

    /// location
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //      return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {
            //mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //  mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
            address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();
            // Fetch the address lines usng getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

   /* private boolean shouldSeeSearchSettings()
    {
        AppData mAppData = new AppData();
        String userToken = (mAppData.getCurrentUser().getUid());
        String userRole = mAppData.getUserRole(userToken);
        return userRole != null  && !(userRole.contains("1"));

    }*/


}




