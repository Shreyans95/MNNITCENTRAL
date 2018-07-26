package com.example.android.mnnitcentral;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Details extends AppCompatActivity {

    private DatabaseReference ref;
    private String name;
    private Toolbar toolbar;
    private TextView information;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        String title = getIntent().getStringExtra("name");
        toolbar = findViewById(R.id.details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        String path = getIntent().getStringExtra("class");
        ref = FirebaseDatabase.getInstance().getReference().child(path);
        Log.v("ref for sub events",""+ref);
        image = findViewById(R.id.image);
        information = findViewById(R.id.information);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                information.setText(dataSnapshot.child("details").getValue().toString());
                String imag = dataSnapshot.child("image").getValue().toString();
                Picasso.with(Details.this).load(imag).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.avatar_default).into(image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
