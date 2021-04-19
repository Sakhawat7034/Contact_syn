package com.example.sakhawat.myfirstapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sakhawat.myfirstapp.Models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UploadUserInfo extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ImageView profilepic;
    Button button;
    EditText etname,etage,etphn;
    FirebaseDatabase firebaseDatabase;
    String name,phn,age;
    Uri imagePath;
    public  static int PICK_IMAGE=123;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                profilepic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_userinfo);

        UISetting();
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentDataToDatabase();
                finish();
                startActivity(new Intent(UploadUserInfo.this,UserProfile.class));
            }
        });
    }

    private void UISetting() {
        etname=(EditText) findViewById(R.id.etname);
        etage=(EditText) findViewById(R.id.etage);
        etphn=(EditText) findViewById(R.id.etphn);
        profilepic=(ImageView) findViewById(R.id.ivProfileImage1);
        button=(Button) findViewById(R.id.button);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

    }


    private void sentDataToDatabase() {
         name=etname.getText().toString();
         age=etage.getText().toString().trim();
         phn=etphn.getText().toString().trim();
         if(name.equals("")&&age.equals("")&&phn.equals("")&&imagePath==null)
         {
             String temp="not yet set";
             DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
             UserInfo userInfo=new UserInfo(temp,temp,temp);
             databaseReference.setValue(userInfo);
         }
         else {
             DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
             UserInfo userInfo = new UserInfo(name, phn, age);
             databaseReference.setValue(userInfo);
             storageReference=firebaseStorage.getReference(firebaseAuth.getUid());
             storageReference.putFile(imagePath);
         }
    }


}
