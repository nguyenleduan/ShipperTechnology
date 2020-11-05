package com.google.testapi_fpt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.testapi_fpt.R;
import com.google.testapi_fpt.model.ProgramModel;

import java.util.ArrayList;

public class SearchListViewAdapter  extends BaseAdapter {
    private Context mycontext;
    private int myLayout;
    private ArrayList<ProgramModel> arr;

    public SearchListViewAdapter(Context context, int layout,ArrayList<ProgramModel>  list ) {
        this.mycontext = context;
        this.myLayout = layout;
        this.arr = list;
    }

    @Override
    public int getCount() {
        return arr.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(myLayout, null);
        TextView tvPhoneLVSearch,tvAddressLVSearch;
        tvPhoneLVSearch = convertView.findViewById(R.id.tvPhoneLVSearch);
        tvAddressLVSearch = convertView.findViewById(R.id.tvAddressLVSearch);
        tvAddressLVSearch.setText(arr.get(position).Address);
        tvPhoneLVSearch.setText(arr.get(position).NumberPhone);
        // code
        return convertView;
    }


}