package com.example.sms_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.sms_app.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class Send_SMS extends AppCompatActivity {


    private EditText txtMessage;
    private RequestQueue mQueue;
    private String auth_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        auth_token = bundle.getString("token");
        setContentView(R.layout.activity_send_sms);

        Button btnSms = findViewById(R.id.send_sms);
        mQueue = Volley.newRequestQueue(this);

        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getjson();
                //sendMessageFunc(txtMessage);
            }
        });
    }

    private void getjson(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getString(R.string.fetch_data_url), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("directory");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String msg = obj.getString("message");
                                String num = obj.getString("to");
                                sendMessageFunc(num, msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<String, String>();
                String token = "Token " + auth_token;
                params.put("Authorization", token);
                return params;
            }
        };
        mQueue.add(request);
    }

    private void sendMessageFunc(String txtMobile, String txtMessage) {
        try{
            SmsManager sms_mgr = SmsManager.getDefault();
            sms_mgr.sendTextMessage(txtMobile.toString(), null,  txtMessage.toString(), null, null);
            Toast.makeText(Send_SMS.this, "SMS sent successfully.", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e){
            Toast.makeText(Send_SMS.this, "SMS failed to sent. Please generate the message again.",Toast.LENGTH_SHORT).show();
        }
    }

}