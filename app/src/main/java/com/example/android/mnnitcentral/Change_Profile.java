package com.example.android.mnnitcentral;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class Change_Profile extends AppCompatActivity {

    private FirebaseUser user;
    private StorageReference mStorage;
    private EditText fname,lname,mname,username,number;
    private Button confirm;
    private DatabaseReference ref;
    private ProgressDialog progress,upload_progress;
    private CircleImageView change_image;
    private final static int GALLERY_PICK=1;
    private TextView name,userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__profile);
        progress = new ProgressDialog(Change_Profile.this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        fname = findViewById(R.id.fname);
        mname = findViewById(R.id.mname);
        lname = findViewById(R.id.lname);
        username = findViewById(R.id.username);
        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        userid = findViewById(R.id.userid);
        ref = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        change_image = findViewById(R.id.change_profile_image);
        mStorage = FirebaseStorage.getInstance().getReference();
        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_image();
            }
        });
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name1 =dataSnapshot.child("fname").getValue(String.class);
                String name2 =dataSnapshot.child("mname").getValue(String.class);
                String name3 =dataSnapshot.child("lname").getValue(String.class);
                fname.setText(name1);
                mname.setText(name2);
                lname.setText(name3);
                username.setText(dataSnapshot.child("username").getValue(String.class));
                number.setText(dataSnapshot.child("number").getValue(String.class));
                name.setText(name1+" "+name2+" "+name3);
                userid.setText(dataSnapshot.child("username").getValue(String.class));
                String image = dataSnapshot.child("image").getValue(String.class);
                if(image!=null)
                {
                    Picasso.with(Change_Profile.this).load(image).placeholder(R.drawable.avatar_default).into(change_image);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        progress.setMessage("Updating Profile");
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                change();
            }
        });

    }
    private void change()
    {
        ref.child("fname").setValue(fname.getText().toString());
        ref.child("mname").setValue(mname.getText().toString());
        ref.child("lname").setValue(lname.getText().toString());
        ref.child("number").setValue(number.getText().toString());
        ref.child("username").setValue(username.getText().toString());
        progress.dismiss();
        startActivity(new Intent(Change_Profile.this,Welcome.class));
        finish();
    }
    private void change_image()
    {

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setAspectRatio(1, 1)
                    .start(Change_Profile.this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
                upload_progress = new ProgressDialog(Change_Profile.this);
                upload_progress.setMessage("Uploading the Image");
                upload_progress.show();
                Uri resultUri = result.getUri();
                String user_id = user.getUid();
            StorageReference filepath = mStorage.child("profile_images").child(user_id+".jgp");
            filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                   if(task.isSuccessful())
                   {
                       String downloadUrl = task.getResult().getDownloadUrl().toString();
                       ref.child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                              if(task.isSuccessful())
                              {
                                  upload_progress.dismiss();
                              }
                           }
                       });
                   }
                   else
                       Toast.makeText(Change_Profile.this,"Not done",Toast.LENGTH_LONG).show();
                }
            });
            }
            else
                if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
                {
                    Exception error = result.getError();
                }
        }
    }
}
