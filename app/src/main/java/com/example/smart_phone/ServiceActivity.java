package com.example.smart_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);


        initializeUI();
    }

    public void foodOnclick(View view) {
        Intent intent = new Intent(this, ServiceFood.class);
        startActivity(intent);
    }

    private void initializeUI()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.service);

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