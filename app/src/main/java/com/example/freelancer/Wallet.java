package com.example.freelancer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Wallet extends AppCompatActivity {

    final String wallet_url = "https://its-freelancer.herokuapp.com/api/wallet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Token t = new Token();
        new UpdateWallet().execute(t.getToken());

    }
    public class UpdateWallet extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String myToken = strings[0];

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
                Toast.makeText(Wallet.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
