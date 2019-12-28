package com.example.freelancer;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    private EditText txtUsername, txtPassword, txtEmail, txtPhone;
    private Button btnRegister;
    private final String urlRegister = "https://its-freelancer.herokuapp.com/api/account/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        connectLayout();
        event();
    }

    private void event() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUsername = txtUsername.getText().toString();
                String strPassword = txtPassword.getText().toString();
                String strEmail = txtEmail.getText().toString();
                String strPhone = txtPhone.getText().toString();

                new RegisterUser().execute(urlRegister, strUsername, strPassword, strEmail, strPhone);

            }
        });
    }

    private void connectLayout() {
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        btnRegister = findViewById(R.id.btnRegister);

    }

    public class RegisterUser extends AsyncTask<String,Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            //get username and password from str input
            String url = strings[0];
            String txtUsername = strings[1];
            String txtPassword = strings[2];
            String txtEmail = strings[3];
            String txtPhone = strings[4];

            //Initiate server request
            OkHttpClient okHttpClient= new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("username", txtUsername)
                    .add("password", txtPassword)
                    .add("email",txtEmail)
                    .add("phone",txtPhone)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            //checking whether we are getting response from server or not
            Response response= null;

            try {
                response= okHttpClient.newCall(request).execute();
                if(response.isSuccessful())
                {
                    String result= response.body().string();
                    showToast("Register Successfully, please Login");
                    Intent i= new Intent(Register.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    String result= response.body().string();
                    JSONObject Jobject = new JSONObject(result);
                    String strToken = Jobject.get("message").toString();
                    showToast(strToken);

                }

            }
            catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }
    }
    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Register.this, Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}