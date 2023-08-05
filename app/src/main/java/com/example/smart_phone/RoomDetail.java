package com.example.smart_phone;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;

public class RoomDetail extends AppCompatActivity {

    private String ID;

    private Button backBtn;
    private Button bookBtn;

    private TextView roomNameTV;
    private TextView roomCapTV;
    private TextView roomTypeTV;

    private int sYear, sMonth, sDay;
    private int eYear, eMonth, eDay;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private Calendar ss;
    private Calendar cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(RoomDetail.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                errorCode + " Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RoomDetail.this, Billplz.class);
                intent.putExtra("total",100.00);
                intent.putExtra("ID",ID);
                intent.putExtra("start",ss);
                intent.putExtra("end",cc);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for payment")
                .setSubtitle("Use biometric credential to confirm your booking")
                .setNegativeButtonText("Cancel Action")
                .build();

        ImageSlider imageSlider =findViewById(R.id.slider);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/smart-hotel-d3cca.appspot.com/o/Room%2FHOTEL1.jpg?alt=media&token=c6f9a9fb-a83c-455d-ac96-f1121b72ffb0",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/smart-hotel-d3cca.appspot.com/o/Room%2FHOTEL2.jpg?alt=media&token=8aab37fc-91bb-4381-a592-e3d6539f05c5", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/smart-hotel-d3cca.appspot.com/o/Room%2FHOTEL%203.jpg?alt=media&token=6e6c0d06-c2f0-407e-9191-a329950b4b8a", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/smart-hotel-d3cca.appspot.com/o/Room%2FHOTEL%204.jpg?alt=media&token=58e7f394-766b-4759-b1a6-15e9b69246e9", ScaleTypes.FIT));
        imageSlider.setImageList(slideModels);



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
                getStartCal();

            }
        });
    }

    void getStartCal()
    {
        Calendar c = Calendar.getInstance();
        sYear = c.get(Calendar.YEAR);
        sMonth = c.get(Calendar.MONTH);
        sDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("START DATE","DATE = "+year+"??"+month+"??"+dayOfMonth);
                Calendar d = Calendar.getInstance();

                Log.d("C", String.valueOf(c.get(Calendar.MONTH)));
                Log.d("D", String.valueOf(d.get(Calendar.MONTH)));
                /*if(d.after(c))
                {
                    Toast.makeText(RoomDetail.this, "You can't book before day", Toast.LENGTH_SHORT).show();
                    getStartCal();
                }
                else
                {
                    getEndCal(c);
                }*/
                getEndCal(c);

            }
        }, sYear,sMonth,sDay);
        dpd.setTitle("Set Check-in Day");
        dpd.show();

    }

    void getEndCal(Calendar s)
    {
        final Calendar c = Calendar.getInstance();
        eYear = c.get(Calendar.YEAR);
        eMonth = c.get(Calendar.MONTH);
        eDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(dayOfMonth == sDay && month == sMonth && year == sYear)
                {
                    Toast.makeText(RoomDetail.this, "Check-out Date cannot be the same as Check-in Date", Toast.LENGTH_SHORT).show();
                    getEndCal(s);
                }
                else
                {
                    Log.d("END DATE","DATE = "+year+"??"+month+"??"+dayOfMonth);
                    getConfirm(s,c);
                }

            }
        }, eYear,eMonth,eDay);
        dpd.setTitle("Set Check-out Day");
        dpd.show();
    }

    void getConfirm(Calendar s, Calendar e)
    {
        new AlertDialog.Builder(this)
                .setTitle("Checkout")
                .setMessage("Confirm Checkout?")
                .setNegativeButton("Check-out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ss = s;
                        cc = e;
                        biometricPrompt.authenticate(promptInfo);
                    }
                })
                .setNeutralButton("Cancel Booking", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //finish
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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