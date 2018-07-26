package com.example.android.mnnitcentral;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class Create_Account extends AppCompatActivity {

    private EditText emailid;
    private EditText pass;
    private EditText userid;
    private Button submit;
    private Toolbar toolbar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__account);
        toolbar =findViewById(R.id.create_account_page);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        submit = findViewById(R.id.create_account);
        mAuth = FirebaseAuth.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }
    private void createAccount()
    {
        emailid = findViewById(R.id.email_create);
        pass = findViewById(R.id.password_create);
        userid = findViewById(R.id.username_create);
        final String email = emailid.getText().toString();
        final String password  = pass.getText().toString();
        final String username = userid.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(username))
        {
            Toast.makeText(Create_Account.this,"Inappropriate",Toast.LENGTH_LONG).show();
        }
        else
        {
            final Intent intent = new Intent(Create_Account.this, MoreInfo.class);
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful())
                    {
                        try
                        {
                            throw task.getException();
                        }
                        catch (FirebaseAuthWeakPasswordException weakPassword)
                        {
                            Toast.makeText(Create_Account.this,"Weak Password",Toast.LENGTH_LONG).show();
                        }
                        catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                        {
                            Log.d("malformed email", "onComplete: malformed_email");

                            // TODO: Take your action
                        }
                        catch (FirebaseAuthUserCollisionException existEmail)
                        {
                            Toast.makeText(Create_Account.this,"Email Already in use",Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e)
                        {
                            Log.d("hi", "onComplete: " + e.getMessage());
                        }
                        finally {
                            startActivity(new Intent(Create_Account.this,Create_Account.class));
                            finish();
                        }

                    }
                    else
                    {
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    }
                }
            });




        }
    }
}
