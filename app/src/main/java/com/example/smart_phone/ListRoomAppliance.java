package com.example.smart_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListRoomAppliance extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout LL;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private int cardID = 1000;
    private Uri temp;

    private LinearLayout newLayout;
    private LinearLayout.LayoutParams newLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room_appliance);

        initUI();
        getData();
    }

    private void initUI()
    {
        LL = findViewById(R.id.applianceRoomList);
        newLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

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
                        //startActivity(new Intent(getApplicationContext(),RoomActivity.class));
                        Log.d("NAV","ROOM");
                        return true;
                    case R.id.service:
                        startActivity(new Intent(getApplicationContext(),ListRoomServices.class));
                        overridePendingTransition(0,0);
                        //startActivity(new Intent(getApplicationContext(),ServiceActivity.class));
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

    @Override
    public void onClick(View v)
    {
        Log.d("TEST ID", String.valueOf(v.getId()));
        if(Integer.parseInt(String.valueOf(v.getId())) >= 1000)
        {
            Intent intent = new Intent(ListRoomAppliance.this,RoomActivity.class );
            startActivity(intent);
        }
    }

    private void getData()
    {
        ArrayList<String> roomIDs = new ArrayList<String>();

        db.collection("users/"+userInfo.getUID()+"/access")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot docs : task.getResult())
                            {
                                roomIDs.add(docs.getId());
                                createButton(docs);
                            }
                        }
                        db.collection("users/"+userInfo.getUID()+"/booking")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful())
                                        {
                                            for(QueryDocumentSnapshot docs : task.getResult())
                                            {
                                                roomIDs.add(docs.getId());
                                                createButton(docs);
                                            }

                                        }
                                        Log.d("ROOMID",roomIDs.toString());
                                    }
                                });
                    }
                });
    }

    private void createButton(QueryDocumentSnapshot doc)
    {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Get Image First
        ImageButton img = new ImageButton(this);

        db.collection("rooms").document(doc.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot docs =  task.getResult();

                            //storageReference.child("Merchant/"+name+"/logo")
                            storageReference.child("Room/"+"test"+"/roompic.jfif")
                                    .getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            MaterialCardView card = new MaterialCardView(ListRoomAppliance.this);
                                            card.setBackgroundColor(Color.CYAN);
                                            card.setStrokeColor(Color.BLACK);
                                            card.setStrokeWidth(5);
                                            card.setId(cardID);
                                            card.setOnClickListener(ListRoomAppliance.this);
                                            card.setContentDescription(docs.getId());
                                            card.setTooltipText(docs.get("roomNo").toString());
                                            LL.addView(card);

                                            cardID++;

                                            temp = uri;
                                            Picasso.get()
                                                    .load(temp)
                                                    .resize(300,200)
                                                    .into(img);
                                            //img.setOnClickListener(BookingActivity.this);
                                            //img.setId(buttonid);
                                            img.setContentDescription("TEST");

                                            LinearLayout LL = new LinearLayout(ListRoomAppliance.this);
                                            LL.setOrientation(LinearLayout.VERTICAL);

                                            //Room Number
                                            TextView NTV = new TextView(ListRoomAppliance.this);
                                            NTV.setText(docs.get("roomNo").toString());
                                            NTV.setTextSize(20);


                                            newLayout = new LinearLayout(ListRoomAppliance.this);
                                            newLayout.setOrientation(LinearLayout.HORIZONTAL);
                                            card.addView(newLayout , newLayoutParams);

                                            newLayout.addView(img,0);
                                            newLayout.addView(LL,1);
                                            LL.addView(NTV,0 );


                                        }
                                    });
                        }

                    }
                });
    }
}