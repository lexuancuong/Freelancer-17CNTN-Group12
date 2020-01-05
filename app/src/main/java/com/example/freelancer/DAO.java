package com.example.freelancer;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DAO {
    OkHttpClient client = new OkHttpClient();
    // code request code here
    String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    // post request code here
    Response doPostRequest(String token, String url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+ token)
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
}
