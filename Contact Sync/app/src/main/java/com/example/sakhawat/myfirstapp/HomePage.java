package com.example.sakhawat.myfirstapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sakhawat.myfirstapp.Adapters.ViewPageAdapter;
import com.example.sakhawat.myfirstapp.Models.UserInfo;
import com.example.sakhawat.myfirstapp.freg.FragmentCallLog;
import com.example.sakhawat.myfirstapp.freg.FragmentContactDatabase;
import com.example.sakhawat.myfirstapp.freg.FragmentPhoneContact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Button phonebook,databasedata;
    FloatingActionButton floatingActionButton;
     RecyclerView RecyclerView;
     final int[] ICONS={R.drawable.ic_call_black_24dp,R.drawable.ic_person,R.drawable.ic_backup_black_24dp};
     TabLayout tabLayout;
     ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        UISetting();

    }

    private void UISetting() {
        firebaseAuth=FirebaseAuth.getInstance();

        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.viewpager);


        ViewPageAdapter adapter=new ViewPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentCallLog(),"Log");
        adapter.addFragment(new FragmentPhoneContact(),"Contact");
        adapter.addFragment(new FragmentContactDatabase(),"Backup Contact");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i=0;i<tabLayout.getTabCount();i++)
        {
            TabLayout.Tab tab=tabLayout.getTabAt(i);
            tab.setIcon(ICONS[i]);
        }
        checkpremission();
    }
    private void checkpremission() {

        if(ContextCompat.checkSelfPermission(HomePage.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED &&ContextCompat.checkSelfPermission(HomePage.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(HomePage.this, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED)
        {
            requestContactsPremission();
        }

    }

    private void requestContactsPremission() {

        if(ActivityCompat.shouldShowRequestPermissionRationale(HomePage.this,Manifest.permission.READ_CONTACTS)&&ActivityCompat.shouldShowRequestPermissionRationale(HomePage.this,Manifest.permission.CALL_PHONE) && ActivityCompat.shouldShowRequestPermissionRationale(HomePage.this,Manifest.permission.READ_CALL_LOG))
        {
            new AlertDialog.Builder(HomePage.this).setTitle("permission needed ")
                    .setMessage("This permission is needed to syncronise your contacts number and  name")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(HomePage.this,new String[] {Manifest.permission.READ_CONTACTS
                                    ,Manifest.permission.CALL_PHONE
                            ,Manifest.permission.READ_CALL_LOG},7034);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }else {
            ActivityCompat.requestPermissions(HomePage.this,new String[] {Manifest.permission.READ_CONTACTS,Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CALL_LOG},7034);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logoutMenu:
            {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(HomePage.this,MainActivity.class));
            }
            break;
            case R.id.ProfileMenu:
            {
                getDataFromFirebase();
            }
            break;
            case R.id.refresh:
            {
               recreate();
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void getDataFromFirebase() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(UserInfo.class)==null)
                {
                    Toast.makeText(getApplicationContext(),"Data not yet uplod",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomePage.this,UploadUserInfo.class));

                }
                else {

                    startActivity(new Intent(HomePage.this,UserProfile.class));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });
    }

}
