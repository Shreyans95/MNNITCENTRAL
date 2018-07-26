package com.example.android.mnnitcentral;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TimeTable extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    private DatabaseReference ref;
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        String title = getIntent().getStringExtra("title");
        final String path = getIntent().getStringExtra("class");
        toolbar = findViewById(R.id.time_table);
        list = findViewById(R.id.recycleview_timetable);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        ref = FirebaseDatabase.getInstance().getReference().child(path).child("lists");
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(TimeTable.this));
        ref.keepSynced(true);
        //Display lists..
        FirebaseRecyclerAdapter<Lists,ListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Lists, ListViewHolder>(
                Lists.class,
                R.layout.list,
                ListViewHolder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(ListViewHolder viewHolder, Lists m, int position) {
                final Lists model = m;
                viewHolder.setName(model.getName());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(TimeTable.this,Show_Time_Table.class);
                        intent.putExtra("class",path);
                        intent.putExtra("title",model.getName());
                        Log.v("name",model.getName());
                        startActivity(intent);
                    }
                });
            }
        };
        list.setAdapter(firebaseRecyclerAdapter);

    }
}
