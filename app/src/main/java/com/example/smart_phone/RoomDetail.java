package com.example.smart_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;

public class RoomDetail extends AppCompatActivity {

    private String ID;

    private Button backBtn;
    private Button bookBtn;

    private TextView roomNameTV;
    private TextView roomCapTV;
    private TextView roomTypeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        Bundle bundle = getIntent().getExtras();
        ID = bundle.getString("ID");

        initUI();
        getData();
    }

    void initUI()
    {
        roomNameTV = findViewById(R.id.dRoomNameTV);
        roomCapTV = findViewById(R.id.dRoomCapTV);
        roomTypeTV = findViewById(R.id.dRoomTypeTV);

        backBtn = findViewById(R.id.dBackButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomDetail.this, BookingActivity.class);
                startActivity(intent);
            }
        });
        bookBtn = findViewById(R.id.dBookButton);
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomDetail.this, BillPlz.class);
                intent.putExtra("ID",ID);
                startActivity(intent);
            }
        });
    }

    void getData()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        db.collection("rooms")
                .document(ID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        Log.d("TEST",doc.getData().toString());

                        roomNameTV.setText(doc.get("roomNo").toString());
                        roomCapTV.setText(doc.get("roomCapA").toString() + " Adults");
                        roomTypeTV.setText(doc.get("roomType").toString());

                    }
                });
    }
}