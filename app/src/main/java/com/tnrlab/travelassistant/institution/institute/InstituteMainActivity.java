package com.tnrlab.travelassistant.institution.institute;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.institution.create_map.CreateMapActivity;
import com.tnrlab.travelassistant.institution.institute_profile.InstituteProfileActivity;
import com.tnrlab.travelassistant.institution.login.LoginActivity;
import com.tnrlab.travelassistant.institution.show_map.ShowMapActivity;
import com.tnrlab.travelassistant.models.institute.Institution;
import com.tnrlab.travelassistant.shared_db.SharedDB;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstituteMainActivity extends AppCompatActivity {


    Institution institution = new Institution();
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.iv_add_map)
    ImageView ivAddMap;
    @BindView(R.id.iv_show_map)
    ImageView ivShowMap;
    @BindView(R.id.iv_logout)
    ImageView ivLogout;
    @BindView(R.id.tv_profile)
    TextView tvProfile;
    @BindView(R.id.tv_add_map)
    TextView tvAddMap;
    @BindView(R.id.tv_show_map)
    TextView tvShowMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_main);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Welcome to Institute");

        String institutionString = getIntent().getStringExtra("institute");


        if (institutionString != null) {
            Gson gson = new Gson();
            institution = gson.fromJson(institutionString, Institution.class);

        }

        if (!institution.isActive()) {
            Toast.makeText(this, "Please Wait! Email address is waiting for verification ", Toast.LENGTH_SHORT).show();
            ivAddMap.setVisibility(View.GONE);
            tvAddMap.setVisibility(View.GONE);

            ivShowMap.setVisibility(View.GONE);
            tvShowMap.setVisibility(View.GONE);

        } else {
            ivAddMap.setVisibility(View.VISIBLE);
            tvAddMap.setVisibility(View.VISIBLE);

            ivShowMap.setVisibility(View.VISIBLE);
            tvShowMap.setVisibility(View.VISIBLE);

        }

    }

    @OnClick({R.id.iv_add_map, R.id.iv_show_map, R.id.iv_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_map:
                startActivity(new Intent(InstituteMainActivity.this, CreateMapActivity.class));
                break;
            case R.id.iv_show_map:
                Intent intent = new Intent(InstituteMainActivity.this, ShowMapActivity.class);
                intent.putExtra("institute", getIntent().getStringExtra("institute"));
                startActivity(intent);
                break;
            case R.id.iv_profile:
                Intent intent1 = new Intent(InstituteMainActivity.this, InstituteProfileActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent1);
                break;
        }
    }

    @OnClick(R.id.iv_logout)
    public void onViewLogoutClicked() {
        FirebaseAuth.getInstance().signOut();
        new SharedDB(getApplicationContext()).clearUserType();
        startActivity(new Intent(InstituteMainActivity.this, LoginActivity.class));
        finish();
    }
}