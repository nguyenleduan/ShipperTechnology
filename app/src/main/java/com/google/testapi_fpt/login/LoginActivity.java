package com.google.testapi_fpt.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.testapi_fpt.DataClass;
import com.google.testapi_fpt.MainActivity;
import com.google.testapi_fpt.R;
import com.google.testapi_fpt.WelcomeActivity;
import com.google.testapi_fpt.model.ProfileModel;
import com.google.testapi_fpt.model.ProgramModel;
import com.google.testapi_fpt.thread.DataMaps;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    LoginButton loginButton;
    String TAG = "CheckLogD";
    CallbackManager mCallbackManager;
    Button btDangky, btDangNhap, btSingInSingin, btCancelSignIn;
    DatabaseReference mDataFireBase;
    Animation aniUp, aniDow;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    EditText edtEmailSigIn, edtpasswordSigIn, edtpasswordSigIn2, edtNameSignIn, edtNumberPhoneSignIn, edtPassLogin, edtEmailLogin;
    ImageView imgAvatarSignIn;
    LinearLayout LinearLogin, LinearSignIn;
    TextView tvNotePassSigin, tvNoteEmailSigin;
    DataClass dataClass = new DataClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        mDataFireBase = FirebaseDatabase.getInstance().getReference();
        inputFind();
        EvenClick();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.e("LOGD", user.getDisplayName());
                } else {
                    Log.e("LOGD", "null");
                }
            }
        };
    }

    private void SignUp(String email, String password, final String userName,
                        final String NumberPhone, final String linkAvatar) {
        final String mIDUSER = null;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();
                            updateUISignUp(user);
                            Log.e("LOGE", user.getUid());
                            ProfileModel profilemodel = new ProfileModel(0, user.getUid(), "null", 100.323232, linkAvatar, 100.5656
                                    , NumberPhone, userName);
                            mDataFireBase.child("Profile").child(user.getUid()).setValue(profilemodel);
                            tvNotePassSigin.setVisibility(View.GONE);
                            tvNoteEmailSigin.setVisibility(View.GONE);
                            LinearSignIn.setVisibility(View.GONE);
                            LinearLogin.setVisibility(View.VISIBLE);
                            edtEmailLogin.setText(edtEmailSigIn.getText().toString());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Kiểm tra lại Email & mật khẩu",
                                    Toast.LENGTH_SHORT).show();
                            tvNotePassSigin.setVisibility(View.VISIBLE);
                            tvNoteEmailSigin.setVisibility(View.VISIBLE);
                            updateUISignUp(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUISignUp(FirebaseUser user) {
    }

    private void EvenClick() {
        btCancelSignIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btCancelSignIn.startAnimation(aniUp);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btCancelSignIn.startAnimation(aniDow);
                    LinearSignIn.setVisibility(View.GONE);
                    LinearLogin.setVisibility(View.VISIBLE);
                    edtEmailSigIn.setText(null);
                    edtpasswordSigIn.setText(null);
                    edtpasswordSigIn2.setText(null);
                    edtNameSignIn.setText(null);
                }
                return true;
            }
        });
        btSingInSingin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btSingInSingin.startAnimation(aniUp);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btSingInSingin.startAnimation(aniDow);
                    if (edtEmailSigIn.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "Chưa nhập Email", Toast.LENGTH_SHORT).show();
                    } else if (edtpasswordSigIn.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "Chưa Nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    } else if (edtpasswordSigIn2.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "Chưa nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
                    } else if (!edtpasswordSigIn.getText().toString().equals(edtpasswordSigIn2.getText().toString())) {
                        Toast.makeText(LoginActivity.this, "Mật khẩu chưa khớp", Toast.LENGTH_SHORT).show();
                    } else if (edtNameSignIn.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "Chưa nhập tên", Toast.LENGTH_SHORT).show();
                    } else if (edtNumberPhoneSignIn.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "Chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
                    } else {
                        SignUp(edtEmailSigIn.getText().toString(), edtpasswordSigIn.getText().toString(), edtNameSignIn.getText().toString(),
                                edtNumberPhoneSignIn.getText().toString(), "null");
                    }
                }
                return true;
            }
        });
        btDangNhap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btDangNhap.startAnimation(aniUp);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btDangNhap.startAnimation(aniDow);
                    Toast.makeText(LoginActivity.this, "Đăng nhập Email chưa hỗ trợ", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
        btDangky.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btDangky.startAnimation(aniUp);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btDangky.startAnimation(aniDow);
                    LinearLogin.setVisibility(View.GONE);
                    LinearSignIn.setVisibility(View.VISIBLE);
                    edtEmailSigIn.setText(null);
                    edtpasswordSigIn.setText(null);
                    edtpasswordSigIn2.setText(null);
                    edtNameSignIn.setText(null);
                }
                return true;
            }
        });

        //
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                FirebaseUser currentUser = mAuth.getCurrentUser();
                updateUIFacebook(currentUser);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();
                Log.e("LOGE", error.toString());
                // ...
            }
        });

    }

    private void inputFind() {
        tvNoteEmailSigin = findViewById(R.id.tvNoteEmailSigin);
        tvNotePassSigin = findViewById(R.id.tvNotePassSigin);
        LinearLogin = findViewById(R.id.LinearLogin);
        edtPassLogin = findViewById(R.id.edtPassLogin);
        LinearSignIn = findViewById(R.id.LinearSignIn);
        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        imgAvatarSignIn = findViewById(R.id.imgAvatarSignIn);
        btCancelSignIn = findViewById(R.id.btCancelSignIn);
        btSingInSingin = findViewById(R.id.btSingInSingin);
        edtNumberPhoneSignIn = findViewById(R.id.edtNumberPhoneSignIn);
        edtNameSignIn = findViewById(R.id.edtNameSignIn);
        edtpasswordSigIn2 = findViewById(R.id.edtpasswordSigIn2);
        edtpasswordSigIn = findViewById(R.id.edtpasswordSigIn);
        edtEmailSigIn = findViewById(R.id.edtEmailSigIn);
        loginButton = findViewById(R.id.login_button);
        btDangky = findViewById(R.id.btDangKy);
        btDangNhap = findViewById(R.id.btDangNhap);
        aniUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        aniDow = AnimationUtils.loadAnimation(this, R.anim.scale_doew);
    }

    // ...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("iii","asd");
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUIFacebook(currentUser); // facebook
    }

    public void Logout() {
        FirebaseAuth.getInstance().signOut();

    }

    private void updateUIFacebook(FirebaseUser user) {
        if (user != null) {

            ProfileModel profileModel  = new ProfileModel(0,user.getUid(),"null",120.45454
                    ,user.getPhotoUrl().toString(),
                    103.65454,null,user.getDisplayName());
            dataClass.mProfile = profileModel;
            mDataFireBase.child("Profile").child(user.getUid()).setValue(profileModel);

            Log.e("LOGEs",   dataClass.mProfile.IDUSER+"");
            DataMaps dataMaps = new DataMaps();
            dataMaps.start();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }


    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUIFacebook(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUIFacebook(null);
                        }

                        // ...
                    }
                });
    }


    //
}