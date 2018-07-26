package com.example.android.mnnitcentral;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class TLB extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView image;
    DatabaseReference ref;
    private TextView details;
    private RecyclerView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tlb);
        toolbar = findViewById(R.id.academic_1);
        image = findViewById(R.id.image);
        details = findViewById(R.id.details);
        String title = getIntent().getStringExtra("title");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        String path = getIntent().getStringExtra("class");
        Log.v("pathhh",path);
        ref = FirebaseDatabase.getInstance().getReference().child(path.toLowerCase());
        Log.v("refer",""+ref);
        list = findViewById(R.id.recycleview_1);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                details.setText(dataSnapshot.child("details").getValue().toString());
                String imag = dataSnapshot.child("image").getValue().toString();
                Picasso.with(TLB.this).load(imag).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.avatar_default).into(image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref = ref.child("steps");
        Log.v("refer",""+ref);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(TLB.this));
        ref.keepSynced(true);
        //Display lists..
        FirebaseRecyclerAdapter<Lists,ListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Lists, ListViewHolder>(
                Lists.class,
                R.layout.lists,
                ListViewHolder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(ListViewHolder viewHolder, Lists model, int position) {
               viewHolder.setDetails(model.getDetails());
            }
        };
        list.setAdapter(firebaseRecyclerAdapter);

    }
}
