package com.example.smart_phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;




public class RoomActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();
    private Switch LED1;
    private Switch LED2;
    private Switch FAN;
    private Switch DOOR;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://smart-hotel-d3cca-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference myRef = database.getReference("sensor");

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
                Map a = new HashMap();
                a.put("LED1", isChecked);
                myRef.updateChildren(a);
            }
        });
        LED2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Map a = new HashMap();
                a.put("LED2",isChecked);
                myRef.updateChildren(a);
            }
        });
        FAN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Map a = new HashMap();
                a.put("FANN",isChecked);
                myRef.updateChildren(a);
            }
        });
        DOOR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Map a = new HashMap();
                a.put("DOOR",!isChecked);
                myRef.updateChildren(a);
            }
        });
    }

    public void goBackToListA(View v)
    {
        Intent intent = new Intent(RoomActivity.this, ListRoomAppliance.class);
        startActivity(intent);
    }

}





