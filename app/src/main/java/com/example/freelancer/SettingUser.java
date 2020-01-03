package com.example.freelancer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingUser extends AppCompatActivity {
    Button btnWallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user);
        btnWallet = (Button) findViewById(R.id.btnOpenWallet);
        btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingUser.this, Wallet.class);
                startActivity(i);
            }
        });
    }
}
