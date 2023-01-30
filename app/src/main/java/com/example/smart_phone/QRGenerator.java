package com.example.smart_phone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import org.w3c.dom.Text;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRGenerator extends AppCompatActivity {

    private ImageView qrImage;
    private TextView nameTV;
    private TextView typeTV;

    private String UID;
    private String type;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);

        Bundle bundle = getIntent().getExtras();
        UID = bundle.getString("UID");
        type = bundle.getString("type");
        name = bundle.getString("name");

        initUI();
        generateQR();
    }

    private void initUI()
    {
        qrImage = findViewById(R.id.qrImageIV);
        nameTV = findViewById(R.id.QRName);
        typeTV = findViewById(R.id.QRType);
    }


    private void generateQR()
    {
        // initializing a variable for default display.
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        // get Screen Position
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = Math.min(width, height);
        dimen = dimen * 3 / 4;

        //QR
        QRGEncoder enc = new QRGEncoder("smarthoteld3cca"+UID, null, QRGContents.Type.TEXT, dimen);
        enc.setColorBlack(Color.WHITE);
        enc.setColorWhite(Color.BLACK);
        Bitmap bitmap = enc.getBitmap();
        qrImage.setImageBitmap(bitmap);

        //Name
        nameTV.setText(name);

        //type
        typeTV.setText(type);
    }

    public void goBack(View v)
    {
        Intent intent = new Intent(QRGenerator.this, ProfileActivity.class);
        startActivity(intent);
    }
}