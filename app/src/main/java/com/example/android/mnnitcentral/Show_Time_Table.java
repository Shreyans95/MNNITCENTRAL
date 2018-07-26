package com.example.android.mnnitcentral;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Show_Time_Table extends AppCompatActivity {

    private ImageView image;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__time__table);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width),(int)(height*.7));
        image = findViewById(R.id.image_2);
        String path = getIntent().getStringExtra("class");
        String title = getIntent().getStringExtra("title");
        ref = FirebaseDatabase.getInstance().getReference().child(path)
                .child("lists").child(title.toLowerCase());
        Log.v("image",""+ref);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imag = dataSnapshot.child("image").getValue().toString();
                Log.v("images",imag);
                Picasso.with(Show_Time_Table.this).load(imag)
                        .placeholder(R.drawable.avatar_default).into(image);
                image.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref.keepSynced(true);
    }
}
