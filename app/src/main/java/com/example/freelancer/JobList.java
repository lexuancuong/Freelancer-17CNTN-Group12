package com.example.freelancer;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class JobList extends AppCompatActivity {

    private ArrayList<Job> _list_job;
    private JobAdapter _adapter;
    private ListView _lv_job;
    private ProgressDialog _loading;
    private String _token = "";
    private final String _get_url = "https://its-freelancer.herokuapp.com/api/job/";
    private Button btnUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        btnUser = (Button)findViewById(R.id.btnAccount);
        getToken();
        connectLayout();
        getListJob();
        event();
    }

    private void getToken() {
        Intent intent = getIntent();
        _token = intent.getExtras().getString("token");
    }

    private void event(){
        btnUser.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v)
        {
            Intent intent = new Intent(JobList.this,SettingUser.class);
            startActivity(intent);
        }});
        _lv_job.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(JobList.this,HireJob.class);
                Bundle bundle=new Bundle();
                Job job = _list_job.get(position);
                bundle.putSerializable("job",(Serializable) job);
                intent.putExtra("bundle",bundle);
                intent.putExtra("token", _token);
                startActivity(intent);
            }
        });
    }


    private void showListJob() {
        if (_list_job != null) {
            _adapter = new JobAdapter(this,R.layout.job,_list_job);
            _lv_job.setAdapter(_adapter);
        }
    }

    private void getListJob() {
        new FetchJobsAsyncTask().execute(_get_url);

    }

    private void connectLayout() {
        _loading = new ProgressDialog(this.getBaseContext());
        _lv_job = this.findViewById(R.id.list_job);
    }

    private class FetchJobsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Display progress bar
            _loading = new ProgressDialog(JobList.this);
            _loading.setMessage("Loading ...");
            _loading.setIndeterminate(false);
            _loading.setCancelable(false);
            _loading.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            DAO data = new DAO();
            try {
                String data_line = data.doGetRequest(params[0]);
                JSONArray jsonArray = new JSONArray(data_line);
                _list_job = new ArrayList<>();
                for (int i = 0;i < jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String name = jsonObject.getString("name");
                    String description = jsonObject.getString("description");
                    String cv = jsonObject.getString("cv_url");
                    String type = jsonObject.getString("type");
                    String username = jsonObject.getString("username");
                    String fullname = jsonObject.getString("fullname");
                    JSONArray jsonArray1 = jsonObject.getJSONArray("price_list");
                    ArrayList<Integer> price = new ArrayList<>();
                    ArrayList<String> price_des = new ArrayList<>();
                    for (int j = 0;j < jsonArray1.length();j++){
                        JSONObject temp = jsonArray1.getJSONObject(j);
                        price.add(temp.getInt("price"));
                        price_des.add(temp.getString("description"));
                    }
                    Job job = new Job(name,description,type,cv,price,price_des,id,username,fullname);
                    _list_job.add(job);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            _loading.dismiss();
            showListJob();
        }
    }

    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(JobList.this, Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
