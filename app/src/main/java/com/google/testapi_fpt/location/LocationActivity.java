package com.google.testapi_fpt.location;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.testapi_fpt.DataClass;
import com.google.testapi_fpt.MainActivity;
import com.google.testapi_fpt.MovableFloatingActionButton;
import com.google.testapi_fpt.R;
import com.google.testapi_fpt.WelcomeActivity;
import com.google.testapi_fpt.adapter.SearchListViewAdapter;
import com.google.testapi_fpt.login.LoginActivity;
import com.google.testapi_fpt.model.ProgramModel;
import com.google.testapi_fpt.thread.DataMaps;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LocationActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    GoogleMap mMap, mMarker;
    static Marker mMarkerLongclick, mMarkerLongclicklistener, mMarkerSearch;
    static ArrayList<Marker> markerList = new ArrayList<>();
    SupportMapFragment mapFragment;
    private FusedLocationProviderClient client;
    FirebaseDatabase mDataFirebase;
    DatabaseReference mData;
    Animation aniUp, aniDow;
    MovableFloatingActionButton movableFloatingActionButton ;
    Spinner spinner;
    ListView lvSearchLocation;
    static LatLng mLatngLongClick;
    static ArrayList<ProgramModel> arrmodels = new ArrayList<>();
    static ArrayList<ProgramModel> arrmodelsSearch = new ArrayList<>();
    ProgressDialog progressDialog;
    final private int mTimeHandler = 2000;
    DataClass activity = new DataClass();
    static SearchListViewAdapter searchView;
    ImageView imgSearchLocation;
    EditText edt, edtSeachGoogleMaps;
    static ArrayList<ProgramModel> arrSearchProgram = new ArrayList<>();
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        inFindview();
        Log.e("Logs", activity.mProfile.IDUSER);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        mDataFirebase = FirebaseDatabase.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        progressDialog = ProgressDialog.show(LocationActivity.this, "Xin chờ xíu", "Đang load dữ liệu...", true, false);
        if (activity.arrProgram.size() > 0) {
            progressDialog.dismiss();
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Chưa có dữ liệu !", Toast.LENGTH_SHORT).show();
            finish();
        }

