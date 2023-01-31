package com.example.smart_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ServiceActivity extends AppCompatActivity {

    private View foodIV;
    private View cleanIV;

    private String roomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Bundle bundle = getIntent().getExtras();
        roomID = bundle.getString("roomID");

        initializeUI();
    }

    private void initializeUI()
    {
        foodIV = findViewById(R.id.button25);
        cleanIV = findViewById(R.id.button26);
    }

    public void goBackToListS(View v)
    {
        Intent intent = new Intent(ServiceActivity.this, ListRoomServices.class);
        intent.putExtra("roomID",roomID);
        startActivity(intent);
    }

    //nav
    public void goToFood(View v)
    {
        Intent intent = new Intent(ServiceActivity.this, ServiceFood.class);
        intent.putExtra("roomID",roomID);
        startActivity(intent);
    }

    public void goToClean(View v)
    {
        Intent intent = new Intent(ServiceActivity.this, ServiceClean.class);
        intent.putExtra("roomID",roomID);
        startActivity(intent);
    }
}