package com.example.sakhawat.myfirstapp.freg;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sakhawat.myfirstapp.Adapters.MyCallLogAdapter;
import com.example.sakhawat.myfirstapp.HomePage;
import com.example.sakhawat.myfirstapp.Models.CallLogs;
import com.example.sakhawat.myfirstapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentCallLog extends Fragment {

    private RecyclerView recyclerView;
    public static int p;
    private FloatingActionButton fab;
    private View v;
    EditText et;
    Button bt;
    private ArrayList<CallLogs> mylist=new ArrayList<>();
    public FragmentCallLog() {
        p=1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.frag_call_log,container,false);
        recyclerView=v.findViewById(R.id.rvCallLog);
        fab=v.findViewById(R.id.fabnumber);
        et=v.findViewById(R.id.etcall);
        bt=v.findViewById(R.id.btcall);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getCallLog();
        final Boolean[] tem = {false};
       MyCallLogAdapter myCallLogAdapter= new MyCallLogAdapter(getActivity(),mylist);
        recyclerView.setAdapter(myCallLogAdapter);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerView.getVisibility()==View.INVISIBLE) {
                        getActivity().recreate();
                    }

                    recyclerView.setVisibility(View.INVISIBLE);
                    et.setVisibility(View.VISIBLE);
                    bt.setVisibility(View.VISIBLE);
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String phn = et.getText().toString().trim();
                            recyclerView.setVisibility(View.VISIBLE);
                            et.setVisibility(View.INVISIBLE);
                            bt.setVisibility(View.INVISIBLE);
                            Intent call = new Intent(Intent.ACTION_CALL);
                            call.setData(Uri.parse("tel:" + phn)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 10);
                            }
                            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            p=0;
                            startActivity(call);
                            return;
                        }

                    });

                }


            });

        Log.e("check","value of P "+p);
        if(p==0)
        {

                getActivity().recreate();

        }

        return v;
    }

    public void   getCallLog() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALL_LOG}, 12);
        }
        Cursor cursor = getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE );
        int number=cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int nameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {

            Date datel= new Date(Long.valueOf(cursor.getString(date)));
            String name=cursor.getString(nameIndex);

            int i=Integer.valueOf(cursor.getString(duration));
            if(name == null)
            {
                name="No Name";
            }
            mylist.add(new CallLogs(name,cursor.getString(number),((int) i/60)+":"+i%60 , datel.toString()));



        }

    }

}
