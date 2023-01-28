package com.example.smart_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.type.DateTime;

import java.util.Calendar;
import android.widget.TimePicker;
import java.io.IOException;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Toast;

public class RoomDetail extends AppCompatActivity {

    private String ID;

    private Button backBtn;
    private Button bookBtn;

    private TextView roomNameTV;
    private TextView roomCapTV;
    private TextView roomTypeTV;

    private int sYear, sMonth, sDay;
    private int eYear, eMonth, eDay;

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
                if(d.after(c))
                {
                    Toast.makeText(RoomDetail.this, "You can't book before day", Toast.LENGTH_SHORT).show();
                    getStartCal();
                }
                else
                {
                    getEndCal(c);
                }

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
                .setTitle("More Booking")
                .setMessage("Do you want to book another room?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(RoomDetail.this, BillPlz.class);
                        intent.putExtra("ID",ID);
                        intent.putExtra("start",s);
                        intent.putExtra("end",e);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel Booking", null)
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