package com.example.smart_phone;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRGenerator extends AppCompatActivity {

    ImageView qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);

        qrImage = findViewById(R.id.qrImageIV);
        generateQR();
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


        QRGEncoder enc = new QRGEncoder("smarthoteld3cca"+userInfo.getUID(), null, QRGContents.Type.TEXT, dimen);
        enc.setColorBlack(Color.WHITE);
        enc.setColorWhite(Color.BLACK);
        Bitmap bitmap = enc.getBitmap();
        qrImage.setImageBitmap(bitmap);
    }
}