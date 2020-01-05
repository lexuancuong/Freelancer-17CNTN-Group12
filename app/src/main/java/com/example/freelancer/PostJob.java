package com.example.freelancer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PostJob extends AppCompatActivity {

    private EditText _job_name;
    private Spinner _job_types;
    private EditText _job_des;
    private EditText _job_cv;
    private Spinner _job_group_price;

    private ArrayList<Integer> _job_prices;
    private ArrayList<String>  _des_prices;
    private ArrayList<String> _types_job_array;
    private ArrayList<Integer>_types_job_id_array;
    private Button _bt_post;
    private ProgressDialog waiting;
    private String _token;
    private final String post_url = "https://its-freelancer.herokuapp.com/api/job/";
    private boolean success = false;

    EditText _editTextPriceValue;
    EditText _editTextPriceDescription;
    ArrayList<String> _line_prices;
    ArrayAdapter<String> _price_adapter;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);
        setTitle("Dang công việc");
        getToken();
        connectLayout();

        getJobTypes();
        initPrice();
        event();

    }

    private void initPrice() {
        _job_prices = new ArrayList<>();
        _des_prices = new ArrayList<>();
        _line_prices=new ArrayList<>();
        _price_adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,_line_prices);
        _price_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _job_group_price.setAdapter(_price_adapter);

    }

    private void getToken() {
        Intent intent = this.getIntent();
        _token = intent.getExtras().getString("token");
    }

    private void getJobTypes() {
        new PostJob.FetchJobTypes().execute("https://its-freelancer.herokuapp.com/api/job-type/");
    }

    private void showJobTypes() {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,_types_job_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _job_types.setAdapter(adapter);


    }

    private void event() {
        _bt_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int nprices=_job_prices.size();
                    if (nprices<1){
                        showToast("Chua co gia san pham");
                        return;
                    }
                    JSONArray jsonArray=new JSONArray();
                    for (int i=0;i<nprices;i++){
                        JSONObject jsonObject1=new JSONObject();
                        jsonObject1.put("price",_job_prices.get(0));
                        jsonObject1.put("description",_des_prices.get(0));
                        jsonArray.put(jsonObject1);
                    }
                    new PostRequest().execute(
                            post_url,
                            _job_name.getText().toString(),
                            _job_des.getText().toString(),
                            _job_cv.getText().toString(),
                            String.valueOf(_types_job_id_array.get(_job_types.getSelectedItemPosition()))
                            ,
                            jsonArray.toString(),
                            _token
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

        });

        _job_group_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("vipc", "onItemSelected: "+String.valueOf(position) );
                _editTextPriceValue.setText(_job_prices.get(position).toString());
                _editTextPriceDescription.setText(_des_prices.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private void connectLayout() {
        _job_name = this.findViewById(R.id.job_name);
        _job_des = this.findViewById(R.id.job_des);
        _job_types = this.findViewById(R.id.job_types);
        _job_cv = this.findViewById(R.id.job_cv);
        _job_group_price = this.findViewById(R.id.job_price_group);
        _bt_post = this.findViewById(R.id.bt_post);


        _editTextPriceValue=this.findViewById(R.id.priceValueTemp);
        _editTextPriceDescription=this.findViewById(R.id.priceDescriptionTemp);
    }

    public void AddPrice(View view) {
        int price_value;
        String price_des;
        price_value=Integer.valueOf(_editTextPriceValue.getText().toString());
        price_des=_editTextPriceDescription.getText().toString();
        for(int i=0;i<_job_prices.size();i++){
            if (_job_prices.get(i)==price_value)
            {
                _des_prices.set(i,price_des);
                _line_prices.set(i,String.valueOf(price_value)+" "+price_des);
                _price_adapter.notifyDataSetChanged();
                return;
            }
        }
        if(price_value<1)return;
        _editTextPriceValue.setText("0");
        _editTextPriceDescription.setText(null);
        _job_prices.add(price_value);
        _des_prices.add(price_des);
        _line_prices.add(String.valueOf(price_value)+" "+price_des);
        _price_adapter.notifyDataSetChanged();
    }

    public void DelPrice(View view) {
        int price_value;
        price_value=Integer.valueOf(_editTextPriceValue.getText().toString());
        for(int i=0;i<_job_prices.size();i++){
            if (_job_prices.get(i)==price_value)
            {
                _job_prices.remove(i);
                _des_prices.remove(i);
                _line_prices.remove(i);
                _editTextPriceValue.setText("0");
                _editTextPriceDescription.setText(null);
                _price_adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,_line_prices);
                _price_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _job_group_price.setAdapter(_price_adapter);
                return;
            }
        }
    }

    private class FetchJobTypes extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            DAO data = new DAO();
            try {
                String data_line = data.doGetRequest(params[0]);
                JSONArray jsonArray = new JSONArray(data_line);
                _types_job_array = new ArrayList<>();
                _types_job_id_array=new ArrayList<>();
                for (int i = 0;i < jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    _types_job_id_array.add(jsonObject.getInt("id"));
                    _types_job_array.add(jsonObject.getString("name"));
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
            showJobTypes();
        }


    }
    private class PostRequest extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {

            String url=strings[0];
            String name=strings[1];
            String des=strings[2];
            String cv=strings[3];
            String tydeids=strings[4];
            String pricelist=strings[5];
            String token=strings[6];
            try {
                RequestBody requestBody=new FormBody.Builder()
                        .add("name",name.toString())
                        .add("description",des.toString())
                        .add("cv_url",cv.toString())
                        .add("type_id",tydeids)
                        .add("price_list",pricelist)
                        .build();
                DAO dao=new DAO();


                Response response=   dao.doPostRequest(token,url,requestBody);
                if(response.isSuccessful())
                {
                    String result= response.body().string();
                    success = true;
                    showToast("Dang thanh cong");
                    finish();
                }
                else
                {
                    String result= response.body().string();
                    JSONObject Jobject = new JSONObject(result);
                    String strToken = Jobject.get("message").toString();
                    showToast(strToken);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PostJob.this, Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
