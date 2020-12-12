package com.google.testapi_fpt.route;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.testapi_fpt.MovableFloatingActionButton;
import com.google.testapi_fpt.R;

public class DetailRouteActivity extends AppCompatActivity {
    MovableFloatingActionButton fab;
    RelativeLayout mRelativeLayout ;
    static Boolean mCheckFab = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_route);
        mRelativeLayout = findViewById( R.id.mRelativeLayout);
        fab = findViewById( R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckFab){
                    mCheckFab = false;
                    mRelativeLayout.setVisibility(View.GONE);
                }else{
                    mCheckFab=true;
                    mRelativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}