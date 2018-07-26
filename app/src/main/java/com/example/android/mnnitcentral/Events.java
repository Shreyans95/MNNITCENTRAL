package com.example.android.mnnitcentral;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Events extends AppCompatActivity {
    private ViewPager imagepager;
    private DatabaseReference ref;
    private ViewPagerAdapter adapter;
    private String [] images;
    private RecyclerView list;
    private String new_path;
    private Toolbar toolbar;
    private FirebaseRecyclerAdapter<Users,UserViewHolder> firebaseRecyclerAdapter;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        ref = FirebaseDatabase.getInstance().getReference().child(getIntent().getStringExtra("class").toLowerCase()+"/lists");
        final String  path = getIntent().getStringExtra("class").toLowerCase();
        final String title = getIntent().getStringExtra("title");
//        ref = FirebaseDatabase.getInstance().getReference().child(path).child(title);
        images = new String[3];
        toolbar = (Toolbar)findViewById(R.id.culrav);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.v("ref222",""+ref);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(Sub_Events_1.this,"hi",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Events.this, Description.class);
                    intent.putExtra("class", getIntent().getStringExtra("class"));
                    intent.putExtra("title", getIntent().getStringExtra("title"));
                    intent.putExtra("image",getIntent().getStringExtra("image"));
                    startActivity(intent);
                    finish();

            }
        });

        //Events list
        list = findViewById(R.id.recycleview);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(Events.this));
        //new_path = getIntent().getStringExtra("class")+"/lists/"+title.toLowerCase();
        new_path = getIntent().getStringExtra("class");
        String res = new_path+"/lists";
        //ref = FirebaseDatabase.getInstance().getReference().child(res.toLowerCase());
        Log.v("category",getIntent().getStringExtra("class"));
        Log.v("ref",""+ref);
        ref.keepSynced(true);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(
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
                viewHolder.setImage(model.getImage(),Events.this);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Events.this,Sub_Events_1.class);
                        intent.putExtra("class",new_path );
                        intent.putExtra("sub_path", "/lists/"+model.getName());
                        intent.putExtra("title",model.getName());
                        intent.putExtra("present_title",getIntent().getStringExtra("title"));
                        intent.putExtra("image",getIntent().getStringExtra("image"));
                        // intent.putExtra("name",model.getName());
                        startActivity(intent);
                        finish();
                    }
                });

            }
        };
        list.setAdapter(firebaseRecyclerAdapter);
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
                break;
            case R.id.SignOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Events.this,MainActivity.class));
                editor.putBoolean("signin",false);
                editor.commit();
                finish();
                break;
//            case R.id.profile:
//                startActivity(new Intent(Events.this,Change_Profile.class));
//                finish();
        }
        return true;
    }
    public void onStart()
    {
        super.onStart();
    }

}
