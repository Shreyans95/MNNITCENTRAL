package com.example.android.mnnitcentral;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration_Steps extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference ref;
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration__steps);
        toolbar = findViewById(R.id.reg_steps);
        list = findViewById(R.id.recycleview_1);
        String title = getIntent().getStringExtra("next_title");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        String path = getIntent().getStringExtra("class");
        String prev_title = getIntent().getStringExtra("prev_title");
        ref = FirebaseDatabase.getInstance().getReference().child(path.toLowerCase())
                .child(title.toLowerCase());
        Log.v("refer",""+ref);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(Registration_Steps.this));
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
