package com.example.smart_phone;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PurchaseHistory extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout myHistory;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Uri temp;
    private String publicRoomID;

    private int cardID = 1000;

    private LinearLayout newLayout;
    private LinearLayout.LayoutParams newLayoutParams;


    SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchasehistory);

        initUI();
        getData();

    }

    private void initUI()
    {
        myHistory = findViewById(R.id.historyPurchaseList);
        newLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onClick(View v)
    {
        Log.d("TEST ID", String.valueOf(v.getId()));
        if(Integer.parseInt(String.valueOf(v.getId())) >= 1000)
        {
            getHistory(v.getContentDescription().toString());
        }
    }

    private void getHistory(String roomID)
    {
        db.collection("rooms").document(roomID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists())
                            {
                                db.collection("users/"+userInfo.getUID()+"/booking").document(roomID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot docs = task.getResult();
                                                if(docs.exists())
                                                {
                                                    LinearLayout ll = new LinearLayout(PurchaseHistory.this);
                                                    ll.setOrientation(LinearLayout.VERTICAL);
                                                    TextView tv1 = new TextView(PurchaseHistory.this);
                                                    TextView tv2 = new TextView(PurchaseHistory.this);
                                                    TextView tv3 = new TextView(PurchaseHistory.this);

                                                    tv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                    tv2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                    tv3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                                    tv1.setText(doc.get("roomNo").toString());
                                                    tv2.setText(doc.get("roomType").toString());
                                                    Timestamp timestamp = (Timestamp)  docs.get("paidOn");
                                                    Date date = timestamp.toDate();
                                                    tv3.setText(date.toString());

                                                    ll.addView(tv1,0);
                                                    ll.addView(tv2,1);
                                                    ll.addView(tv3,2);

                                                    new AlertDialog.Builder(PurchaseHistory.this)
                                                            .setTitle("Booking History")
                                                            .setView(ll)
                                                            .setNeutralButton("Done", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    //finish
                                                                }
                                                            })

                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                            .show();

                                                    Log.d("TEST","OPEN HISTORY");
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    private void getData()
    {
        db.collection("users/"+userInfo.getUID()+"/booking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            QuerySnapshot docs = task.getResult();
                            if(docs.isEmpty())
                            {
                                TextView TV = new TextView(PurchaseHistory.this);
                                TV.setText("You didn't purchase any room yet");
                                myHistory.addView(TV);
                            }
                            for(QueryDocumentSnapshot doc : task.getResult())
                            {
                                createButton(doc);
                            }
                        }
                        else
                        {
                            Toast.makeText(PurchaseHistory.this, "Something's wrong, please try again later.", Toast.LENGTH_SHORT).show();
                        }
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

                            if(docs.get("roomType").toString().equals("Deluxe"))
                            {
                                //storageReference.child("Merchant/"+name+"/logo")
                                storageReference.child("Room/"+"test"+"/roompic.jfif")
                                        .getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                MaterialCardView card = new MaterialCardView(PurchaseHistory.this);
                                                card.setBackgroundColor(Color.CYAN);
                                                card.setStrokeColor(Color.BLACK);
                                                card.setStrokeWidth(5);
                                                card.setId(cardID);
                                                card.setOnClickListener(PurchaseHistory.this);
                                                card.setContentDescription(docs.getId());
                                                myHistory.addView(card);

                                                cardID++;

                                                temp = uri;
                                                Picasso.get()
                                                        .load(temp)
                                                        .resize(350,255)
                                                        .into(img);
                                                //img.setOnClickListener(BookingActivity.this);
                                                //img.setId(buttonid);
                                                img.setContentDescription("TEST");

                                                LinearLayout LL = new LinearLayout(PurchaseHistory.this);
                                                LL.setOrientation(LinearLayout.VERTICAL);

                                                //Room Number
                                                TextView NTV = new TextView(PurchaseHistory.this);
                                                NTV.setText(docs.get("roomNo").toString());
                                                NTV.setTextSize(20);

                                                newLayout = new LinearLayout(PurchaseHistory.this);
                                                //newLayout.setId((layoutid));
                                                newLayout.setOrientation(LinearLayout.HORIZONTAL);
                                                card.addView(newLayout , newLayoutParams);

                                                newLayout.addView(img,0);
                                                newLayout.addView(LL,1);
                                                LL.addView(NTV,0 );
                                            }
                                        });
                            }
                            else if(docs.get("roomType").toString().equals("Superior"))
                            {
                                //storageReference.child("Merchant/"+name+"/logo")
                                storageReference.child("Room/"+"test"+"/roompic2.jpeg")
                                        .getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                MaterialCardView card = new MaterialCardView(PurchaseHistory.this);
                                                card.setBackgroundColor(Color.CYAN);
                                                card.setStrokeColor(Color.BLACK);
                                                card.setStrokeWidth(5);
                                                card.setId(cardID);
                                                card.setOnClickListener(PurchaseHistory.this);
                                                card.setContentDescription(docs.getId());
                                                myHistory.addView(card);

                                                cardID++;

                                                temp = uri;
                                                Picasso.get()
                                                        .load(temp)
                                                        .resize(350,255)
                                                        .into(img);
                                                //img.setOnClickListener(BookingActivity.this);
                                                //img.setId(buttonid);
                                                img.setContentDescription("TEST");

                                                LinearLayout LL = new LinearLayout(PurchaseHistory.this);
                                                LL.setOrientation(LinearLayout.VERTICAL);

                                                //Room Number
                                                TextView NTV = new TextView(PurchaseHistory.this);
                                                NTV.setText(docs.get("roomNo").toString());
                                                NTV.setTextSize(20);

                                                newLayout = new LinearLayout(PurchaseHistory.this);
                                                //newLayout.setId((layoutid));
                                                newLayout.setOrientation(LinearLayout.HORIZONTAL);
                                                card.addView(newLayout , newLayoutParams);

                                                newLayout.addView(img,0);
                                                newLayout.addView(LL,1);
                                                LL.addView(NTV,0 );



                                                Log.d("ADDED","CHILD");

                                            }
                                        });
                            }
                        }

                    }
                });
    }





    private void getDbData(String userID)
    {
        Log.d("USER ID",userID);
        Log.d("ROOM ID",publicRoomID);
        db.collection("rooms/"+publicRoomID+"/access").document(userInfo.getUID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot doc = task.getResult();
                            ArrayList<String> dependantList = new ArrayList<String>();
                            if(doc.exists())
                            {
                                dependantList = (ArrayList<String>) doc.get("dependant");
                                //read first, then update
                                writeDbData(userID,dependantList);
                            }
                            else
                            {
                                writeDbData(userID,dependantList);
                            }
                        }
                    }
                });
    }

    private void writeDbData(String userID, ArrayList<String> dependantList)
    {

        if(dependantList.isEmpty())
        {
            Map<String, Object> docData = new HashMap<>();
            docData.put("dependant", Arrays.asList(userID));

            Map<String, Object> bookData = new HashMap<>();
            docData.put("Owner",userInfo.getUID());
            docData.put("AddedOn", FieldValue.serverTimestamp());

            db.collection("rooms/"+publicRoomID+"/access").document(userInfo.getUID())
                    .set(docData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                db.collection("users/"+userID+"/access").document(publicRoomID)
                                        .set(bookData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Intent intent = new Intent(PurchaseHistory.this, ProfileActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
        else
        {
            Map<String, Object> bookData = new HashMap<>();

            db.collection("rooms/"+publicRoomID+"/access").document(userInfo.getUID())
                    .update("dependant", FieldValue.arrayUnion(userID))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                db.collection("users/"+userID+"/access").document(publicRoomID)
                                        .set(bookData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Intent intent = new Intent(PurchaseHistory.this, ProfileActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }




    }
}
