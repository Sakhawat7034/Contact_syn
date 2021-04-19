package com.example.sakhawat.myfirstapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    Button signup;
    EditText password,email;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String name,mail,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        UISetting();

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 pass=password.getText().toString().trim();
                mail=email.getText().toString().trim();

                if(validData())
                {
                    progressDialog.setMessage("checking info");
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();
                             sentEmailVarification();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Sign Up failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
    public void UISetting()
    {
        password=findViewById(R.id.etPassword1);
        signup=findViewById(R.id.btsignup);
        email=findViewById(R.id.etemail);
    }

    public Boolean validData()
    {
        Boolean result=false;

        if( pass.length()<6 || mail.equals(""))
        {

                Toast.makeText(getApplicationContext(),"Enter valid data",Toast.LENGTH_SHORT).show();

        }
        else
        {
            result=true;
        }
        return result;
    }

    public void sentEmailVarification()
    {
        final FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Sign Up Successfully, Varification Email Sent!",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Varification Email Didn't sent!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
