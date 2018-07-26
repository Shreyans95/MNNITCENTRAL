package com.example.android.mnnitcentral;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Registration extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView image;
    DatabaseReference ref;
    private TextView details;
    private Button online_registration;
    private Button physical_registration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        toolbar = findViewById(R.id.registration);
        image = findViewById(R.id.image);
        details = findViewById(R.id.details);
        online_registration = findViewById(R.id.online_registration);
        physical_registration = findViewById(R.id.physical_registration);
        final String title = getIntent().getStringExtra("title");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        final String path = getIntent().getStringExtra("class");
        Log.v("pathhh",path);
        ref = FirebaseDatabase.getInstance().getReference().child(path.toLowerCase());
        Log.v("refer",""+ref);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                details.setText(dataSnapshot.child("details").getValue().toString());
                String imag = dataSnapshot.child("image").getValue().toString();
                Picasso.with(Registration.this).load(imag).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.avatar_default).into(image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final Intent intent = new Intent(Registration.this,Registration_Steps.class);
        online_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("class",path);
                intent.putExtra("prev_title",title);
                intent.putExtra("next_title","Online Registration");
                startActivity(intent);
            }
        });
        physical_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("class",path);
                intent.putExtra("prev_title",title);
                intent.putExtra("next_title","Physical Registration");
                startActivity(intent);
            }
        });
    }
}
