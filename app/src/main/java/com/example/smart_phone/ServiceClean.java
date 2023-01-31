package com.example.smart_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ServiceClean extends AppCompatActivity {

    private CheckBox dailyCB;
    private CheckBox extraCB;

    private ImageView dailyIV;
    private ImageView extraIV;

    private String roomID;
    private String cleantype;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_clean);

        Bundle bundle = getIntent().getExtras();
        roomID = bundle.getString("roomID");

        initUI();
    }

    private void initUI()
    {
        dailyCB = findViewById(R.id.cb_daily_cleaning);
        extraCB = findViewById(R.id.cb_extra_cleaning);

        dailyIV = findViewById(R.id.imageView6);
        extraIV = findViewById(R.id.imageView9);

        dailyCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    extraCB.setChecked(false);
                }
            }
        });

        extraCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    dailyCB.setChecked(false);
                }
            }
        });
    }

    public void listDaily(View v)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LinearLayout ll1= new LinearLayout(this);
        ll1.setOrientation(LinearLayout.VERTICAL);
        final TextView text1 = new TextView(this);
        final TextView text2 = new TextView(this);
        final TextView emptyTV = new TextView(this);

        emptyTV.setTextSize(20);

        text1.setText("Light Cleaning");
        text1.setTextSize(20);
        text1.setTextColor(Color.BLACK);
        text1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        text2.setText("30 Minutes");
        text2.setTextSize(20);
        text2.setTextColor(Color.BLACK);
        text2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        ll1.addView(emptyTV);
        ll1.addView(text1);
        ll1.addView(text2);


        alert
                .setTitle("Daily Cleaning Task")
                .setView(ll1)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish
                    }
                }).show();
    }

    public void listExtra(View v)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LinearLayout ll1= new LinearLayout(this);
        ll1.setOrientation(LinearLayout.VERTICAL);
        final TextView text1 = new TextView(this);
        final TextView text2 = new TextView(this);
        final TextView emptyTV = new TextView(this);

        emptyTV.setTextSize(20);

        text1.setText("Extra Cleaning");
        text1.setTextSize(20);
        text1.setTextColor(Color.BLACK);
        text1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        text2.setText("2 Hours");
        text2.setTextSize(20);
        text2.setTextColor(Color.BLACK);
        text2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        ll1.addView(emptyTV);
        ll1.addView(text1);
        ll1.addView(text2);


        alert
                .setTitle("Extra Cleaning Task")
                .setView(ll1)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish
                    }
                }).show();
    }

    public void submitClean(View v)
    {
        if(!(dailyCB.isChecked() && extraCB.isChecked()))
        {
            Toast.makeText(this, "Select any cleaning service to continue", Toast.LENGTH_SHORT).show();
            return;
        }

        if(dailyCB.isChecked())
        {
            cleantype = "daily";
        }
        else if(extraCB.isChecked())
        {
            cleantype = "extra";
        }

        Map<String, Object> docData = new HashMap<>();
        docData.put("clean",cleantype);
        docData.put("roomID",roomID);
        docData.put("userID",userInfo.getUID());
        docData.put("status","ordered");
        docData.put("userName",userInfo.getName());
        docData.put("addedOn", FieldValue.serverTimestamp());

        db.collection("clean").document()
                .set(docData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ServiceClean.this, "You have successfully order your cleaning service", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ServiceClean.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                });
    }
}