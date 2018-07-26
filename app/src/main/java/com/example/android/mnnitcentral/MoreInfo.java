package com.example.android.mnnitcentral;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MoreInfo extends AppCompatActivity {

    private String fname,mname,lname,pnum;
    private Button submit;
    private String email,username,password;
    private DatePicker datePicker;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference ref;
    private ProgressDialog progress;
    private CircleImageView upload_image;
    private static final int GALLERY_PICK =1;
    private StorageReference mStorage;
    private android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        submit = findViewById(R.id.submit);
        upload_image = findViewById(R.id.upload_image);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();
        toolbar = findViewById(R.id.more_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("More Info about You");
        String uid = user.getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname = ((EditText)findViewById(R.id.fname)).getText().toString();
                mname = ((EditText)findViewById(R.id.mname)).getText().toString();
                lname = ((EditText)findViewById(R.id.lname)).getText().toString();
                pnum = ((EditText)findViewById(R.id.pnumber)).getText().toString();
                //datePicker = findViewById(R.id.simpleDatePicker);
                email = getIntent().getStringExtra("email");
                username = getIntent().getStringExtra("username");
                password = getIntent().getStringExtra("password");
                mAuth = FirebaseAuth.getInstance();
                upload_image = findViewById(R.id.upload_image);


                progress = new ProgressDialog(MoreInfo.this);
                progress.setMessage("Registration in Process");
                progress.show();
                Toast.makeText(MoreInfo.this,"Verification Email Sent",Toast.LENGTH_LONG).show();
                HashMap<String,String> map = new HashMap<>();
                map.put("username",username);
                map.put("fname",fname);
                map.put("mname",mname);
                map.put("lname",lname);
                map.put("number",pnum);
                ref.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {

                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                        Toast.makeText(MoreInfo.this,"Email sent",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(MoreInfo.this,Login_Page.class));
                                    progress.dismiss();
                                    finish();
                                }
                            });
                        }
                    }
                });

            }
        });
    }
    private void upload(){
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setAspectRatio(1, 1)
                    .start(MoreInfo.this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
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
                                       // upload_progress.dismiss();
                                    }
                                }
                            });
                        }
                        else
                            Toast.makeText(MoreInfo.this,"Not done",Toast.LENGTH_LONG).show();
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
