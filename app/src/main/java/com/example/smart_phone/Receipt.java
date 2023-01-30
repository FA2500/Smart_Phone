package com.example.smart_phone;

import androidx.appcompat.app.AppCompatActivity;

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

        Log.d("STATUS",status);
        Log.d("QUERY",query);
    }

    public void goToProfile(View v)
    {
        Intent intent = new Intent(Receipt.this, ProfileActivity.class);
        startActivity(intent);

    }
}