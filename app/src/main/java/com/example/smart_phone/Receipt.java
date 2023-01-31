package com.example.smart_phone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Receipt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        Bundle extras = getIntent().getExtras();
        String status = extras.getString("status");
        String query = extras.getString("query");

        int reqCode = 1;
        Intent intent = new Intent(getApplicationContext(), Splash_screen.class);
        Splash_screen ss = new Splash_screen();
        ss.showNotification(this, "Successfuly Payment", "You have successfully pay for your Hotel Booking", intent, reqCode);

        Log.d("STATUS",status);
        Log.d("QUERY",query);
    }

    public void goToProfile(View v)
    {
        Intent intent = new Intent(Receipt.this, ProfileActivity.class);
        startActivity(intent);

    }
}