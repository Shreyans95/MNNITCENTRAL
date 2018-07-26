package com.example.android.mnnitcentral;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Password_update extends AppCompatActivity {

    private FirebaseUser user;
    private EditText password,old_password,email;
    private Button submit,cancel;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_update);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));
        password = findViewById(R.id.new_password);
        submit = findViewById(R.id.update_password_btn);
        cancel = findViewById(R.id.cancel);
        old_password = findViewById(R.id.old_password);
        email = findViewById(R.id.present_email);
        user = FirebaseAuth.getInstance().getCurrentUser();
        progress = new ProgressDialog(Password_update.this);
        progress.setMessage("Updating...");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               update_password();

            }
        });


    }
    protected void update_password()
    {
        progress.show();
        final String pass = password.getText().toString().trim();

        if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(Password_update.this,"Please Enter the password",Toast.LENGTH_SHORT).show();
        }
        else {
            user = FirebaseAuth.getInstance().getCurrentUser();
            String emailId = email.getText().toString().trim();

            String oldpass = old_password.getText().toString().trim();
            AuthCredential credential = EmailAuthProvider
                    .getCredential(emailId, oldpass);
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Password_update.this, "Password is updated!", Toast.LENGTH_SHORT).show();
                                            progress.dismiss();
                                            finish();
                                        } else {
                                            Toast.makeText(Password_update.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                          //  progress.hide();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Password_update.this, "Error in login!", Toast.LENGTH_SHORT).show();
                              //  progress.hide();
                            }
                        }
                    });
        }
        progress.dismiss();
       // finish();
    }
}
