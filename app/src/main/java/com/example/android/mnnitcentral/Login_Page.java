package com.example.android.mnnitcentral;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
public class Login_Page extends AppCompatActivity {

    private EditText email ;
    private EditText password ;
    private Button submit;
    private Toolbar toolbar;
    private ProgressDialog progress;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView forget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        submit = (Button)findViewById(R.id.button);
        forget = findViewById(R.id.forget);
        toolbar = (Toolbar)findViewById(R.id.login_page);
        auth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progress = new ProgressDialog(Login_Page.this);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("Logining you in..");
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setMessage("Sending...");
                progress.show();
                String emailid = email.getText().toString();
                if(TextUtils.isEmpty(emailid))
                    Toast.makeText(Login_Page.this,"Please Enter the email",Toast.LENGTH_SHORT).show();
                else
                auth.sendPasswordResetEmail(emailid)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(Login_Page.this,"Password reset option sent to email",Toast.LENGTH_SHORT).show();
                                }
                                progress.dismiss();
                            }
                        });
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //progress.setMessage("Logining you in..");
                progress.show();
//                progress.setCanceledOnTouchOutside(false);
                startSignIn();

            }
        });
    }
    private void startSignIn()
    {
        String emailId = email.getText().toString();
        String pass = password.getText().toString();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        if(TextUtils.isEmpty(emailId) || TextUtils.isEmpty(pass))
        {
            Toast.makeText(Login_Page.this,"Please fill out both the fields",Toast.LENGTH_LONG).show();
            progress.dismiss();
        }
        else
        {
            auth.signInWithEmailAndPassword(emailId,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    {
                        if (!task.isSuccessful())
                        {
                            progress.hide();

                            try
                            {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthInvalidUserException invalidEmail)
                            {
                                Toast.makeText(Login_Page.this,"Invalid email address",Toast.LENGTH_LONG).show();
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                            {
                                Toast.makeText(Login_Page.this,"Wrong Password",Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e)
                            {}
                        }
                        else
                        {
                            user = auth.getCurrentUser();
                            if(!user.isEmailVerified())
                            {
                                Toast.makeText(Login_Page.this,"Email not verified",Toast.LENGTH_LONG).show();
                                progress.dismiss();
                            }
                            else
                            {
                                Toast.makeText(Login_Page.this, "Done", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Login_Page.this, Welcome.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                progress.dismiss();
                                editor.putBoolean("signin", true);
                                editor.commit();
                                finish();
                            }
                        }
                    }
                }
            });
        }
    }
}
