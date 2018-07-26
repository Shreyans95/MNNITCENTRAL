package com.example.android.mnnitcentral;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button create;
    private Button login,freeuser;
    private Toolbar toolbar;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // Log.v("start","stat");
        create = findViewById(R.id.create);
        login = findViewById(R.id.login);
        freeuser = findViewById(R.id.freeuser);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        Boolean ans = pref.getBoolean("signin",false);
        toolbar = (Toolbar)findViewById(R.id.main_page);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MNNIT CENTRAL APP");
        Log.v("status",""+ans);
        if(ans==true)
        {
            startActivity(new Intent(MainActivity.this,Welcome.class));
            finish();
        }
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Create_Account.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Login_Page.class));
            }
        });
        freeuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Welcome.class));
                finish();
            }
        });
    }
}
