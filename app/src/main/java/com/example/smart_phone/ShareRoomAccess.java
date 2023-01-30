package com.example.smart_phone;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ShareRoomAccess extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout myBookLL;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Uri temp;



    private int cardID = 1000;

    private LinearLayout newLayout;
    private LinearLayout.LayoutParams newLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareroomaccess);

        initUI();
        getData();
    }

    private void initUI()
    {
        myBookLL = findViewById(R.id.mybooklist);
        newLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onClick(View v)
    {

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
                        TextView TV = new TextView(ShareRoomAccess.this);
                        TV.setText("You didn't book any room yet");
                        myBookLL.addView(TV);
                    }
                    for(QueryDocumentSnapshot doc : task.getResult())
                    {
                        createButton(doc);
                    }
                }
                else
                {
                    Toast.makeText(ShareRoomAccess.this, "Something's wrong, please try again later.", Toast.LENGTH_SHORT).show();
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
                            DocumentSnapshot doc =  task.getResult();

                            //storageReference.child("Merchant/"+name+"/logo")
                            storageReference.child("Room/"+"test"+"/roompic.jfif")
                                    .getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            MaterialCardView card = new MaterialCardView(ShareRoomAccess.this);
                                            card.setBackgroundColor(Color.CYAN);
                                            card.setStrokeColor(Color.BLACK);
                                            card.setStrokeWidth(5);
                                            card.setId(cardID);
                                            card.setOnClickListener(ShareRoomAccess.this);
                                            card.setContentDescription("ID NANTI");
                                            myBookLL.addView(card);

                                            cardID++;

                                            temp = uri;
                                            Picasso.get()
                                                    .load(temp)
                                                    .resize(350,255)
                                                    .into(img);
                                            //img.setOnClickListener(BookingActivity.this);
                                            //img.setId(buttonid);
                                            img.setContentDescription("TEST");

                                            LinearLayout LL = new LinearLayout(ShareRoomAccess.this);
                                            LL.setOrientation(LinearLayout.VERTICAL);

                                            //Room Number
                                            TextView NTV = new TextView(ShareRoomAccess.this);
                                            NTV.setText(doc.get("roomNo").toString());
                                            NTV.setTextSize(20);


                                            newLayout = new LinearLayout(ShareRoomAccess.this);
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
