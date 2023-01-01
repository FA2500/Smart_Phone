package com.example.smart_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;




public class RoomActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();

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

        Button btnOnLed1 = findViewById(R.id.button4LED1);
        btnOnLed1.setOnClickListener(view -> {
            post("http://192.168.252.56/?led_on", new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {}
            });
        });
        Button btnOffLed1 = findViewById(R.id.button5LED1);
        btnOffLed1.setOnClickListener(view -> {
            post("http://192.168.252.56/?led_off", new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {}
            });
        });
        Button btnOnLed2 = findViewById(R.id.button6LED2);
        btnOnLed2.setOnClickListener(view -> {
            post("http://192.168.252.56/?led2_on", new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {}
            });
        });
        Button btnOffLed2 = findViewById(R.id.button7LED2);
        btnOffLed2.setOnClickListener(view -> {
            post("http://192.168.252.56/?led2_off", new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {}
            });
        });
        Button btnOnFan = findViewById(R.id.buttonFanOn);
        btnOnFan.setOnClickListener(view -> {
            post("http://192.168.252.56/?fan_on", new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {}
            });
        });
        Button btnOffFan = findViewById(R.id.buttonFanOff);
        btnOffFan.setOnClickListener(view -> {
            post("http://192.168.252.56/?fan_off", new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {}
            });
        });
        Button btnLockDoor = findViewById(R.id.buttonLock);
        btnLockDoor.setOnClickListener(view -> {
            post("http://192.168.252.56/?door_lock", new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {}
            });
        });
        Button btnUnlockDoor = findViewById(R.id.buttonUnlock);
        btnUnlockDoor.setOnClickListener(view -> {
            post("http://192.168.252.56/?door_unlock", new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {}
            });
        });
    }

    Call post(String url, Callback callback) {

        Request request = new Request.Builder()
                .url(url)

                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

}





