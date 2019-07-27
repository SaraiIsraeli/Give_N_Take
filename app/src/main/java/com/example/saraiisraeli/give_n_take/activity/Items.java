package com.example.saraiisraeli.give_n_take.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
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

import com.example.saraiisraeli.give_n_take.R;
import com.example.saraiisraeli.give_n_take.models.AppData;
import com.example.saraiisraeli.give_n_take.models.Item;
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
import java.util.Map;
import java.util.UUID;

public class Items extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Items";

    public static final String KEY_User_Document1 = "doc1";
    Map<String, Object> itemValues;
    AppData mAppData = new AppData();
    private String afterSaveMsg = "Save Succeed ";
    private Snackbar saveMsg;
    ImageView IDProf;
    Uri selectedImage;
    Button Upload_Btn, Choose_Btn;
    FirebaseStorage storage;
    StorageReference storageReference;

    private String Document_img1 = "";
    String userId,nameStr,descStr,locationStr;
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
    Boolean m_ischecked;

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
        m_currentLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    m_location.setEnabled(true);
                    m_location.setInputType(InputType.TYPE_NULL);
                    m_location.setFocusableInTouchMode(false);
                    m_location.setVisibility(View.INVISIBLE);
                    m_ischecked = isChecked;
                } else {
                    m_location.setEnabled(false);
                    m_location.setFocusableInTouchMode(true);
                    m_location.setVisibility(View.VISIBLE);
                    m_ischecked = isChecked;
                }
            }
        });
}

    private void getItemDetails()
    {
        Log.d(TAG,"get item details");
        m_itemDecStr = m_itemDesc.getText().toString();
        m_itemNameStr = m_itemName.getText().toString();
        m_locationStr = m_location.getText().toString();

        }

    private String getCurrentLocation()
    {
      String location = "";

      return location;
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
                Item item = new Item(m_itemNameStr,m_itemDecStr,m_locationStr);
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
    }
