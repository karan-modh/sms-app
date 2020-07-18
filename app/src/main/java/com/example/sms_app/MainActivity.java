package com.example.sms_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText txtMobile;
    private EditText txtMessage;
    private Button btnSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMobile = findViewById(R.id.mobileNo);
        txtMessage = findViewById(R.id.message);
        btnSms = findViewById(R.id.btnSend);
        btnSms.setOnClickListener(view -> sendMessageFunc(txtMobile, txtMessage));
    }

    private void sendMessageFunc(EditText txtMobile, EditText txtMessage) {
        try{
            SmsManager sms_mgr = SmsManager.getDefault();
            sms_mgr.sendTextMessage(txtMobile.getText().toString(), null,  txtMessage.getText().toString(), null, null);
            Toast.makeText(MainActivity.this, "SMS sent successfully.", Toast.LENGTH_SHORT).show();
            txtMessage.getText().clear();
            txtMobile.getText().clear();
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this, "SMS failed to sent. Please try again.",Toast.LENGTH_SHORT).show();
        }
    }
}



