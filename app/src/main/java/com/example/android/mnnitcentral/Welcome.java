package com.example.android.mnnitcentral;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Welcome extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Toolbar toolbar;
    private DatabaseReference ref;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        toolbar = (Toolbar)findViewById(R.id.welcome_page);
        setSupportActionBar(toolbar);
        progress = new ProgressDialog(Welcome.this);
        progress.setMessage("Welcome.");
        progress.show();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ref = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
            start();
        }
        else
        {
          getSupportActionBar().setTitle("MNNIT CENTRAL");
          progress.dismiss();
        }



        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabs = (TabLayout)findViewById(R.id.sliding_tabs);
        tabs.setupWithViewPager(viewPager);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
            case R.id.setting:
                startActivity(new Intent(Welcome.this,Settings.class));
                break;
            case R.id.SignOut:

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Welcome.this,MainActivity.class));
                editor.putBoolean("signin",false);
                editor.commit();
                finish();
                break;
//            case R.id.profile:
//                startActivity(new Intent(Welcome.this,Change_Profile.class));
        }
        return true;
    }

    private void start()
    {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("username").getValue(String.class);
                Log.v("check",name);
                editor.putString("username",name);
                getSupportActionBar().setTitle(name);
                editor.commit();
                progress.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
