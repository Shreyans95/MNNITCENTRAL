package com.example.android.mnnitcentral;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class List_of_Places extends AppCompatActivity {

    private DatabaseReference ref;
    private RecyclerView list;
    private String new_path;
    private Toolbar toolbar;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of__places);

        ref = FirebaseDatabase.getInstance().getReference().child(getIntent().getStringExtra("class")).child("lists");
        toolbar = (Toolbar)findViewById(R.id.list_of_places);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = findViewById(R.id.places);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(List_of_Places.this));
        //Chnage 1

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(Sub_Events_1.this,"hi",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(List_of_Places.this, Welcome.class);
                intent.putExtra("class", getIntent().getStringExtra("class"));
                intent.putExtra("title", getIntent().getStringExtra("title"));
                intent.putExtra("image",getIntent().getStringExtra("image"));
                startActivity(intent);
                finish();

            }
        });


        //Change
        new_path = getIntent().getStringExtra("class")+getIntent().getStringExtra("title").toLowerCase();
        String res = new_path+"/lists";
        ref = FirebaseDatabase.getInstance().getReference().child(res.toLowerCase());
        FirebaseRecyclerAdapter<Users,UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                Users.class,
                R.layout.word,
                UserViewHolder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, Users m, final int position) {
                final Users model = m;
                viewHolder.setName(model.getName());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(model.getImage(),List_of_Places.this);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(List_of_Places.this,MapsActivity.class);
                        intent.putExtra("class",new_path+"/lists/" );
                        intent.putExtra("title",model.getName());
                        intent.putExtra("present_title",getIntent().getStringExtra("title"));
                        startActivity(intent);
                    }
                });
            }
        };
        list.setAdapter(firebaseRecyclerAdapter);
    }

    public void onStart() {
        super.onStart();
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
                startActivity(new Intent(List_of_Places.this,Settings.class));
                break;
            case R.id.SignOut:

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(List_of_Places.this,MainActivity.class));
                editor.putBoolean("signin",false);
                editor.commit();
                finish();
                break;
//            case R.id.profile:
//                startActivity(new Intent(List_of_Places.this,Change_Profile.class));
        }
        return true;
    }


}
