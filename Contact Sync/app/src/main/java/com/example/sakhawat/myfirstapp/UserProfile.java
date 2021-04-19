package com.example.sakhawat.myfirstapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sakhawat.myfirstapp.Models.UserInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity {

    ImageView imageView;

    FirebaseStorage firebaseStorage;
    TextView profilename,usermail,userage;
    Button button;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

       UISetting();
      getDataFromFirebase();
        StorageReference storageReference = firebaseStorage.getReference(firebaseAuth.getUid());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(imageView);
                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this,UpdateProfile.class));
            }
        });
    }

    private void getDataFromFirebase() {
        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    profilename.setText("Name : " + userInfo.getName());
                    usermail.setText("Phone no: " + userInfo.getPhn());
                    userage.setText("Age :"+userInfo.getAge());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void UISetting() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView=(ImageView) findViewById(R.id.ivProfileImage);
        profilename=(TextView) findViewById(R.id.tvName);
        usermail=(TextView) findViewById(R.id.tvEmail);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        button=findViewById(R.id.button2);
        userage=findViewById(R.id.tvage);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
