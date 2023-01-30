package com.example.smart_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BillPlz extends AppCompatActivity {

    private String ID;
    private String roomFBID = "";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_plz);

        Bundle bundle = getIntent().getExtras();
        ID = bundle.getString("ID");

        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        Map<String, String> map = new HashMap<>();
        map.put("email", userInfo.getEmail());
        map.put("phone", "01156403489");
        map.put("name", userInfo.getName());
        map.put("amount", String.valueOf(1 * 100));
        map.put("desc"," Hotel Booking");

        myWebView.loadUrl("https://afcgame.online/pay",map);

        myWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                Log.d("Webview","Current URL = "+url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if(url.contains("bills"))
                {
                    Log.d("GETBILLID",url.substring(38));
                    saveTodb();
                }
                else if(url.contains("success"))
                {
                    updateToDb();
                    Log.d("GOTO","SUCCESS");
                    nav( "True",url);
                }
                else if(url.contains("failed"))
                {
                    Log.d("GOTO","FAILED");
                    nav( "False",url);
                }
            }
        });
    }

    public void nav(String status,String query)
    {
        if(status=="True")
        {
            updateToDb();
            Intent intent = new Intent(BillPlz.this, Receipt.class);
            intent.putExtra("status",status);
            intent.putExtra("query",query);
            startActivity(intent);
        }
        else if(status=="False")
        {
            /*Intent intent = new Intent(BillPlz.this, RoomDetail.class);
            intent.putExtra("ID",ID);
            startActivity(intent);*/
            Intent intent = new Intent(BillPlz.this, Receipt.class);
            intent.putExtra("status",status);
            intent.putExtra("query",query);
            startActivity(intent);
        }
    }

    public void updateToDb()
    {
        Map<String, Object> docData = new HashMap<>();
        docData.put("billstatus", true);
        docData.put("paidOn", FieldValue.serverTimestamp());

        Map<String, Object> accessData = new HashMap<>();
        accessData.put("ownerName", userInfo.getName());
        accessData.put("dependent", Arrays.asList());
        accessData.put("AddedOn", FieldValue.serverTimestamp());

        //Room Booking History
        db.collection("rooms/"+ID+"/booking").document(userInfo.getUID())
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BillPlz.this, "Something failed, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });

        //Room Booking Access
        db.collection("rooms/"+ID+"/access").document(userInfo.getUID())
                        .set(accessData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });

        //User Booking History
        db.collection("users/"+ userInfo.getUID()+"/booking").document(ID)
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        createToDb();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BillPlz.this, "Something failed, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void createToDb()
    {
        Map<String, Object> docData = new HashMap<>();
        docData.put("billstatus", false);
        db.collection("rooms/"+ID+"/booking").document(userInfo.getUID()).set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //test
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BillPlz.this, "Something failed, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void saveTodb()
    {
        Log.d("TEST ID BUND", ID);

        //Update Room Collection to be booking mode to prevent other guest to book the same room

        db.collection("rooms").document(ID)
                .update("isBooked", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        createToDb();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BillPlz.this, "Something failed, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });

        //Create a collection on User to add booking on his/her booking list

        Map<String, Object> docData = new HashMap<>();
        docData.put("billstatus", false);

        db.collection("users/"+ userInfo.getUID()+"/booking").document(ID)
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        createToDb();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BillPlz.this, "Something failed, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });

        /*db.collection("rooms").document(ID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BillPlz.this, "Something failed, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                })

        db.collection("rooms").whereEqualTo("roomNo",ID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                            roomFBID = document.getId().toString();
                            Log.d("TEST DB", roomFBID);
                            break;
                        }
                    }
                }

            })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TEST DB", "FAILED");
                    }
                });*/


    }
}