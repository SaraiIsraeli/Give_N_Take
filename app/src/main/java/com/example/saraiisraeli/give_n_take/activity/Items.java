package com.example.saraiisraeli.give_n_take.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.saraiisraeli.give_n_take.R;
import com.example.saraiisraeli.give_n_take.models.AppData;
import com.example.saraiisraeli.give_n_take.models.Item;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

    public class Items extends AppCompatActivity implements View.OnClickListener,
            GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{


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
    private static final String TAG = "Items";
    public static final String KEY_User_Document1 = "doc1";
    Map<String, Object> itemValues;
    AppData mAppData = new AppData();
    String userToken = (mAppData.getCurrentUser().getUid());
    private String afterSaveMsg = "Save Succeed ";
    private Snackbar saveMsg;
    ImageView IDProf;
    Uri selectedImage;
    Button Upload_Btn, Choose_Btn;
    FirebaseStorage storage;
    StorageReference storageReference;
    private String Document_img1 = "";
    String userId;
    EditText m_itemName;
    EditText m_itemDesc;
    EditText m_location;
    Button m_addPhoto;
    Button m_historyItemsBtn;
    Button m_backBtn;
    CheckBox m_currentLocation;
    Intent myIntnet;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    private DatabaseReference dbRef;
    String m_itemNameStr, m_itemDecStr, m_locationStr;
    Boolean isCurrentLocationChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        Log.d(TAG, "entered items" );
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef=firebaseDatabase.getReference();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        userId=user.getUid();
        firebaseAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
            }
        };
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        IDProf = (ImageView) findViewById(R.id.IdProf);
        Upload_Btn = (Button) findViewById(R.id.UploadBtn);
        Choose_Btn = (Button) findViewById(R.id.ChooseBtn);
        m_historyItemsBtn = (Button)findViewById(R.id.HistoryBtn);
        m_currentLocation = (CheckBox)findViewById(R.id.LocationRB);
        m_location = (EditText)findViewById(R.id.LocationET);
        m_itemName = (EditText)findViewById(R.id.ItemName);
        m_itemDesc = (EditText)findViewById(R.id.ItemDesc);
        m_historyItemsBtn.setOnClickListener(this);
        IDProf.setOnClickListener(this);
        Choose_Btn.setOnClickListener(this);
        Upload_Btn.setOnClickListener(this);
        m_currentLocation.setOnClickListener(this);
        m_currentLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                      m_location.setEnabled(false);
                    isCurrentLocationChecked = true;
                 //   m_location.setInputType(InputType.TYPE_NULL);
                   // m_location.setFocusableInTouchMode(false);
                } else {
                    isCurrentLocationChecked = false;
                    m_location.setText("");
                    m_location.setEnabled(true);
                    m_location.setFocusableInTouchMode(true);
                }
            }
        });
        // location
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
}


        @Override
        public void onPause()
        {
            super.onPause();
            ReturnToMain();
        }

        private void ReturnToMain()
        {
            Log.d(TAG, "Start Method: ReturnToMain");
            Intent myIntent = new Intent(Items.this, MainActivity.class);
            startActivity(myIntent);
            finish();
        }

    private void getItemDetails()
    {
        Log.d(TAG,"get item details");
        m_itemDecStr = m_itemDesc.getText().toString();
        m_itemNameStr = m_itemName.getText().toString();
        if (isCurrentLocationChecked == true){
            getCurrentLocation();
        }
        else{
            m_locationStr = m_location.getText().toString();
        }
    }

    private void getCurrentLocation()
    {
        checkLocation(); //check whether location service is enable or not in your  phone
        //startLocationUpdates();
    }
