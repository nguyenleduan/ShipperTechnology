package com.google.testapi_fpt.route;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.testapi_fpt.DataClass;
import com.google.testapi_fpt.MainActivity;
import com.google.testapi_fpt.R;
import com.google.testapi_fpt.adapter.SearchListViewAdapter;
import com.google.testapi_fpt.location.LocationActivity;
import com.google.testapi_fpt.model.ProgramModel;
import com.google.testapi_fpt.route.adapter.ListViewitem;
import com.google.testapi_fpt.route.model.RouteModel;

import java.util.ArrayList;
import java.util.List;

public class RouteActivity extends AppCompatActivity {
    ListView lv, lvSearch;
    EditText editTextPhone;
    DataClass mdata = new DataClass();
    DatabaseReference mData;
    DataClass dataClass = new DataClass();
    static ArrayList<ProgramModel> arrSearchProgram = new ArrayList<>();
    public static ArrayList<RouteModel> arrRoute = new ArrayList<>();
    Button btAddLocation, btStart;
    ConstraintLayout mConstraintLayout;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        findview();
        Event();
//        Location.distanceBetween(dMyLatitude,dMyLongitude,dLatEnd,dLoEnd,resul);
//        //
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String mPhone = data.getStringExtra("keyPhone");
                    Double mLatitude, mLongitude;
                    mLatitude = data.getDoubleExtra("KeyLatitude", 0);
                    mLongitude = data.getDoubleExtra("KeyLongitude", 0);
                    arrRoute.add(new RouteModel(mPhone, mLatitude, mLongitude));
                    SetListItem(arrRoute);
                    editTextPhone.setText(null);
                }
            }
        }
    }

    private void Event() {
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RouteActivity.this, DetailRouteActivity.class);
                startActivity(intent);
            }
        });
        editTextPhone.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    CloseKeyboard();
                    lvSearch.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });
        mConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvSearch.setVisibility(View.GONE);
            }
        });
        btAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RouteActivity.this, AddLocationRouteActivity.class);
                intent.putExtra("keyPHONE", editTextPhone.getText().toString());
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });
        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrSearchProgram.clear();
                for (int i = 0; i < mdata.arrProgram.size(); i++) {
                    if (String.valueOf(mdata.arrProgram.get(i).NumberPhone).toLowerCase().contains(s.toString().toLowerCase())) {
                        arrSearchProgram.add(mdata.arrProgram.get(i));
                    }
                }
                ListViewSearch(arrSearchProgram);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }

    private void ListViewSearch(final ArrayList<ProgramModel> arrlist) {
        lvSearch.setVisibility(View.VISIBLE);
        SearchListViewAdapter searchView = new SearchListViewAdapter(RouteActivity.this, R.layout.listview_search, arrlist);
        searchView.notifyDataSetChanged();
        lvSearch.setAdapter(searchView);
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // add to list Route
                arrRoute.add(new RouteModel(arrSearchProgram.get(position).NumberPhone, arrSearchProgram.get(position).Latitude, arrSearchProgram.get(position).Longitude));
                SetListItem(arrRoute);
                // close key board
                CloseKeyboard();
                editTextPhone.setText(null);
                // GONE Listview
                lvSearch.setVisibility(View.GONE);
            }

        });
    }

    public void SetListItem(ArrayList<RouteModel> arrRoute) {
        ListViewitem adapter = new ListViewitem(RouteActivity.this, R.layout.item_route, arrRoute);
        lv.setAdapter(adapter);
        if (arrRoute.size() > 1) {
            btStart.setVisibility(View.VISIBLE);
        } else {
            btStart.setVisibility(View.GONE);
        }
        ListAdapter listadp = lv.getAdapter();
        if (listadp != null) {
            int totalHeight = 0;
            for (int i = 0; i < listadp.getCount(); i++) {
                View listItem = listadp.getView(i, null, lv);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = lv.getLayoutParams();
            params.height = totalHeight + (lv.getDividerHeight() * (listadp.getCount() - 1));
            lv.setLayoutParams(params);
            lv.requestLayout();
        }
    }

    private void CloseKeyboard() {
        View view = RouteActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void findview() {
        btStart = findViewById(R.id.btStart);
        mConstraintLayout = findViewById(R.id.mConstraintLayout);
        btAddLocation = findViewById(R.id.btAddLocation);
        editTextPhone = findViewById(R.id.editTextPhone);
        lvSearch = findViewById(R.id.lvSearch);
        lv = findViewById(R.id.lv);
    }

    // ẩn topp dektop
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }
}