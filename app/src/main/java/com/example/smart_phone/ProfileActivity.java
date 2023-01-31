package com.example.smart_phone;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ProfileActivity extends AppCompatActivity {

    //TextView
    private TextView nameTV;
    private TextView emailTV;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Room List
    private ArrayList<String> roomList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeUI();
        getData();

        //test
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d("TOKEN", token);
                    }
                });
        //test
    }

    private void initializeUI()
    {
        nameTV = findViewById(R.id.proName);
        emailTV = findViewById(R.id.proEmail);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.booking:
                        startActivity(new Intent(getApplicationContext(),BookingActivity.class));
                        overridePendingTransition(0,0);
                        Log.d("NAV","PROFILE");
                        return true;
                    case R.id.room:
                        //startActivity(new Intent(getApplicationContext(),RoomActivity.class));
                        startActivity(new Intent(getApplicationContext(),ListRoomAppliance.class));
                        overridePendingTransition(0,0);
                        Log.d("NAV","ROOM");
                        return true;
                    case R.id.service:
                        //startActivity(new Intent(getApplicationContext(),ServiceActivity.class));
                        startActivity(new Intent(getApplicationContext(),ListRoomServices.class));
                        overridePendingTransition(0,0);
                        Log.d("NAV","SERVICE");
                        return true;
                    case R.id.profile:
                        Log.d("NAV","PROFILE");
                        return true;
                }
                return false;
            }
        });
    }

    //GET ROOM ACCESS
    public void getRoomAccess(View v)
    {
        new AlertDialog.Builder(this)
                .setTitle("Get Room Access")
                .setMessage("Select Method to get room Access?")
                .setPositiveButton("Show QR Code", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GETgenerateQR();
                    }
                })
                .setNegativeButton("Scan QR Code", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GETscanQR();
                    }
                })
                .setNeutralButton("Cancel Action", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //finish
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    //SEND ROOM ACCESS
    public void goToMyBooking(View v)
    {
        Intent intent = new Intent(ProfileActivity.this, ShareRoomAccess.class);
        startActivity(intent);
    }

    //PURCHASE HISTORY
    public void goToPurchaseHistory(View v)
    {
        Intent intent = new Intent(ProfileActivity.this, PurchaseHistory.class);
        startActivity(intent);
    }

    private void getData()
    {
        nameTV.setText(userInfo.getName());
        emailTV.setText(userInfo.getEmail());
        //registerTV.setText(sdf.format(userInfo.registeredTime));
       // Log.d("TEST", userInfo.registeredTime.toString());
    }

    private void GETgenerateQR()
    {
        Intent intent = new Intent(ProfileActivity.this, QRGenerator.class);
        intent.putExtra("UID",userInfo.getUID());
        intent.putExtra("type","GET");
        intent.putExtra("name",userInfo.getName());
        startActivity(intent);
    }

    private void GETscanQR()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 50);
        }
        Log.d("CAMERA PERM", String.valueOf(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)));
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureActivity.class);

        barLauncher.launch(options);
    }

    public void Logout(View v)
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class );
        startActivity(intent);
    }

    ActivityResultLauncher< ScanOptions > barLauncher = registerForActivityResult(new ScanContract(), result -> {
    if (result.getContents() != null) {
        if(result.getContents().contains("smarthoteld3cca"))
        {
            String GetID = result.getContents().substring(15);

            //get Data
            db.collection("users/"+userInfo.getUID()+"/booking")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot docs : task.getResult())
                                {
                                    roomList.add(docs.getId());
                                }
                            }
                            db.collection("users/"+userInfo.getUID()+"/access")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                boolean check = true;
                                                for(DocumentSnapshot docs : task.getResult())
                                                {
                                                    roomList.add(docs.getId());
                                                }
                                                for(int i = 0 ; i < roomList.size() ; i++)
                                                {
                                                    if(GetID.equals(roomList.get(i)))
                                                    {
                                                        check = false;
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                                                        builder.setTitle("Error");
                                                        builder.setMessage("You already have access to this room");
                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                            }
                                                        }).show();
                                                        break;
                                                    }
                                                    else
                                                    {
                                                        //test

                                                    }
                                                }
                                                if(check)
                                                {
                                                    writeDbData(GetID);
                                                }
                                            }

                                        }
                                    });
                        }
                    });
            //get Data
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle("Invalid QR Code");
            builder.setMessage("You can only scan HOBBS QR Code");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }

    }
});

    private void writeDbData(String GetID)
    {

        db.collection("rooms/"+GetID+"/access").document(userInfo.getUID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot doc = task.getResult();
                            ArrayList<String> dependantList = new ArrayList<String>();
                            if(doc.exists())
                            {
                                dependantList = (ArrayList<String>) doc.get("dependant");
                                //read first, then update

                            }
                            else
                            {

                            }
                            if(dependantList.isEmpty())
                            {
                                Map<String, Object> docData = new HashMap<>();
                                docData.put("dependant", Arrays.asList(userInfo.getUID()));

                                Map<String, Object> bookData = new HashMap<>();
                                docData.put("Owner",userInfo.getUID());
                                docData.put("AddedOn", FieldValue.serverTimestamp());

                                db.collection("rooms/"+GetID+"/access").document(userInfo.getUID())
                                        .set(docData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    db.collection("users/"+userInfo.getUID()+"/access").document(GetID)
                                                            .set(bookData)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                                                        startActivity(intent);
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }
                            else
                            {
                                Map<String, Object> bookData = new HashMap<>();

                                db.collection("rooms/"+GetID+"/access").document(userInfo.getUID())
                                        .update("dependant", FieldValue.arrayUnion(userInfo.getUID()))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    db.collection("users/"+userInfo.getUID()+"/access").document(GetID)
                                                            .set(bookData)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                                                        startActivity(intent);
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });






    }
}