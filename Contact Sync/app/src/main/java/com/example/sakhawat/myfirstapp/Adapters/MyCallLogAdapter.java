package com.example.sakhawat.myfirstapp.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sakhawat.myfirstapp.Models.CallLogs;
import com.example.sakhawat.myfirstapp.R;

import java.util.ArrayList;

public class MyCallLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity context;
    ArrayList<CallLogs> items;
    private OnItemClickListener mListener;
    int size=0;

    public MyCallLogAdapter(Activity context, ArrayList<CallLogs> items) {
        this.context = context;
        this.items = items;
        size=items.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View row = layoutInflater.inflate(R.layout.custom_call_log, viewGroup, false);
        Item item = new Item(row,mListener);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        final CallLogs temp = items.get(size-i-1);
        ((Item) viewHolder).textname.setText(temp.getName()+"\n"+temp.getNumber());
        ((Item) viewHolder).textduration.setText(temp.getDuration());
        ((Item) viewHolder).textdate.setText(temp.getData());
        ((Item) viewHolder) .imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:" + temp.getNumber())).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.CALL_PHONE},10);
                }
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(call);

            }
        });
        ((Item) viewHolder).imageViewmes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=temp.getNumber();

                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("sms:"+phone));
                i.putExtra("sms_body","");
                context.startActivity(i);
            }
        });




    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener{
        void onItemClickListener(int pos);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    public static class Item extends RecyclerView.ViewHolder {
        TextView textname,textdate,textduration;
        ImageView imageView,imageViewmes;

        public Item(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textname = itemView.findViewById(R.id.tvname);
            textdate=itemView.findViewById(R.id.tvdate);
            textduration=itemView.findViewById(R.id.tvduration);
            imageView = itemView.findViewById(R.id.call);
            imageViewmes=itemView.findViewById(R.id.ivmessage1);
        }
    }
}
