package com.example.smart_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Map;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout layout;
    private LinearLayout.LayoutParams layoutParams;

    private LinearLayout newLayout;
    private LinearLayout.LayoutParams newLayoutParams;

    private EditText searchET;
    private String searchTypeSet = "roomNo";

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Uri temp;
    private Image img;

    /*private ImageView roomImgIV;
    private TextView roomNameTV;
    private TextView roomTypeTV;
    private TextView roomCapTV;*/

    private int NoRow = 0;
    private int NoLinearVer = 0;
    private int NoRoom = 0;
    private int NoChild = 0;

    private int cardID = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        initializeUI();
        getData();
    }

    @Override
    public void onClick(View v)
    {
        Log.d("TEST",v.getId() + "");
        Log.d("TEST",v.getContentDescription() + "");

        Intent intent = new Intent(this, RoomDetail.class);
        intent.putExtra("ID",v.getContentDescription().toString());
        startActivity(intent);
        /*for(int i = 1000 ; i < 1012 ; i++)
        {
            if(v.getId() == i)
            {
                ImageButton b = (ImageButton)v;
                //Intent intent = new Intent(this, welcomeActivity.class);
                int idTV = (int)b.getId() - 1000;
                idTV = idTV + 10000;
                TextView TV = findViewById(idTV);
                Log.d("TV","RECEIVE ID = "+idTV);
                String name = TV.getText().toString();
                String username = b.getContentDescription().toString();
                //String name = b.getId();
                //String name = (String) b.getText();
                //intent.putExtra("name",name);
                //intent.putExtra("username",username);
                //startActivity(intent);
                //Log.d("TEST ID","ID = "+v.getId());
                //createDistrictButton(StateM[i]);
            }
        }*/
    }

    private void initializeUI()
    {


        layout = findViewById(R.id.BLL01);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        searchET = findViewById(R.id.searchET);
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(searchET.getText().length() == 0 || searchET.getText() == null)
                {
                    cardID = 1000;
                    layout.removeAllViews();
                    getData();
                }
                else
                {
                    cardID = 1000;
                    layout.removeAllViews();
                    getDataWithSearch(searchET.getText().toString());
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.booking);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.booking:
                        Log.d("NAV","BOOKING");
                        return true;
                    case R.id.room:
                        //startActivity(new Intent(getApplicationContext(),RoomActivity.class));
                        startActivity(new Intent(getApplicationContext(),ListRoomAppliance.class));
                        overridePendingTransition(0,0);
                        Log.d("NAV","ROOM");
                        return true;
                    case R.id.service:
                        //startActivity(new Intent(getApplicationContext(),ServiceActivity.class));
                        startActivity(new Intent(getApplicationContext(),ListRoomServices.class));
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
    }

    private void getData()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        db.collection("rooms")
                .whereEqualTo("isBooked",false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                try {
                                    NoRoom++;
                                    createButton(document.getData(), document.getId());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.d("TEsT",document.getData().toString());

                            }
                        }
                    }
                });
    }

    private void getDataWithSearch(String searchValue)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        db.collection("rooms")
                .whereEqualTo("isBooked",false)
                .whereEqualTo(searchTypeSet, searchValue)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                try {
                                    NoRoom++;
                                    createButton(document.getData(), document.getId());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.d("TEsT",document.getData().toString());

                            }
                        }
                    }
                });
    }

    private void createButton(Map<String, Object> data, String ID) throws IOException
    {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Get Image First
        ImageButton img = new ImageButton(this);

        if(data.get("roomType").toString().equals("Deluxe"))
        {
            //storageReference.child("Merchant/"+name+"/logo")
            storageReference.child("Room/"+"test"+"/roompic.jfif")
                    .getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            MaterialCardView card = new MaterialCardView(BookingActivity.this);
                            card.setBackgroundColor(Color.CYAN);
                            card.setStrokeColor(Color.BLACK);
                            card.setStrokeWidth(5);
                            card.setId(cardID);
                            card.setOnClickListener(BookingActivity.this);
                            card.setContentDescription(ID);
                            layout.addView(card);

                            cardID++;

                            temp = uri;
                            Picasso.get()
                                    .load(temp)
                                    .resize(350,255)
                                    .into(img);
                            //img.setOnClickListener(BookingActivity.this);
                            //img.setId(buttonid);
                            img.setContentDescription("TEST");

                            LinearLayout LL = new LinearLayout(BookingActivity.this);
                            LL.setOrientation(LinearLayout.VERTICAL);




                            //Room Number
                            TextView NTV = new TextView(BookingActivity.this);
                            NTV.setText(data.get("roomNo").toString());
                            NTV.setTextSize(20);

                            //Room Type
                            TextView TTV = new TextView(BookingActivity.this);
                            TTV.setText(data.get("roomType").toString());
                            TTV.setTextSize(20);

                            //Room Size
                            int adult = Integer.parseInt(data.get("roomCapA").toString()) ;
                            int child = Integer.parseInt(data.get("roomCapC").toString()) ;
                            String stx = "";

                            if(adult > 0)
                            {
                                stx = adult + " Adults";
                            }
                            if(child > 0)
                            {
                                stx = stx + " " + child + " Child";
                            }

                            TextView STV = new TextView(BookingActivity.this);
                            STV.setText(stx);
                            STV.setTextSize(20);

                            STV.setTypeface(Typeface.SERIF, Typeface.BOLD);


                            newLayout = new LinearLayout(BookingActivity.this);
                            //newLayout.setId((layoutid));
                            newLayout.setOrientation(LinearLayout.HORIZONTAL);
                            card.addView(newLayout , layoutParams);

                            newLayout.addView(img,0);
                            newLayout.addView(LL,1);
                            LL.addView(NTV,0 );
                            LL.addView(TTV,1);
                            LL.addView(STV,2);

                            NoChild++;
                            Log.d("ADDED","CHILD");

                        }
                    });
        }
        else if(data.get("roomType").toString().equals("Superior"))
        {
            //storageReference.child("Merchant/"+name+"/logo")
            storageReference.child("Room/"+"test"+"/roompic2.jpeg")
                    .getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            MaterialCardView card = new MaterialCardView(BookingActivity.this);
                            card.setBackgroundColor(Color.CYAN);
                            card.setStrokeColor(Color.BLACK);
                            card.setStrokeWidth(5);
                            card.setId(cardID);
                            card.setOnClickListener(BookingActivity.this);
                            card.setContentDescription(ID);
                            layout.addView(card);

                            cardID++;

                            temp = uri;
                            Picasso.get()
                                    .load(temp)
                                    .resize(350,255)
                                    .into(img);
                            //img.setOnClickListener(BookingActivity.this);
                            //img.setId(buttonid);
                            img.setContentDescription("TEST");

                            LinearLayout LL = new LinearLayout(BookingActivity.this);
                            LL.setOrientation(LinearLayout.VERTICAL);




                            //Room Number
                            TextView NTV = new TextView(BookingActivity.this);
                            NTV.setText(data.get("roomNo").toString());
                            NTV.setTextSize(20);

                            //Room Type
                            TextView TTV = new TextView(BookingActivity.this);
                            TTV.setText(data.get("roomType").toString());
                            TTV.setTextSize(20);

                            //Room Size
                            int adult = Integer.parseInt(data.get("roomCapA").toString()) ;
                            int child = Integer.parseInt(data.get("roomCapC").toString()) ;
                            String stx = "";

                            if(adult > 0)
                            {
                                stx = adult + " Adults";
                            }
                            if(child > 0)
                            {
                                stx = stx + " " + child + " Child";
                            }

                            TextView STV = new TextView(BookingActivity.this);
                            STV.setText(stx);
                            STV.setTextSize(20);

                            STV.setTypeface(Typeface.SERIF, Typeface.BOLD);


                            newLayout = new LinearLayout(BookingActivity.this);
                            //newLayout.setId((layoutid));
                            newLayout.setOrientation(LinearLayout.HORIZONTAL);
                            card.addView(newLayout , layoutParams);

                            newLayout.addView(img,0);
                            newLayout.addView(LL,1);
                            LL.addView(NTV,0 );
                            LL.addView(TTV,1);
                            LL.addView(STV,2);

                            NoChild++;
                            Log.d("ADDED","CHILD");

                        }
                    });
        }


    }

    public void setFacetFilter(View v)
    {
        new AlertDialog.Builder(this)
                .setTitle("Filtering Option")
                .setMessage("Choose Filtering Options that you want to filter")
                .setPositiveButton("Room Name", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        searchTypeSet = "roomNo";
                    }
                })
                .setNegativeButton("Room Type", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchTypeSet = "roomType";
                    }
                })
                .setNeutralButton("Remove Filter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        searchTypeSet = "";
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}