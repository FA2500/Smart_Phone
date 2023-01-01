package com.example.smart_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;

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

    private void getData()
    {
        nameTV.setText(userInfo.name);
        emailTV.setText(userInfo.email);
        //registerTV.setText(sdf.format(userInfo.registeredTime));
       // Log.d("TEST", userInfo.registeredTime.toString());
    }
}