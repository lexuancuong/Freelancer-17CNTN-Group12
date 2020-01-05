package com.example.freelancer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ShowListTransaction extends AppCompatActivity {

    Token t;
    String Mytoken;
    Button showJob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_transaction);

        t = new Token();
        Mytoken = t.getToken();
    }

    public void callSingleTransaction(View view) {
        Intent i = new Intent(ShowListTransaction.this, ShowTransaction.class);
        startActivity(i);
    }
}
