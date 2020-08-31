package com.tnrlab.travelassistant.institute_profile;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.institute.InstituteMainActivity;
import com.tnrlab.travelassistant.loader.Loader;
import com.tnrlab.travelassistant.login.LoginActivity;
import com.tnrlab.travelassistant.models.institute.Institution;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstituteProfileActivity extends AppCompatActivity implements InstituteProfileView {

    InstituteProfilePresenter instituteProfilePresenter;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.etInstitutionName)
    TextView etInstitutionName;
    @BindView(R.id.etAddress)
    TextView etAddress;
    @BindView(R.id.etAddress1)
    TextView etAddress1;

    @BindView(R.id.etInsEmail)
    TextView etInsEmail;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_profile);
        ButterKnife.bind(this);
        loader = new Loader(InstituteProfileActivity.this);
        instituteProfilePresenter = new InstituteProfilePresenter(this, InstituteProfileActivity.this);
        instituteProfilePresenter.getMyInstituteProfile();
        loader.showDialog();
    }

    @Override
    public void onInstituteProfileReady(Institution institution) {


        if (institution != null) {
            etInstitutionName.setText(institution.getInstitutionName());
            etAddress.setText(institution.getMapPickerInsAddress());
            etAddress1.setText(institution.getUserGivenInsAddress());
            etInsEmail.setText(institution.getEmailAddress());

            if (institution.isActive()){
                tvStatus.setText("Institute address is verified");
            }else {

                tvStatus.setText("Please Wait! Email address is waiting for verification");
            }
        }
        if (loader !=null){
            loader.hideDialog();
        }
    }

    @Override
    public void onInstituteProfileFailed(String message) {
        if (loader !=null){
            loader.hideDialog();
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @OnClick(R.id.bt_back)
    public void onViewClicked() {
        finish();
    }
}