package com.google.testapi_fpt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.testapi_fpt.DataClass;
import com.google.testapi_fpt.R;
import com.google.testapi_fpt.location.LocationActivity;
import com.google.testapi_fpt.route.RouteActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class HomeFragment extends Fragment {
    RelativeLayout rl1,rl2;
    DataClass dataClass = new DataClass();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Log.e("LOGE",dataClass.mProfile.IDUSER.toString());
        rl1 = root.findViewById(R.id.rl1);
        rl2 = root.findViewById(R.id.rl2);
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                startActivity(intent);
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RouteActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}
