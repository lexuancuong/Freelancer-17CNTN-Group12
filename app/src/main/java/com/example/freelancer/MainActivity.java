package com.example.freelancer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView txtUsername;
    private TextView txtPassword;
    private Button btnLogin;
    private TextView txtRegister;
    final String login_url = "https://its-freelancer.herokuapp.com/api/account/login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUsername = (TextView)findViewById(R.id.txtUsername);
        txtPassword = (TextView)findViewById(R.id.txtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        Token g = new Token();
        g.setToken("asjdhjkashdjahsdh");
        String tmp = g.getToken();
        showToast(tmp);

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("xxxx", "This is my messageasdasdas");
                Intent i = new Intent(MainActivity.this, Register.class);
                Log.d("xxxx", "This is my messageasdasdas");

                startActivity(i);
                showToast("hasdahdjahsjdasd");
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {    //Event: click button login
            @Override
            public void onClick(View v) {
                String stringUsername = txtUsername.getText().toString().trim();
                String stringPassword = txtPassword.getText().toString().trim();
                new LoginUser().execute(stringUsername, stringPassword);
            }
        });

    }

    public class LoginUser extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String...strings)
        {
            //get username and password from str input
            String txtUsername = strings[0];
            String txtPassword = strings[1];

            //Initiate server request
            OkHttpClient okHttpClient= new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("username", txtUsername)
                    .add("password", txtPassword)
                    .build();

            Request request = new Request.Builder()
                    .url(login_url)
                    .post(formBody)
                    .build();

            //checking whether we are getting response from server or not
            Response response = null;
            try{
                response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful())
                {
                    String jsonData = response.body().string();
                    JSONObject Jobject = new JSONObject(jsonData);
                    String strToken = Jobject.get("token").toString();
                    showToast("Login Sucessly");
                }
                else
                {
                    showToast("Wrong Username or password");
                }

            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

    }
    public void showToast(final String Text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }

}
