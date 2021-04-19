package com.example.sakhawat.myfirstapp.freg;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sakhawat.myfirstapp.Adapters.Myadapter;
import com.example.sakhawat.myfirstapp.Models.PhoneData;
import com.example.sakhawat.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentContactDatabase extends Fragment {

    private View v;
    RecyclerView recyclerView;
    TextView textView;
    Myadapter mAdapter;
    ProgressBar progressBar;
    ArrayList<PhoneData> mylist = new ArrayList<>();
    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;
    FloatingActionButton fab;
    Boolean result;
    public FragmentContactDatabase() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.frag_phone_database,container,false);
        UISetting();
        checkpremission();
        refreshDatabase();

        return v;
    }

    private void refreshDatabase() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                new DataFromFirebase().execute("");
            }
        });
    }

    private void UISetting() {
        recyclerView = v.findViewById(R.id.rvPhonedatabase);
        textView = v.findViewById(R.id.tvmessage);
        progressBar = v.findViewById(R.id.progressBar2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fab=v.findViewById(R.id.fabrefresh);
    }

    private class DataFromFirebase extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... strings) {
            String temp;
            getPhoneNumberFromFirebase();
            if(mylist.size()>0)
            {
                temp="Mylist has data";
            }
            else {
                temp="Mylist has no data";
            }
            return temp;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("Mylist has data"))
            {

            }

        }

        private void getPhoneNumberFromFirebase() {
            DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("mylis").getValue() == null) {
                        progressBar.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("Data not yet Syncronized");


                    } else {
                        for (DataSnapshot dsp : dataSnapshot.child("mylis").getChildren()) {
                            mylist.add(dsp.getValue(PhoneData.class));

                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        mAdapter=new Myadapter(getActivity(),mylist);
                        recyclerView.setAdapter(mAdapter);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }
    }
    private void checkpremission() {

        if(ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED)
        {
            new DataFromFirebase().execute("");
        }
        else {
            requestContactsPremission();
        }
    }

    private void requestContactsPremission() {

        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CALL_PHONE))
        {
            new AlertDialog.Builder(getContext()).setTitle("permission needed ")
                    .setMessage("This permission is needed to syncronise your contacts number and  name")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),new String[] {
                                    android.Manifest.permission.CALL_PHONE},7034);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }else {
            ActivityCompat.requestPermissions(getActivity(),new String[] { Manifest.permission.CALL_PHONE},7034);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==7034)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                new DataFromFirebase().execute("");
            }
            else {
                Toast.makeText(getContext(),"Please grant call permission",Toast.LENGTH_LONG).show();


            }
        }

    }
}

