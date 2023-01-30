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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ProfileActivity extends AppCompatActivity {

    //TextView
    private TextView nameTV;
    private TextView emailTV;
    private TextView registerTV;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeUI();
        getData();
    }

    private void initializeUI()
    {
        nameTV = findViewById(R.id.proName);
        emailTV = findViewById(R.id.proEmail);
        registerTV = findViewById(R.id.proRegister);

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
                        Log.d("NAV","BOOKING");
                        return true;
                    case R.id.room:
                        startActivity(new Intent(getApplicationContext(),RoomActivity.class));
                        overridePendingTransition(0,0);
                        Log.d("NAV","ROOM");
                        return true;
                    case R.id.service:
                        startActivity(new Intent(getApplicationContext(),ServiceActivity.class));
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

    public void goToMyBooking(View v)
    {
        Intent intent = new Intent(ProfileActivity.this, ShareRoomAccess.class);
        startActivity(intent);
    }

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


    ActivityResultLauncher< ScanOptions > barLauncher = registerForActivityResult(new ScanContract(), result -> {
    if (result.getContents() != null) {
        if(result.getContents().contains("smarthoteld3cca"))
        {
            String GetID = result.getContents().substring(15);
            if(GetID.equals(userInfo.getUID()))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Error");
                builder.setMessage("You already have access to this room");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
            else
            {

            }


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
}