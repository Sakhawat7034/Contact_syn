package com.example.sakhawat.myfirstapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sakhawat.myfirstapp.Models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText password;
    TextView register,forgetpass;
    EditText username;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String pname,pmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UISetting();

        firebaseAuth= FirebaseAuth.getInstance();

        progressDialog= new ProgressDialog(this);

        FirebaseUser user= firebaseAuth.getCurrentUser();

        if(user!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,HomePage.class));

        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=username.getText().toString().trim();
                String pass=password.getText().toString().trim();
                if(name.equals("")||pass.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Necessary Information",Toast.LENGTH_SHORT).show();
                }
                else {

               validData(name,pass);}
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PasswordActivity.class));
            }
        });

    }

    public void UISetting()
    {
        username=findViewById(R.id.etUsername);
        password=findViewById(R.id.etPassword);
        login=findViewById(R.id.btLogin);
        register=findViewById(R.id.tvRegister);
        forgetpass=findViewById(R.id.tvforgotpass);

    }
    public void validData(String name,String pass)
    {
        progressDialog.setMessage("Checking login info");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(name,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                    boolean emailflag=firebaseUser.isEmailVerified();
                    if(emailflag && ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CALL_LOG)== PackageManager.PERMISSION_GRANTED)


                    {
                        progressDialog.dismiss();
                        getDataFromFirebase();
                    }
                    else if(!emailflag&&ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CALL_LOG)== PackageManager.PERMISSION_GRANTED){
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"please varify your email",Toast.LENGTH_LONG).show();
                    }
                    else if(emailflag && ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Please give premissions",Toast.LENGTH_LONG).show();
                        checkpremission();
                    }

                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Invalid id or password",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getDataFromFirebase() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(UserInfo.class)==null)
                {


                    startActivity(new Intent(MainActivity.this,UploadUserInfo.class));

                }
                else {
                    startActivity(new Intent(MainActivity.this,HomePage.class));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void checkpremission() {

        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED)
        {
            requestContactsPremission();
        }
        else {
            getDataFromFirebase();
        }

    }

    private void requestContactsPremission() {

        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_CONTACTS)&&
                ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.CALL_PHONE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_CALL_LOG))
        {
            new AlertDialog.Builder(MainActivity.this).setTitle("permission needed ")
                    .setMessage("This permission is needed to syncronise your contacts number and  name")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[] {android.Manifest.permission.READ_CONTACTS
                                    , android.Manifest.permission.CALL_PHONE
                                    , android.Manifest.permission.READ_CALL_LOG},7034);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CALL_LOG},7034);

        }
    }

}
