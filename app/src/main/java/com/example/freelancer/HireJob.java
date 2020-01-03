package com.example.freelancer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HireJob extends AppCompatActivity {

    private TextView _job_name;
    private TextView _job_type;
    private TextView _job_des;
    private TextView _job_cv;
    private RadioGroup _job_group_price;
    private TextView _fullname;
    private Button _bt_request;
    private Job _job;
    private ProgressDialog waiting;
    private String _token;
    private final String hire_url = "https://its-freelancer.herokuapp.com/api/transaction";
    private boolean success = false;
    private String msg;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_job);
        setTitle("Thuê công việc");
        connectLayout();
        getJobData();
        showJobData();
        event();
    }

    private void event() {
        _bt_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id_radio_button = _job_group_price.getCheckedRadioButtonId();
                RadioButton radioButton = HireJob.this.findViewById(id_radio_button);
                if (radioButton == null)
                {
                    showToast("Hãy chọn một giá");
                }
                else{
                    int idx = _job_group_price.indexOfChild(radioButton);
                    int id_job = _job.get_id();
                    int price_job = _job.get_price().get(idx);
                    new putRequest().execute(hire_url,String.valueOf(id_job),String.valueOf(price_job));
                    if (success == true){
                        finish();
                    }
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showJobData() {
        _job_name.setText(_job.get_name().toString());
        _job_type.setText(_job.get_type().toString());
        _job_des.setText(_job.get_description().toString());
        _job_cv.setText(_job.get_cv().toString());
        _fullname.setText(_job.get_fullname().toString());
        for (int i = 0;i < _job.get_price().size();i++)
        {
            RadioButton temp = new RadioButton(this);
            temp.setId(View.generateViewId());
            temp.setText(_job.get_price().get(i).toString() + " - " + _job.get_price_description().get(i).toString());
            _job_group_price.addView(temp);
        }
    }

    private void getJobData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        _job = (Job) bundle.getSerializable("job");
        _token = intent.getExtras().getString("token");
    }

    private void connectLayout() {
        _job_name = this.findViewById(R.id.job_name);
        _job_des = this.findViewById(R.id.jop_des);
        _job_type = this.findViewById(R.id.jop_type);
        _job_cv = this.findViewById(R.id.job_cv);
        _job_group_price = this.findViewById(R.id.jop_price_group);
        _bt_request = this.findViewById(R.id.bt_request);
        _fullname = this.findViewById(R.id.fullname);
    }

    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HireJob.this, Text, Toast.LENGTH_LONG).show();
            }
        });
    }

    private class putRequest extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Display progress bar
            waiting = new ProgressDialog(HireJob.this);
            waiting.setMessage("Processing ...");
            waiting.setIndeterminate(false);
            waiting.setCancelable(false);
            waiting.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url= strings[0];
            String id_job = strings[1];
            String price_job = strings[2];

            //Initiate server request
            OkHttpClient okHttpClient= new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("jobId",id_job)
                    .add("price",price_job)
                    .build();
            Request request = new Request.Builder()
                    .addHeader("Authorization","Bearer "+_token)
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
                    success = true;
                    msg = "Gửi yêu cầu thành công";
                }
                else
                {
                    String result= response.body().string();
                    JSONObject Jobject = new JSONObject(result);
                    msg = Jobject.get("message").toString();
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            waiting.dismiss();
            showToast(msg);
        }

    }
}
