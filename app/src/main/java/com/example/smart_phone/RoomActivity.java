package com.example.smart_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class RoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        initializeUI();
    }

    private void initializeUI()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.room);

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
                        Log.d("NAV","ROOM");
                        return true;
                    case R.id.service:
                        startActivity(new Intent(getApplicationContext(),ServiceActivity.class));
                        overridePendingTransition(0,0);
                        Log.d("NAV","SERVICE");
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        Log.d("NAV","PROFILE");
                        return true;
                }
                return false;
            }
        });
    }
}