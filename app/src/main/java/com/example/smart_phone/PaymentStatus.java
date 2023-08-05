package com.example.smart_phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class PaymentStatus extends AppCompatActivity {

    TextView tv;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);
        tv = findViewById(R.id.paymentstatusTV);
        imageView = findViewById(R.id.paymentStatusIMG);

        String status = getIntent().getStringExtra("status");
        if(Objects.equals(status, "success"))
        {
            tv.setText("Payment received. Thank you for your purchase.");
            imageView.setImageResource(R.drawable.ic_baseline_check_24);
        }
        else if(Objects.equals(status, "failed"))
        {
            tv.setText("Payment failed. Please try again later.");
            imageView.setImageResource(R.drawable.ic_baseline_cancel_24);
        }

    }

    public void goBack(View v)
    {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }

}