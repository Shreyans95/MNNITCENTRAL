package com.example.android.mnnitcentral;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Sub_Events_1 extends AppCompatActivity {

    private ImageView image_1;
    private DatabaseReference ref;
    private RecyclerView list;
    private String path,imagepath;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        setContentView(R.layout.activity_sub__events_1);
        toolbar = (Toolbar)findViewById(R.id.sub_events_1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(Sub_Events_1.this,"hi",Toast.LENGTH_SHORT).show();
                if(getIntent().getStringExtra("title").equalsIgnoreCase("Atheletic Meet"))
                {
                    startActivity(new Intent(Sub_Events_1.this,Welcome.class));
                    finish();
                }
                else
                {
                    Intent intent = new Intent(Sub_Events_1.this, Events.class);
                    intent.putExtra("class", getIntent().getStringExtra("class"));
                    intent.putExtra("title", getIntent().getStringExtra("present_title"));
                    intent.putExtra("image",getIntent().getStringExtra("image"));
                    startActivity(intent);
                    finish();
                }
            }
        });
        image_1 = findViewById(R.id.image_1);
        list = findViewById(R.id.recycleview_1);
        imagepath = (getIntent().getStringExtra("class")+getIntent().getStringExtra("sub_path")).toLowerCase();
        path = imagepath +"/lists";

        DatabaseReference i = FirebaseDatabase.getInstance().getReference().child(imagepath).child("image");
        i.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String image =dataSnapshot.getValue().toString() ;
                Picasso.with(Sub_Events_1.this).load(dataSnapshot.getValue().toString()).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.avatar_default).into(image_1);
                //Log.v("imagess",image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref = FirebaseDatabase.getInstance().getReference().child(path.toLowerCase());

        //Events List
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(Sub_Events_1.this));
        ref.keepSynced(true);


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
                viewHolder.setImage(model.getImage(),Sub_Events_1.this);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(FirebaseAuth.getInstance().getCurrentUser() == null)
                        {
                            //Toast.makeText(Sub_Events_1.this,"log in",Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Sub_Events_1.this);
                            builder.setTitle("User Not Logged In");
                            builder.setMessage("You must be logged in to read furthur content");
                            builder.setPositiveButton("Log In", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User clicked OK button
                                    startActivity(new Intent(Sub_Events_1.this,Login_Page.class));
                                    finish();
                                }
                            });
                            builder.setNegativeButton("Continue as free User", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }
                        else {
                            Intent intent = new Intent(Sub_Events_1.this, Details.class);
                            intent.putExtra("class", path + "/" + model.getName().toLowerCase());
                            Log.v("classname",path + "/" + model.getName().toLowerCase());
                            intent.putExtra("name",model.getName());
//                            Intent intent = new Intent(Sub_Events_1.this, Events.class);
//                            intent.putExtra("prev_class", path);
//                            intent.putExtra("prev_title", getIntent().getStringExtra("present_title"));
//                            intent.putExtra("prev_image",getIntent().getStringExtra("image"));
//                            startActivity(intent);
//                            finish();
                            startActivity(intent);
                        }
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
                startActivity(new Intent(Sub_Events_1.this,MainActivity.class));
                editor.putBoolean("signin",false);
                editor.commit();
                finish();
                break;
//            case R.id.profile:
//                startActivity(new Intent(Sub_Events_1.this,Change_Profile.class));
//                finish();
        }
        return true;
    }
    public void onStart()
    {
        super.onStart();
    }
}
