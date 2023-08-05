package com.example.smart_phone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Billplz extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFunctions mFunctions = FirebaseFunctions.getInstance("asia-southeast1");
    WebView webView;
    String ID;
    Calendar start,end;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_plz);

        Intent intent = getIntent();
        total = intent.getDoubleExtra("total", 0.0);
        ID = getIntent().getStringExtra("ID");
        start = getIntent().getParcelableExtra("start");
        end = getIntent().getParcelableExtra("end");


        initUI();
        createBill();
    }

    private void initUI()
    {
        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    public void createBill()
    {
        Map<String, Object> data = new HashMap<>();
        data.put("collection_id", "oqfedjph");
        data.put("email", (!userInfo.getEmail().isEmpty()) ?  userInfo.getEmail() : "mfarisammar@gmail.com");
        data.put("name", (!userInfo.getName().isEmpty()) ?  userInfo.getName() : "faris" );
        data.put("amount", (total.isNaN() || total != 0) ? (total*100) : 10000);
        data.put("description", "Hotel.");
        mFunctions.getHttpsCallable("createBill").call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        Map<String, Object> result = (Map<String, Object>) httpsCallableResult.getData();
                        String billId = (String) result.get("id");
                        String redirectUrl = (String) result.get("url");
                        Log.d("Payment","successful");
                        Log.d("Payment",result.toString());
                        Log.d("Payment",billId);
                        Log.d("Payment",redirectUrl);

                        // Redirect the user to the payment page
                        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl));
                        //startActivity(browserIntent);

                        //
                        webView.loadUrl(redirectUrl);
                        webView.setWebViewClient(new WebViewClient()
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
                                    saveTodb();
                                    Log.d("GETBILLID",url.substring(38));
                                }
                                else if(url.contains("success"))
                                {
                                    updateToDb();
                                    Log.d("GOTO","SUCCESS");
                                    nav( "True",url);
                                }
                                else if(url.contains("redirect?Billplz[id]"))
                                {
                                    Log.d("GOTO","FAILED");
                                    nav( "False",url);
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseFunctionsException) {
                            FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                            FirebaseFunctionsException.Code code = ffe.getCode();
                            String message = ffe.getMessage();
                            Log.d("Payment",message);
                            Log.e("Payment", "createBill failed", e);
                        }
                    }});
    }

    public void nav(String status,String query)
    {
        if(status=="True")
        {
            updateToDb();
            Intent intent = new Intent(Billplz.this, Receipt.class);
            intent.putExtra("status",status);
            intent.putExtra("query",query);
            startActivity(intent);



        }
        else if(status=="False")
        {
            Intent intent = new Intent(Billplz.this, Receipt.class);
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
                        Toast.makeText(Billplz.this, "Something failed, please try again later.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Billplz.this, "Something failed, please try again later.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Billplz.this, "Something failed, please try again later.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Billplz.this, "Something failed, please try again later.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Billplz.this, "Something failed, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}