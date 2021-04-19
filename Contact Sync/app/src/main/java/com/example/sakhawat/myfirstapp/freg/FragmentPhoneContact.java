package com.example.sakhawat.myfirstapp.freg;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sakhawat.myfirstapp.Adapters.MyCallLogAdapter;
import com.example.sakhawat.myfirstapp.Adapters.Myadapter;
import com.example.sakhawat.myfirstapp.Models.CallLogs;
import com.example.sakhawat.myfirstapp.Models.PhoneData;
import com.example.sakhawat.myfirstapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;


public class FragmentPhoneContact extends Fragment {


    private RecyclerView recyclerView;
    private View v;
    HashSet<String> dupset=new HashSet<>();
    private ArrayList<PhoneData> mylist=new ArrayList<>();
    private Myadapter mAdapter;
  FloatingActionButton fabsynch,fabadd;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public FragmentPhoneContact() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fram_phone_contact,container,false);
        UISetting();
        getDataFromPhoneBook();
        fabsynch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoneNumberFromFirebase();

                Collections.sort(mylist, new Comparator<PhoneData>() {
                    @Override
                    public int compare(PhoneData o1, PhoneData o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                databaseReference.child("mylis").setValue(mylist);
                Snackbar.make(v,"Successfully synchronize",Snackbar.LENGTH_SHORT).show();
            }
        });
        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ContactsContract.Intents.Insert.ACTION);
                i.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                startActivity(i);
            }
        });
        return v;
    }

    private void UISetting() {
        recyclerView=v.findViewById(R.id.rvPhoneContact);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new Myadapter(getActivity(),mylist));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        fabsynch=v.findViewById(R.id.fabnumber);
        fabadd=v.findViewById(R.id.fabaddnumber);
    }


    private void getDataFromPhoneBook() {


        Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (name != null) {
                if (dupset.add(name)) {
                    mylist.add(new PhoneData(name, phoneNumber));
                }
            }

        }
        phones.close();
        mAdapter = new Myadapter(getActivity(), mylist);
        recyclerView.setAdapter(mAdapter);
    }


    private void getPhoneNumberFromFirebase() {
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("mylis").getValue() != null) {

                    for (DataSnapshot dsp : dataSnapshot.child("mylis").getChildren()) {
                    PhoneData tem=dsp.getValue(PhoneData.class);
                    if(dupset.add(tem.getName()));
                        mylist.add(tem);
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
}


