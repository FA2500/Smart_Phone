package com.example.smart_phone;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ShareRoomAccess extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout myBookLL;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Uri temp;
    private String publicRoomID;

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
        myBookLL = findViewById(R.id.applianceRoomList);
        newLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onClick(View v)
    {
        Log.d("TEST ID", String.valueOf(v.getId()));
        if(Integer.parseInt(String.valueOf(v.getId())) >= 1000)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Send Room Access")
                    .setMessage("Select Method to send room Access?")
                    .setPositiveButton("Show QR Code", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            POSTgenerateQR(v.getContentDescription().toString() , v.getTooltipText().toString());
                        }
                    })
                    .setNegativeButton("Scan QR Code", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            publicRoomID = v.getContentDescription().toString();
                            POSTscanQR();
                        }
                    })
                    .setNeutralButton("Cancel Action", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //finish
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
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
                            DocumentSnapshot docs =  task.getResult();

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
                                            card.setContentDescription(docs.getId());
                                            card.setTooltipText(docs.get("roomNo").toString());
                                            myBookLL.addView(card);

                                            cardID++;

                                            temp = uri;
                                            Picasso.get()
                                                    .load(temp)
                                                    .resize(300,200)
                                                    .into(img);
                                            //img.setOnClickListener(BookingActivity.this);
                                            //img.setId(buttonid);
                                            img.setContentDescription("TEST");

                                            LinearLayout LL = new LinearLayout(ShareRoomAccess.this);
                                            LL.setOrientation(LinearLayout.VERTICAL);

                                            //Room Number
                                            TextView NTV = new TextView(ShareRoomAccess.this);
                                            NTV.setText(docs.get("roomNo").toString());
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

    private void POSTgenerateQR(String ID, String name)
    {
        Intent intent = new Intent(ShareRoomAccess.this, QRGenerator.class);
        intent.putExtra("UID",ID);
        intent.putExtra("type","POST");
        intent.putExtra("name",name);
        startActivity(intent);
    }

    private void POSTscanQR()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 50);
        }
        Log.d("CAMERA PERM", String.valueOf(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)));
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureActivity.class);

        barLauncher.launch(options);
    }

    ActivityResultLauncher< ScanOptions > barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            if(result.getContents().contains("smarthoteld3cca"))
            {
                String GetID = result.getContents().substring(15);
                //test
                getDbData(GetID);
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShareRoomAccess.this);
                builder.setTitle("Invalid QR Code");
                builder.setMessage("You can only scan HOBBS QR Code");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }

        }
    });

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
                                                    Intent intent = new Intent(ShareRoomAccess.this, ProfileActivity.class);
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
                                                    Intent intent = new Intent(ShareRoomAccess.this, ProfileActivity.class);
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
