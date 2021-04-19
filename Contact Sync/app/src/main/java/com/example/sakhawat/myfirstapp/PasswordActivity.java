package com.example.sakhawat.myfirstapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {

    EditText etemail;
    Button btresetpassword;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        etemail=(EditText) findViewById(R.id.etPasswordReset);
        btresetpassword=(Button)findViewById(R.id.btnPasswordReset);
        firebaseAuth=FirebaseAuth.getInstance();

        btresetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=etemail.getText().toString().trim();

                if(email.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter your Email id",Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Password reset email sendt!",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PasswordActivity.this,MainActivity.class));
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Error in sending email!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }
}
