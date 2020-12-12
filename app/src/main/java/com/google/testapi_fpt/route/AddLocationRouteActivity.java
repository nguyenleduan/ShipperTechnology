package com.google.testapi_fpt.route;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.testapi_fpt.DataClass;
import com.google.testapi_fpt.R;
import com.google.testapi_fpt.location.LocationActivity;
import com.google.testapi_fpt.model.ProgramModel;
import com.google.testapi_fpt.thread.DataMaps;

import java.io.IOException;
import java.util.List;

public class AddLocationRouteActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    GoogleMap mMap, mMarker;
    EditText edt;
    ConstraintLayout mConstraintLayout;
    static Marker mMarkerSearch, markerClick;
    SupportMapFragment mapFragment;
    Button bt;
    DataClass dataClass = new DataClass();
    DatabaseReference mData;
    static String mPhone,mAddress ="Chưa cập nhật";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location_route);
        Intent integer = getIntent();
        mPhone = integer.getStringExtra("keyPHONE");
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        edt = findViewById(R.id.edt);
        bt = findViewById(R.id.bt);
        edt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    CloseKeyboard();
                    SearchLocation(edt.getText().toString());
                    return true;
                }
                return false;
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogYesNo();
            }
        });
    }
    private void SaveLocation(String s){

        ProgramModel programModel = new ProgramModel(mAddress, dataClass.mProfile.IDUSER, mMarkerSearch.getPosition().latitude
                , "null",
                mMarkerSearch.getPosition().longitude, "null", "null", s);
        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("ProgramGroup").child(dataClass.mProfile.IDUSER).child(s).setValue(programModel);
        mData.child("Program").child(dataClass.mProfile.IDUSER).child(s).setValue(programModel);
    }
    private void BackActivity(String s) {
        Intent intent = new Intent();
        if(mMarkerSearch!=null){
            intent.putExtra("KeyLatitude",mMarkerSearch.getPosition().latitude);
            intent.putExtra("KeyLongitude",mMarkerSearch.getPosition().longitude);
            intent.putExtra("keyPhone",s);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void DialogYesNo() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_location_route);
        final EditText edt;
        Button btNo, btYes;
        edt = dialog.findViewById(R.id.edt);
        btNo = dialog.findViewById(R.id.btNo);
        btYes = dialog.findViewById(R.id.btYes);
        edt.setText(mPhone);
        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt.getText().toString().equals("")) {
                    Toast.makeText(AddLocationRouteActivity.this, "Bổ sung thêm số điện thoại", Toast.LENGTH_SHORT).show();
                } else {
                    SaveLocation(edt.getText().toString());
                    BackActivity(edt.getText().toString());
                    DataMaps dataMaps = new DataMaps();
                    dataMaps.start();
                    dialog.dismiss();
                }
            }
        });
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackActivity(edt.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void SearchLocation(String location) {
        if (mMarkerSearch != null) {
            mMarkerSearch.remove();
        }
        Double dLat, dLong;
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(AddLocationRouteActivity.this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);
            if (addressList.size() != 0) {
                Address ad = addressList.get(0);
                dLat = ad.getLatitude();
                dLong = ad.getLongitude();
                LatLng latLne = new LatLng(dLat, dLong);
                mMarkerSearch = mMap.addMarker(new MarkerOptions().position(latLne).title(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_search)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLne, 15));
                mAddress=location;
                bt.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Tìm kiếm lỗi !", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "tìm kiếm lỗi !!", Toast.LENGTH_SHORT).show();
        }

    }

    private void CloseKeyboard() {
        View view = AddLocationRouteActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMarker = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.getUiSettings().setMapToolbarEnabled(true);
//        15.7128638,106.5815815,6z
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(15.7128638, 106.5815815), 6));
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mMarkerSearch != null) {
                    mMarkerSearch.remove();
                }
                bt.setVisibility(View.VISIBLE);
                mMarkerSearch = mMap.addMarker(new MarkerOptions().position(latLng).title("Vị trí đánh dấu").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_search)));

            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}