package com.google.testapi_fpt;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.testapi_fpt.fragment.GroupFragment;
import com.google.testapi_fpt.fragment.HomeFragment;
import com.google.testapi_fpt.fragment.ProfileFragment;
import com.google.testapi_fpt.model.ProgramModel;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bt_navigation;
    SpaceNavigationView spaceNavigationView;
    DataClass dataClass = new DataClass();
    DatabaseReference mData;
    private  static  final  int INSTALL_FAILED_USER_RESTRICTED = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spaceNavigationView = findViewById(R.id.space);
        ProgramModel programModel = new ProgramModel("null", dataClass.mProfile.IDUSER, 102.656565, "null",
                102.3333, "null", "null", "G0001");
        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("ProgramGroup").child(dataClass.mProfile.IDUSER).child("G0001").setValue(programModel);
        mData.child("Program").child(dataClass.mProfile.IDUSER).child("G0001").setValue(programModel);
        spaceNavigationView.setCentreButtonSelectable(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            // true
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    INSTALL_FAILED_USER_RESTRICTED);
        }

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();
                Fragment fragment = null;
                 if(itemIndex==0){
                     fragment = new GroupFragment();
                     getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
                 }else if(itemIndex==1){
                     fragment = new ProfileFragment();
                     getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
                 }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
//                Toast.makeText(MainActivity.this, itemIndex + " 3s" + itemName, Toast.LENGTH_SHORT).show();
            }
        });
        // load data

        final ProgressDialog progressDialog = ProgressDialog.show(this, "Xin chờ xíu", "Đang load dữ liệu...", true, false);
        progressDialog.show();
        spaceNavigationView.addSpaceItem(new SpaceItem("GROUP", R.drawable.icon_group));
        spaceNavigationView.addSpaceItem(new SpaceItem("PROFILE", R.drawable.icon_profile));
        final Timer time =new Timer();
        final int[] count = {0};
        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(count[0] < 10){
                            if(dataClass.arrProgram.size()>0){
                                progressDialog.dismiss();
                                time.cancel();
                            }else if(count[0] ==10){
                                time.cancel();
                                finish();

                            }
                        }
                        count[0]++;
                    }
                });
            }
        }, 1000, 1000);
    }

//

}