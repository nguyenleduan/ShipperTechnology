package com.google.testapi_fpt.route.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.testapi_fpt.R;
import com.google.testapi_fpt.route.RouteActivity;
import com.google.testapi_fpt.route.model.RouteModel;

import java.util.ArrayList;
import java.util.List;

public class ListViewitem  extends BaseAdapter {
    private Context mycontext;
    private int myLayout;
    private static ArrayList<RouteModel> arry;
    RouteActivity routeActivity  = new RouteActivity() ;
    public ListViewitem(Context context, int layout, ArrayList<RouteModel> mLists) {
        this.mycontext = context;
        this.myLayout = layout;
        this.arry = mLists;
    }

    @Override
    public int getCount() {
        return arry.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(myLayout, null);
        TextView txtphone ,txtmunber;
        txtphone = convertView.findViewById(R.id.txtphone);
        txtmunber = convertView.findViewById(R.id.txtmunber);
        txtmunber.setText(position+1+"");
        final ImageView imgRoute,imgCallPhone,imgDelete;
        imgCallPhone = convertView.findViewById(R.id.imgCallPhone);
        imgDelete = convertView.findViewById(R.id.imgdelete);
        imgRoute = convertView.findViewById(R.id.imgroute);
        /// set content
        txtphone.setText(arry.get(position).getPhone());
        imgCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCallPhone.setImageResource(R.drawable.ic_call_phone_finish);
                String s = "tel:" + arry.get(position).getPhone();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(s));
                mycontext.startActivity(intent);

            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arry.remove(position);
                Log.e("LOGE","ziee"+arry.size());
                notifyDataSetChanged();
                routeActivity.arrRoute = arry;
            }
        });
        /// set img
        if(position==0){
            imgRoute.setImageResource(R.drawable.ic_pin_route_begin);
        }else if(position==(arry.size()-1)){
            imgRoute.setImageResource(R.drawable.ic_pin_route_end);
        }
        // code
        return convertView;
    }


}