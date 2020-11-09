package com.google.testapi_fpt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.google.testapi_fpt.login.LoginActivity;
import com.google.testapi_fpt.model.ProfileModel;
import com.google.testapi_fpt.model.ProgramModel;
import com.google.testapi_fpt.test.UserModelss;
import com.google.testapi_fpt.thread.DataMaps;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 2000;
    Animation top;
    ImageView img;
    DatabaseReference mData;
//    public static ProfileModel mProfile =null;
//    public static ArrayList<ProgramModel> arr = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        top = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        img = findViewById(R.id.imgLogo);
        img.setAnimation(top);
//        arr.clear();
        DataMaps dataMaps = new DataMaps();
        dataMaps.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        },SPLASH_SCREEN);
        String url = "http://my-json-feed";

        JsonObjectRequest re = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject jsonObject = response.getJSONObject("main");
                    JSONArray jsonArray = response.getJSONArray("DDASD");
                    JSONObject object = jsonArray.getJSONObject(0);
                    String a = object.getString("asd");
                    
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


//        new AsyncTask<Void, Void, ArrayList<UserModelss>>() {
//            @Override
//            protected ArrayList<UserModelss> doInBackground(Void... voids) {
//                try{
//                    OkHttpClient client = new OkHttpClient();
//                    Moshi moshi = new Moshi.Builder().build();
//                    Type usersType = Types.newParameterizedType(List.class, UserModelss.class);
//                    final JsonAdapter<ArrayList<UserModelss>> jsonAdapter = moshi.adapter(usersType);
//
//                    // Tạo request lên server.
//                    Request request = new Request.Builder()
//                            .url("https://api.github.com/users")
//                            .build();
//                    client.newCall(request).enqueue(new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            Log.e("Error", "Network Error");
//                        }
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            // Lấy thông tin JSON trả về. Bạn có thể log lại biến json này để xem nó như thế nào.
//                            String json = response.body().string();
//                            final ArrayList<UserModelss> users = jsonAdapter.fromJson(json);
//                            // Cho hiển thị lên RecyclerView.
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                   Log.e("LOGE",users.get(0).getId()+" 1111");
//                                }
//                            });
//                        }
//                    });
//                }catch (Exception e){
//                    Log.e("Exceptiodn",e.toString());
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(ArrayList<UserModelss> userModelsses) {
//                super.onPostExecute(userModelsses);
//            }
//        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
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
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}