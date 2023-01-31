package com.example.smart_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

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
    private Switch LED1;
    private Switch LED2;
    private Switch FAN;
    private Switch DOOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        initializeUI();

    }

    private void initializeUI()
    {
        LED1 = findViewById(R.id.switch1);
        LED2 = findViewById(R.id.switch2);
        FAN = findViewById(R.id.switch3);
        DOOR = findViewById(R.id.switch4);

        LED1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    post("http://192.168.227.56/?led_on", new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {}
                    });
                }
                else
                {
                    post("http://192.168.227.56/?led_off", new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {}
                    });
                }
            }
        });
        LED2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    post("http://192.168.227.56/?led2_on", new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {}
                    });
                }
                else
                {
                    post("http://192.168.227.56/?led2_off", new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {}
                    });
                }
            }
        });
        FAN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    post("http://192.168.227.56/?fan_on", new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {}
                    });
                }
                else
                {
                    post("http://192.168.227.56/?fan_off", new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {}
                    });
                }
            }
        });
        DOOR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    post("http://192.168.227.56/?door_lock", new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {}
                    });
                }
                else
                {
                    post("http://192.168.227.56/?door_unlock", new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {}
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {}
                    });
                }
            }
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

    public void goBackToListA(View v)
    {
        Intent intent = new Intent(RoomActivity.this, ListRoomAppliance.class);
        startActivity(intent);
    }

}





