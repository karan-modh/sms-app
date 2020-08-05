package com.example.sms_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private String auth_token = "";
    private RequestQueue mQueue;
//    private String loginUrl = "https://sms-app-cs321.herokuapp.com/api/account/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(auth_token != ""){
            openSMSscreen();
        }
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        Button btnLogin = findViewById(R.id.login_btn);

        mQueue = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(view -> perform_login(email.getText().toString(), password.getText().toString()));
    }



    public void perform_login(String email, String password){

        StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.loginUrl) ,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        auth_token = parseResponse(response);
                        openSMSscreen();
                    }
                    catch (Exception e){
                        Log.e("Response Parse Error", e.toString());
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        mQueue.add(request);
    }

    public String parseResponse(String response) throws JSONException {
        JSONObject obj = new JSONObject(response);
        System.out.println(response);
        if (obj.getString("response").equals("Successfully authenticated.")) {
            return obj.getString("token");
        }
        else{
            return "";
        }

    }

    public void openSMSscreen(){
        if(auth_token != ""){
            Intent intent = new Intent(this,Send_SMS.class);
            intent.putExtra("token", auth_token);
            startActivity(intent);
        }
        else{
            Toast.makeText(MainActivity.this, "Invalid Login Credentials Provided", Toast.LENGTH_SHORT).show();
        }
    }


}



