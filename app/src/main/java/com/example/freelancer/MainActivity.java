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
        connectLayout();
        event();
    }

    private void event() {
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {    //Event: click button login
            @Override
            public void onClick(View v) {
                String stringUsername = txtUsername.getText().toString().trim();
                String stringPassword = txtPassword.getText().toString().trim();
                new LoginUser().execute(login_url,stringUsername, stringPassword);
            }
        });

    }

    private void connectLayout() {
        txtUsername = (TextView)findViewById(R.id.txtUsername);
        txtPassword = (TextView)findViewById(R.id.txtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
    }


    public class LoginUser extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String...strings)
        {
            //get username and password from str input
            String url = strings[0];
            String txtUsername = strings[1];
            String txtPassword = strings[2];

            //Initiate server request
            OkHttpClient okHttpClient= new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("username", txtUsername)
                    .add("password", txtPassword)
                    .build();

            DAO data = new DAO();
            //checking whether we are getting response from server or not
            Response response = null;
            try{
                response = data.doPostRequest("",url,formBody);
                if(response.isSuccessful())
                {
                    String jsonData = response.body().string();
                    JSONObject Jobject = new JSONObject(jsonData);
                    String strToken = Jobject.get("token").toString();
                    showToast("Login Sucessly");
                    Intent intent = new Intent(MainActivity.this,HireJob.class);
                    intent.putExtra("token",strToken);
                    startActivity(intent);
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
