package com.example.freelancer;

import android.os.AsyncTask;
import android.os.Bundle;
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

public class Wallet extends AppCompatActivity implements TopUpDialog.ExampleDialogListener{

    final String urlGetBalance = "https://its-freelancer.herokuapp.com/api/wallet";
    final String urlChangeBalce = "https://its-freelancer.herokuapp.com/api/wallet";
    TextView txtBalance;
    Button btnTopUp;
    Button btnWithdraw;
    Token t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        t = new Token();
        showToast(t.getToken());
        txtBalance = (TextView) findViewById(R.id.txtBalance);
        btnTopUp = (Button) findViewById(R.id.btnTopup);
        btnWithdraw = (Button) findViewById(R.id.btnWithdraw);


        btnTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogTopUp(0);
            }
        });
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogTopUp(1);
            }
        });

        new UpdateWallet().execute(t.getToken());

    }

    private void openDialogTopUp(int f)  //Open Dialog to Top up
    {
        Bundle args = new Bundle();
        args.putInt("function",f);
        TopUpDialog dialog = new TopUpDialog();
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "Top up");
    }
    @Override
    public void applyTexts(String password, String amount) {
        new changeBalanceWithAmount().execute(password,amount);
        //new UpdateWallet().execute(t.getToken());
    }

    public class changeBalanceWithAmount extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //get username and password from str input
            String myToken = "Bearer " + t.getToken();
            String password = strings[0];
            String amount = strings[1];
            //Initiate server request
            OkHttpClient okHttpClient= new OkHttpClient();

            //Form Body
            RequestBody formBody = new FormBody.Builder()
                    .add("password", password)
                    .add("amount", String.valueOf(amount))
                    .build();

            //build request
            Request request = new Request.Builder()
                    .url(urlChangeBalce)
                    .post(formBody)
                    .addHeader("Authorization",myToken)
                    //.header("Content-Type", "application/json")
                    .build();

            //checking whether we are getting response from server or not
            Response response = null;
            try{
                response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful())
                {
                    String jsonData = response.body().string();
                    JSONObject Jobject = new JSONObject(jsonData);
                    String strBalance = Jobject.get("balance").toString();
                    showToast("Your balance is" + strBalance);
                    publishProgress(strBalance);
                }
                else
                {
                    String result = response.body().string();
                    JSONObject Jobject = new JSONObject(result);
                    String strMess = Jobject.get("message").toString();
                    showToast("Cant access server: " + strMess);
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(String... values)
        {
            txtBalance.setText(values[0]);
        }  //set text for asynctask

    }


    public class UpdateWallet extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //get username and password from str input
            String myToken = "Bearer " + strings[0];

            //Initiate server request
            OkHttpClient okHttpClient= new OkHttpClient();

            //build request
            Request request = new Request.Builder()
                    .url(urlGetBalance)
                    .addHeader("Authorization",myToken)
                    .header("Content-Type", "application/json")
                    .build();

            //checking whether we are getting response from server or not
            Response response = null;
            try{
                response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful())
                {
                    String jsonData = response.body().string();
                    JSONObject Jobject = new JSONObject(jsonData);
                    String strBalance = Jobject.get("balance").toString();
                    showToast("Your balance is" + strBalance);
                    publishProgress(strBalance);
                    //finish();

                }
                else
                {
                    String result = response.body().string();
                    JSONObject Jobject = new JSONObject(result);
                    String strMess = Jobject.get("message").toString();
                    showToast("Cant access server" + strMess);
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(String... values)
        {
            txtBalance.setText(values[0]);
        }  //set text for asynctask

    }
    public void showToast(final String Text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Wallet.this,
                        Text, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