// test 
    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.HistoryBtn: {
                Log.d(TAG, "HistoryItems button pressed");
                myIntnet = new Intent(Items.this, HistoryItems.class);
                startActivity(myIntnet);
                finish();
                break;
            }
            case R.id.UploadBtn:{
                Log.d(TAG, "Uploading image");
                uploadImage();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getItemDetails();
                saveItemToDB();
                uploadItem();
                break;
            }
            case R.id.ChooseBtn:{
                Log.d(TAG,"Select image");
                selectImage();
                break;
            }
            case R.id.IdProf:{
                Log.d(TAG,"Select image -IDProf");
                selectImage();
                break;
            }
            case R.id.LocationRB:{
                //  setLocationBoolean();
                break;
            }

        }
    }

    private void setLocationBoolean() {
        if(isCurrentLocationChecked == false)
        {
            isCurrentLocationChecked = true;
        }
        else{
            isCurrentLocationChecked = false;
        }
    }

    private void saveItemToDB()
    {
        Log.d(TAG, "Start Method: saveItemToDB");
        //validate Distance field contains a valid value
        //save the settings to DB
        boolean itemName = true;
        boolean itemDesc  = true;
        boolean itemLocation = true;
        if(itemName && itemDesc && itemLocation)
        {
            try
            {
                Item item = new Item(m_itemNameStr,m_locationStr,m_itemDecStr);
                itemValues = item.ItemToMap();
                if (!itemValues.isEmpty())
                {
                    mAppData.SavetNewItem(itemValues,userId);
                    saveMsg.show();
                    Log.d(TAG, "End Method: saveItemToDB");
                }

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private void uploadItem() {
        myIntnet = new Intent(Items.this, MainActivity.class);
        startActivity(myIntnet);
        finish();
    }

    private Boolean validateFields() {
        boolean isValid = true;
        return isValid;
    }

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo:");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == 1) {
                    File f = new File(Environment.getExternalStorageDirectory().toString());
                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals("temp.jpg")) {
                            f = temp;
                            break;
                        }
                    }
                    try {
                        Bitmap bitmap;
                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                        bitmap=getResizedBitmap(bitmap, 400);
                        IDProf.setImageBitmap(bitmap);
                        BitMapToString(bitmap);
                        String path = android.os.Environment
                                .getExternalStorageDirectory()
                                + File.separator
                                + "Phoenix" + File.separator + "default";
                        f.delete();
                        OutputStream outFile = null;
                        File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                        try {
                            outFile = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                            outFile.flush();
                            outFile.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == 2) {
                    Bitmap bitmap = null;
                    selectedImage = data.getData();
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bitmap=getResizedBitmap(bitmap, 400);
                    Log.w("path of image..*****...", picturePath+"");
                    IDProf.setImageBitmap(bitmap);
                    BitMapToString(bitmap);
                }
            }
        }
        public String BitMapToString(Bitmap userImage1) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
            byte[] b = baos.toByteArray();
            Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
            return Document_img1;
        }

        public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
            int width = image.getWidth();
            int height = image.getHeight();

            float bitmapRatio = (float)width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }
            return Bitmap.createScaledBitmap(image, width, height, true);
        }

        @Override
        public void onPointerCaptureChanged(boolean hasCapture) {

        }

        private void uploadImage() {

            if(selectedImage != null)
            {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                StorageReference ref = storageReference.child("images"+ UUID.randomUUID().toString());
                ref.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(Items.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(Items.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded "+(int)progress+"%");
                            }
                        });
            }
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
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();
                // Fetch the address lines usng getAddressLine,
                // join them, and send them to the thread.
                for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                if (isCurrentLocationChecked) {
                    m_locationStr = addressFragments.get(0);
                   m_location.setText(m_locationStr);
               //     float [] result = new float[1];
                //    List<Address> inputAddressFragments;
                 //   inputAddressFragments = geocoder.getFromLocationName(item.getItemName(),2);
                  //  Location.distanceBetween(address.getLatitude(),address.getLongitude(),
                   //         inputAddressFragments.get(0).getLatitude(),inputAddressFragments.get(0).getLongitude(),result);
                    //Log.i(TAG,"Found address , distance = "  +  result[0]/1000);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String msg = "Updated Location: " +
                    Double.toString(location.getLatitude()) + "," +
                    Double.toString(location.getLongitude());
            // You can now create a LatLng Object for use with maps
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
        public void getItem (Map<String, Object> itemValues){
        Item item = new Item();
        item.setItemLocation(itemValues.get("itemLocation").toString());
        item.setItemName(itemValues.get("itemName").toString());
        item.setItemMoreInfo(itemValues.get("itemDescription").toString());
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

    }