//        for(int i = 0 ; i<=5;i++){
//            AddLocation("","54654",10.834957,"check",120.26262,"Test",
//                    "Note","096"+i+2+"441"+i+"0"+i+3);
//        }
//        checkdataMaps();
    }

    private void SearchLocation(String location) {
        if (mMarkerSearch != null) {
            mMarkerSearch.remove();
        }
        Double dLat, dLong;
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(LocationActivity.this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address ad = addressList.get(0);
        dLat = ad.getLatitude();
        dLong = ad.getLongitude();
        LatLng latLne = new LatLng(dLat, dLong);
        mMarkerSearch = mMap.addMarker(new MarkerOptions().position(latLne).title(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_search)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLne, 15));
    }

    private void CloseKeyboard() {
        View view = LocationActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void ListViewSearch(final ArrayList<ProgramModel> arrlist) {
        lvSearchLocation.setVisibility(View.VISIBLE);
        searchView = new SearchListViewAdapter(LocationActivity.this, R.layout.listview_search, arrlist);
        searchView.notifyDataSetChanged();
        lvSearchLocation.setAdapter(searchView);
        lvSearchLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MoveCameraSearch(new LatLng(arrlist.get(position).Latitude, arrlist.get(position).Longitude), 15);
                // close key board
                CloseKeyboard();
                edt.setText(null);
                // GONE Listview
                lvSearchLocation.setVisibility(View.GONE);
            }
        });
    }

    private void ShowDetailLocation(final Marker marker) {
        final Dialog dialog = new Dialog(LocationActivity.this);
        dialog.setContentView(R.layout.dialog_detail_location_marker);
        final Button btCloseDetailLocationDialgo, btCallNowDetailLocationDialgo;
        TextView txtNameDetailLocationDialog, txtAdressDetailLocationDialog, txtMunberPhoneDetailLocationDialog, txtNoteDetailLocationDialog;
        btCloseDetailLocationDialgo = dialog.findViewById(R.id.btCloseDetailLocationDialgo);
        btCallNowDetailLocationDialgo = dialog.findViewById(R.id.btCallNowDetailLocationDialgo);
        txtNameDetailLocationDialog = dialog.findViewById(R.id.txtNameDetailLocationDialog);
        txtAdressDetailLocationDialog = dialog.findViewById(R.id.txtAdressDetailLocationDialog);
        txtMunberPhoneDetailLocationDialog = dialog.findViewById(R.id.txtMunberPhoneDetailLocationDialog);
        txtNoteDetailLocationDialog = dialog.findViewById(R.id.txtNoteDetailLocationDialog);

        for (int i = 0; i < activity.arrProgram.size(); i++) {
            if (activity.arrProgram.get(i).NumberPhone.equals(marker.getTitle())) {
                txtNameDetailLocationDialog.setText(activity.arrProgram.get(i).Name);
                txtAdressDetailLocationDialog.setText(activity.arrProgram.get(i).Address);
                txtMunberPhoneDetailLocationDialog.setText(activity.arrProgram.get(i).NumberPhone);
                txtNoteDetailLocationDialog.setText(activity.arrProgram.get(i).Note);
                i = activity.arrProgram.size();
            }
        }
        btCloseDetailLocationDialgo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btCloseDetailLocationDialgo.startAnimation(aniUp);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btCloseDetailLocationDialgo.startAnimation(aniDow);
                    dialog.dismiss();
                }
                dialog.cancel();
                return true;
            }
        });
        btCallNowDetailLocationDialgo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btCallNowDetailLocationDialgo.startAnimation(aniUp);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btCallNowDetailLocationDialgo.startAnimation(aniDow);
                    CallPhone(marker);
                    dialog.dismiss();
                }
                dialog.cancel();
                return true;
            }
        });
        //show
        dialog.show();
    }

    private void AddLocation(String address, String iduser, Double latiude, String linkavatar, Double longtiude,
                             String name, String note, String numberphone) {
        ProgramModel programModel = new ProgramModel(address, iduser, latiude, linkavatar,
                longtiude, name, note, numberphone);
//        mData.child("ProgramGroup").child(activity.mProfile.IDUSER).child(numberphone).setValue(programModel);
        mData.child("Program").child(activity.mProfile.IDUSER).child(numberphone).setValue(programModel);
//        activity.LoadData();
    }

    private void CallPhone(Marker marker) {
        if (ContextCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            // true
            ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String s = "tel:" + marker.getTitle();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(s));
            startActivity(intent);
        }
    }

    // datarealtime check maps
    private void checkdataMaps() {
        final DatabaseReference mDatax;
        mDatax = FirebaseDatabase.getInstance().getReference();
        mDatax.child("Program").child(activity.mProfile.IDUSER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    Toast.makeText(LocationActivity.this, "!!1!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }

                mDatax.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    private void DialogLongClickMarker(final Marker marker) {
        final Dialog dialog = new Dialog(LocationActivity.this);
        dialog.setContentView(R.layout.dialog_longclick_marker);
        final Button btDeleteDialogLogclick, btCancelDialogLogclick;
        btCancelDialogLogclick = dialog.findViewById(R.id.btCancelDialogLogclick);
        btDeleteDialogLogclick = dialog.findViewById(R.id.btDeleteDialogLogclick);
        btCancelDialogLogclick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btCancelDialogLogclick.startAnimation(aniUp);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btCancelDialogLogclick.startAnimation(aniDow);
                }
                dialog.cancel();
                return true;
            }
        });
        btDeleteDialogLogclick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btDeleteDialogLogclick.startAnimation(aniUp);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btDeleteDialogLogclick.startAnimation(aniDow);
                    marker.remove();
                    progressDialog = ProgressDialog.show(LocationActivity.this, "Xin chờ xíu", "Đang load dữ liệu...", true, false);

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//                Query applesQuery = ref.child("ProgramGourp").orderByChild("title").equalTo("Apple");
                    Query applesQuery = ref.child("Program").child(activity.mProfile.IDUSER).child(marker.getTitle());
                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                            DataMaps dataMaps = new DataMaps();
                            activity.arrProgram.clear();
                            dataMaps.start();
                            final Timer time = new Timer();
                            final int[] count = {0};
                            time.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (count[0] < 10) {
                                                if (activity.arrProgram.size() > 0) {
                                                    time.cancel();
                                                    progressDialog.dismiss();
                                                    LoadMarker();
                                                }
                                            } else {
                                                time.cancel();
                                                // note
                                            }
                                            count[0]++;
                                        }
                                    });
                                }
                            }, 1000, 1000);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(LocationActivity.this, "rd", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                dialog.dismiss();

                return true;
            }
        });
        dialog.show();
    }

    private void DialogAddProgram() {
        final Dialog dialog = new Dialog(LocationActivity.this);
        dialog.setContentView(R.layout.dialog_add_program);
        final EditText edtNumberPhoneAddLocation, edtNameLocationAddLocation, edtAddressAddLocation, edtNoteAddLocation;
        final Button btCancelAddLocation, btAddAddLocation;
        edtNumberPhoneAddLocation = dialog.findViewById(R.id.edtNumberPhoneAddLocation);
        edtNameLocationAddLocation = dialog.findViewById(R.id.edtNameLocationAddLocation);
        edtAddressAddLocation = dialog.findViewById(R.id.edtAddressAddLocation);
        edtNoteAddLocation = dialog.findViewById(R.id.edtNoteAddLocation);
        btCancelAddLocation = dialog.findViewById(R.id.btCancelAddLocation);
        btAddAddLocation = dialog.findViewById(R.id.btAddAddLocation);
        final String note = "null";
        btAddAddLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btAddAddLocation.startAnimation(aniUp);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btAddAddLocation.startAnimation(aniDow);
                }
                if (edtNumberPhoneAddLocation.getText().toString().equals("")) {
                    Toast.makeText(LocationActivity.this, "Chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
                } else if (edtAddressAddLocation.getText().toString().equals("")) {
                    Toast.makeText(LocationActivity.this, "Chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                } else if (edtNameLocationAddLocation.getText().toString().equals("")) {
                    Toast.makeText(LocationActivity.this, "Chưa nhập tên địa điểm", Toast.LENGTH_SHORT).show();
                } else if (edtNoteAddLocation.getText().toString().equals("")) {
                    // add program
                    AddLocation(edtAddressAddLocation.getText().toString(), activity.mProfile.IDUSER, mLatngLongClick.latitude, "LINK",
                            mLatngLongClick.longitude, edtNameLocationAddLocation.getText().toString(), note,
                            edtNumberPhoneAddLocation.getText().toString());
                    dialog.cancel();
                    progressDialog = ProgressDialog.show(LocationActivity.this, "Xin chờ xíu", "Đang load dữ liệu...", true, false);
                    DataMaps dataMaps = new DataMaps();
                    dataMaps.start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (activity.arrProgram.size() > 0) {
                                LoadMarker();
                            } else {
                                Toast.makeText(LocationActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                            mMarkerLongclick.remove();
                        }
                    }, 2000);
                } else {
                    // add programm
                    AddLocation(edtAddressAddLocation.getText().toString(), "002", mLatngLongClick.latitude, "LINK",
                            mLatngLongClick.longitude, edtNameLocationAddLocation.getText().toString(), edtNoteAddLocation.getText().toString(),
                            edtNumberPhoneAddLocation.getText().toString());
                    dialog.cancel();
                    progressDialog = ProgressDialog.show(LocationActivity.this, "Xin chờ xíu", "Đang load dữ liệu...", true, false);
                    final DataMaps dataMaps = new DataMaps();
                    dataMaps.start();
                    final Timer time = new Timer();
                    final int[] count = {0};
                    time.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (count[0] < 10) {
                                        if (activity.arrProgram.size() > 0) {
                                            progressDialog.dismiss();
                                            LoadMarker();
                                            mMarkerLongclick.remove();
                                            time.cancel();
                                        }
                                    } else if (count[0] == 10) {
                                        Toast.makeText(LocationActivity.this, "Lỗi load dữ liệu", Toast.LENGTH_SHORT).show();
                                        mMarkerLongclick.remove();
                                        progressDialog.dismiss();
                                        time.cancel();
                                    }
                                    count[0]++;
                                }
                            });
                        }
                    }, 1000, 1000);
                }
                return true;
            }
        });
        btCancelAddLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btCancelAddLocation.startAnimation(aniUp);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btCancelAddLocation.startAnimation(aniDow);
                }
                dialog.cancel();
                return true;
            }
        });
        dialog.show();
    }

    private void EvenView() {
        movableFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LocationActivity.this, "!", Toast.LENGTH_SHORT).show();
            }
        });
        edtSeachGoogleMaps.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    CloseKeyboard();
                    SearchLocation(edtSeachGoogleMaps.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void inFindview() {

        movableFloatingActionButton = findViewById(R.id.fab);
        edt = findViewById(R.id.edt);
        spinner = findViewById(R.id.spinner);
        aniUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        aniDow = AnimationUtils.loadAnimation(this, R.anim.scale_doew);
        String arrSearch[] = {"Tìm địa điểm của bạn", "Tìm trên Google maps"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, arrSearch);
        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    edtSeachGoogleMaps.setVisibility(View.GONE);
                    edt.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    edt.setVisibility(View.GONE);
                    edtSeachGoogleMaps.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        client = LocationServices.getFusedLocationProviderClient(this);
        lvSearchLocation = findViewById(R.id.lvSearchLocation);
        edtSeachGoogleMaps = findViewById(R.id.edtSeachGoogleMaps);
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrSearchProgram.clear();
                for (int i = 0; i < activity.arrProgram.size(); i++) {
                    if (String.valueOf(activity.arrProgram.get(i).NumberPhone).toLowerCase().contains(s.toString().toLowerCase())) {
                        arrSearchProgram.add(activity.arrProgram.get(i));
                    }
                }
                ListViewSearch(arrSearchProgram);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
//        bt = findViewById(R.id.bt);
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                for (int i =0;i<arrmodels.size();i++){
////                    Toast.makeText(LocationActivity.this, arrmodels.get(i).Address+"", Toast.LENGTH_SHORT).show();
////                }
//
//                if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                client.getLastLocation().addOnSuccessListener(LocationActivity.this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        onMyLocationClick(location);
//                        Toast.makeText(LocationActivity.this, location.getLatitude()+"", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
        EvenView();
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
        mMap.setMyLocationEnabled(true);
//        LatLng latLng =new LatLng(10.147283, 106.101777);
//        mMap.addMarker(new MarkerOptions().position(latLng).title("TEST"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (mMarkerLongclick != null) {
                    mMarkerLongclick.remove();
                }
                LatLng mlatLng = new LatLng(latLng.latitude, latLng.longitude);
                mMarkerLongclick = mMap.addMarker(new MarkerOptions().position(mlatLng).title("Địa điểm chưa xác định")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_question_mark)));
                DialogLongClick();
                mLatngLongClick = null;
                mLatngLongClick = latLng;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                ShowDetailLocation(marker);
            }
        });
        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                CallPhone(marker);
            }
        });
        LoadMarker();
    }

    // logclicklistener marker
    public abstract class LinkMarkerLongClickListener implements GoogleMap.OnMarkerDragListener {
        private int previousIndex = -1;
        private Marker cachedMarker = null;
        private LatLng cachedDefaultPostion = null;
        private ArrayList<Marker> markerList;
        private List<LatLng> defaultPostions;

        public LinkMarkerLongClickListener(ArrayList<Marker> markerList) {
            this.markerList = new ArrayList<>(markerList);
            defaultPostions = new ArrayList<>(markerList.size());
            for (Marker marker : markerList) {
                defaultPostions.add(marker.getPosition());
                marker.setDraggable(true);
            }
        }

        public abstract void onLongClickListener(Marker marker);

        @Override
        public void onMarkerDragStart(Marker marker) {
            onLongClickListener(marker);
            setDefaultPostion(markerList.indexOf(marker));
        }

        @Override
        public void onMarkerDrag(Marker marker) {
            setDefaultPostion(markerList.indexOf(marker));
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            setDefaultPostion(markerList.indexOf(marker));
        }


        private void setDefaultPostion(int markerIndex) {
            if (previousIndex == -1 || previousIndex != markerIndex) {
                cachedMarker = markerList.get(markerIndex);
                cachedDefaultPostion = defaultPostions.get(markerIndex);
                previousIndex = markerIndex;
            }
            cachedMarker.setPosition(cachedDefaultPostion);
        }
    }

    private void LoadMarker() {
        arrSearchProgram.clear();
        mMap.clear();
        mMarkerLongclicklistener = null;
        Log.e("LOGE", activity.arrProgram.size() + "");
        markerList.clear();
        for (int i = 0; i < activity.arrProgram.size(); i++) {
            LatLng latLng = new LatLng(activity.arrProgram.get(i).Latitude, activity.arrProgram.get(i).Longitude);
            mMarkerLongclicklistener = mMap.addMarker(new MarkerOptions().position(latLng).title(activity.arrProgram.get(i).NumberPhone).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)).snippet(activity.arrProgram.get(i).Address));
            markerList.add(mMarkerLongclicklistener);
        }

        mMap.setOnMarkerDragListener(new LinkMarkerLongClickListener(markerList) {
            @Override
            public void onLongClickListener(Marker marker) {
                DialogLongClickMarker(marker);

            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.834631, 105.667668), 14));
    }


    private void MoveCameraSearch(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        // x y
    }

    private void DialogLongClick() {
        final Dialog dialog = new Dialog(LocationActivity.this);
        dialog.setContentView(R.layout.dialog_location_longclick);
        final Button btAddDialogLogclick, btCancelDialogLogclick;
        btCancelDialogLogclick = dialog.findViewById(R.id.btCancelDialogLogclick);
        btAddDialogLogclick = dialog.findViewById(R.id.btAddDialogLogclick);
        btCancelDialogLogclick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btCancelDialogLogclick.startAnimation(aniUp);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btCancelDialogLogclick.startAnimation(aniDow);
                }
                dialog.cancel();
                return true;
            }
        });
        btAddDialogLogclick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btAddDialogLogclick.startAnimation(aniUp);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btAddDialogLogclick.startAnimation(aniDow);
                }
                dialog.cancel();
                DialogAddProgram();
                return true;
            }
        });
        dialog.show();
    }

}