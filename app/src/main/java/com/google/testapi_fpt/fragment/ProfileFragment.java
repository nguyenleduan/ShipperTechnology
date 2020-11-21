package com.google.testapi_fpt.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.snapshot.IndexedNode;
import com.google.testapi_fpt.DataClass;
import com.google.testapi_fpt.ImageCover;
import com.google.testapi_fpt.R;
import com.google.testapi_fpt.login.LoginActivity;
import com.google.testapi_fpt.teeee;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    ImageView imgLogout;
    LoginActivity acLogIn = new LoginActivity();
    DataClass dataClass = new DataClass();
    TextView tvSLLocation, tvName;
    CircleImageView circleImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        FindView(root);
        SetContent();

        return root;
    }

    private void SetContent() {

        tvSLLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), teeee.class);
                startActivity(intent);
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acLogIn.Logout();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        tvName.setText(dataClass.mProfile.UserName);
        tvSLLocation.setText("Số địa điểm: " + dataClass.arrProgram.size());
        Picasso.get().load(dataClass.mProfile.LinkAvatar).into(circleImageView);
    }

    private void FindView(View root) {
        imgLogout = root.findViewById(R.id.imgLogout);
        tvName = root.findViewById(R.id.tvName);
        circleImageView = root.findViewById(R.id.circleImageView);
        tvSLLocation = root.findViewById(R.id.tvSLLocation);
    }
}
