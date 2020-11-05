package com.google.testapi_fpt.thread;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.testapi_fpt.DataClass;
import com.google.testapi_fpt.WelcomeActivity;
import com.google.testapi_fpt.model.ProgramModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DataMaps extends Thread {
    DatabaseReference mData,mDataGroup;
    DataClass dataClass = new DataClass();
    @Override
    public void run( ) {
        dataClass.arrProgram.clear();
        dataClass.arrProgramGroup.clear();
        mData = FirebaseDatabase.getInstance().getReference();
        mDataGroup = FirebaseDatabase.getInstance().getReference();
//        mData = FirebaseDatabase.getInstance().getReference().child("ProgramGroup").child("000001");
        dataClass.arrProgram.clear();
//        mData.child("ProgramGroup").child("G00001").addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            try{
//                    ProgramModel models = snapshot.getValue(ProgramModel.class);
//                    ProgramModel pro = new ProgramModel(models.Address, models.IDUSER, models.Latitude, models.LinkAvatar, models.Longitude, models.Name,
//                            models.Note, models.NumberPhone);
//                    mArrActivity.arr.add(pro);
//                }catch (Exception e){
//                    Log.e("Exception",e.toString());
//                }
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//    });
        Log.e("Start","DataMaps Start");
        if(dataClass.mProfile!= null){
            mData.child("Program").child(dataClass.mProfile.IDUSER).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try{
                        ProgramModel models = snapshot.getValue(ProgramModel.class);
                        ProgramModel pro = new ProgramModel(models.Address, models.IDUSER, models.Latitude, models.LinkAvatar, models.Longitude, models.Name,
                                models.Note, models.NumberPhone);
                        dataClass.arrProgram.add(pro);
                    }catch (Exception e){
                        Log.e("Exception",e.toString());
                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            mDataGroup.child("ProgramGroup").child(dataClass.mProfile.IDUSER).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try{
                        ProgramModel models = snapshot.getValue(ProgramModel.class);
                        ProgramModel pro = new ProgramModel(models.Address, models.IDUSER, models.Latitude, models.LinkAvatar, models.Longitude, models.Name,
                                models.Note, models.NumberPhone);
                        dataClass.arrProgramGroup.add(pro);
                    }catch (Exception e){
                        Log.e("Exception",e.toString());
                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        super.run();
    }
//

}
