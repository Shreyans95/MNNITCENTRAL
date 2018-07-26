package com.example.android.mnnitcentral;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Description extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private ViewPager imagepager;
    private DatabaseReference ref;
    private ViewPagerAdapter adapter;
    private String [] images;
    private TextView desc;
    private Button details;
    private ImageView i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        final String title = getIntent().getStringExtra("title");
        toolbar = findViewById(R.id.description);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About "+title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String path = getIntent().getStringExtra("class");
        ref = FirebaseDatabase.getInstance().getReference().child(path.toLowerCase());
        desc = findViewById(R.id.desc);
        details = findViewById(R.id.details);
        i = findViewById(R.id.event_image);
        Log.v("refffff",""+ref);
//        imagepager =  findViewById(R.id.imagepager);
//        imagepager.setOffscreenPageLimit(2);
//        images = new String[3];
//        Log.v("refff",""+ref);
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                images[0]=dataSnapshot.child("image1").getValue().toString();
//                images[1]=dataSnapshot.child("image2").getValue().toString();
//                images[2]=dataSnapshot.child("image3").getValue().toString();
//                Log.v("image1",images[0]);
//                Log.v("image2",images[1]);
//                Log.v("image3",images[2]);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        imagepager = findViewById(R.id.imagepager);
//        adapter = new ViewPagerAdapter(Description.this,images);
//        imagepager.setAdapter(adapter);


        final String image = getIntent().getStringExtra("image");
        if(image!=null) {

            try {
                Picasso.with(Description.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.avatar_default).into(i, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(Description.this).load(image).placeholder(R.drawable.avatar_default).into(i);
                    }
                });
            } catch (Exception e) {
                Log.v("message","this is executed");
                i.setImageResource(R.drawable.avatar_default);
            }
        }
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                desc.setText(dataSnapshot.child("details").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                        if(title.equalsIgnoreCase("Atheletic Meet"))
                        {
                            intent = new Intent(Description.this, Sub_Events_1.class);
                            intent.putExtra("class", path);
                            intent.putExtra("sub_path", "");
                        }
                        else

                        {
                            intent = new Intent(Description.this, Events.class);
                            //intent.putExtra("class", path);
                            intent.putExtra("class", path);

                        }
                            intent.putExtra("title", title);
                        intent.putExtra("image",image);
                            startActivity(intent);
                            finish();
            }
        });
        ref.keepSynced(true);
    }
}
