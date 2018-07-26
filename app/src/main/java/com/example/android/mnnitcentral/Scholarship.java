package com.example.android.mnnitcentral;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Scholarship extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private DatabaseReference ref;
    private RecyclerView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship);
        String title = getIntent().getStringExtra("title");
        final String path = getIntent().getStringExtra("class");
        toolbar = findViewById(R.id.scholarship);
        list = findViewById(R.id.recycleview_scholarship);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        ref = FirebaseDatabase.getInstance().getReference().child(path).child("lists");
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(Scholarship.this));
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Scholarship.this);
                        builder.setTitle("Information Not Available");
                        builder.setMessage("The information will be provided at the time of the scholarship filling");

                        builder.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                });
            }
        };
        list.setAdapter(firebaseRecyclerAdapter);

    }
}
