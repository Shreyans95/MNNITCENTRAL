package com.example.android.mnnitcentral;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Try extends AppCompatActivity {

    private ImageView image;
    private StorageReference s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try);
        image = findViewById(R.id.image);
        s = FirebaseStorage.getInstance().getReference().child("https://firebasestorage.googleapis.com/v0/b/mnnitcentral-a1269.appspot.com/o/academic%2Ftime%20table%2Fcs4a.png?alt=media&token=ffd93b66-a408-42dc-8315-7e1c12347f43");
//        Glide.with(this /* context */)
//                .using(new FirebaseImageLoader())
//                .load(storageReference)
//                .into(imageView);

    }
}
