package com.example.smart_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ServiceFood extends AppCompatActivity {

    private CheckBox breakfast;
    private CheckBox lunch;
    private CheckBox Dinner;

    private ImageView breakfastIV;
    private ImageView lunchIV;
    private ImageView dinnerIV;

    private String roomID;
    private String foodtype = "";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_food);

        Bundle bundle = getIntent().getExtras();
        roomID = bundle.getString("roomID");

        initUI();
    }

    private void initUI()
    {
        breakfast = findViewById(R.id.cb_breakfast);
        lunch = findViewById(R.id.cb_lunch);
        Dinner = findViewById(R.id.cb_dinner);

        breakfastIV = findViewById(R.id.imageView3);
        lunchIV = findViewById(R.id.imageView7);
        dinnerIV = findViewById(R.id.imageView8);

        breakfast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    lunch.setChecked(false);
                    Dinner.setChecked(false);
                }
            }
        });

        lunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    breakfast.setChecked(false);
                    Dinner.setChecked(false);
                }

            }
        });

        Dinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    breakfast.setChecked(false);
                    lunch.setChecked(false);
                }
            }
        });
    }

    public void listBreakfast(View v)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LinearLayout ll1= new LinearLayout(this);
        ll1.setOrientation(LinearLayout.VERTICAL);
        final TextView text1 = new TextView(this);
        final TextView text2 = new TextView(this);
        final TextView emptyTV = new TextView(this);

        emptyTV.setTextSize(20);

        text1.setText("Pancake with Butter");
        text1.setTextSize(20);
        text1.setTextColor(Color.BLACK);
        text1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        text2.setText("Hot Tea");
        text2.setTextSize(20);
        text2.setTextColor(Color.BLACK);
        text2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        ll1.addView(emptyTV);
        ll1.addView(text1);
        ll1.addView(text2);


        alert
                .setTitle("Breakfast Food List")
                .setView(ll1)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish
                    }
                }).show();
    }

    public void listLunch(View v)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LinearLayout ll1= new LinearLayout(this);
        ll1.setOrientation(LinearLayout.VERTICAL);
        final TextView text1 = new TextView(this);
        final TextView text2 = new TextView(this);
        final TextView emptyTV = new TextView(this);

        emptyTV.setTextSize(20);

        text1.setText("Ayam Gepok");
        text1.setTextSize(20);
        text1.setTextColor(Color.BLACK);
        text1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        text2.setText("Hot Coffee");
        text2.setTextSize(20);
        text2.setTextColor(Color.BLACK);
        text2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        ll1.addView(emptyTV);
        ll1.addView(text1);
        ll1.addView(text2);


        alert
                .setTitle("Lunch Food List")
                .setView(ll1)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish
                    }
                }).show();
    }

    public void listDinner(View v)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LinearLayout ll1= new LinearLayout(this);
        ll1.setOrientation(LinearLayout.VERTICAL);
        final TextView text1 = new TextView(this);
        final TextView text2 = new TextView(this);
        final TextView emptyTV = new TextView(this);

        emptyTV.setTextSize(20);

        text1.setText("ABC");
        text1.setTextSize(20);
        text1.setTextColor(Color.BLACK);
        text1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        text2.setText("Watermelon");
        text2.setTextSize(20);
        text2.setTextColor(Color.BLACK);
        text2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        ll1.addView(emptyTV);
        ll1.addView(text1);
        ll1.addView(text2);


        alert
                .setTitle("Dinner Food List")
                .setView(ll1)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish
                    }
                }).show();
    }

    public void submitFood(View v)
    {

        if(breakfast.isChecked())
        {
            foodtype = "breakfast";
        }
        else if(lunch.isChecked())
        {
            foodtype = "lunch";
        }
        else if(Dinner.isChecked())
        {
            foodtype = "dinner";
        }
        else
        {
            foodtype = "dinner";
        }

        Map<String, Object> docData = new HashMap<>();
        docData.put("food",foodtype);
        docData.put("roomID",roomID);
        docData.put("userID",userInfo.getUID());
        docData.put("status","ordered");
        docData.put("userName",userInfo.getName());
        docData.put("addedOn", FieldValue.serverTimestamp());

        db.collection("food").document()
                .set(docData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ServiceFood.this, "You have successfully order your food", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ServiceFood.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                });

    }


}