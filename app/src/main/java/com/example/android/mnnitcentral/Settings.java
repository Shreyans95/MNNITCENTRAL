package com.example.android.mnnitcentral;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {

    private TextView account,name,userid,share;
    private TextView password_update,mute_notification,help;
    private CircleImageView update_profile_image;
    private DatabaseReference ref;
    private FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        name = findViewById(R.id.name);
        userid = findViewById(R.id.userid);
        account = findViewById(R.id.account);
        password_update = findViewById(R.id.password_update);
        mute_notification = findViewById(R.id.mute_notification);
        share = findViewById(R.id.share);
        help = findViewById(R.id.help);
        update_profile_image = findViewById(R.id.update_profile_image);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null)
        {
           name.setText("Logged Out");
           userid.setText("Logged Out");
           update_profile_image.setBackgroundResource(R.drawable.avatar_default);
        }
        else
            {
                uid = user.getUid();
                ref = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name1 = dataSnapshot.child("fname").getValue(String.class);
                        String name2 = dataSnapshot.child("mname").getValue(String.class);
                        String name3 = dataSnapshot.child("lname").getValue(String.class);
                        userid.setText(dataSnapshot.child("username").getValue(String.class));
                        name.setText(name1 + " " + name2 + " " + name3);
                        String image = dataSnapshot.child("image").getValue(String.class);
                        if (image != null) {
                            Picasso.with(Settings.this).load(image).placeholder(R.drawable.avatar_default).into(update_profile_image);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                account.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Settings.this, Change_Profile.class));
                    }
                });
                password_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        update_password();
                    }
                });
            }
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        ref.keepSynced(true);
    }
    protected void share()
    {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.setType("*/*");

        // Append file and send Intent
        File originalApk = new File(filePath);

        try {
            //Make new directory in new location
            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            //Get application's name and convert to lowercase
            tempFile = new File(tempFile.getPath() + "/" + getString(app.labelRes).replace(" ","").toLowerCase() + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            //Copy file to new location
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            //Open share dialog
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            startActivity(Intent.createChooser(intent, "Share app via"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void update_password(){
       startActivity(new Intent(Settings.this,Password_update.class));
    }
}
