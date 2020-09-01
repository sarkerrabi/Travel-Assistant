package com.tnrlab.travelassistant.institution.institute_signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.institution.institute.InstituteMainActivity;
import com.tnrlab.travelassistant.institution.login.LoginActivity;
import com.tnrlab.travelassistant.loader.Loader;
import com.tnrlab.travelassistant.models.institute.Institution;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tnrlab.travelassistant.common.Common.isValidEmail;

public class SignUpInstituteActivity extends AppCompatActivity implements SignUpInstituteView {
    private static final int REQUEST_CODE = 5678;
    @BindView(R.id.etAddress)
    TextView selected_place_info;
    @BindView(R.id.etInstitutionName)
    EditText etInstitutionName;
    @BindView(R.id.etAddress1)
    EditText etAddress1;
    @BindView(R.id.etAddress2)
    EditText etAddress2;
    @BindView(R.id.etInsEmail)
    EditText etInsEmail;
    @BindView(R.id.ConfirmPassword)
    EditText etConfirmPassword;
    CarmenFeature carmenFeature;

    Loader loader;
    SignUpInstitutePresenter signUpForInstitutionPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_key));
        setContentView(R.layout.activity_sign_up_institute);
        ButterKnife.bind(this);

        signUpForInstitutionPresenter = new SignUpInstitutePresenter(this, SignUpInstituteActivity.this);

        loader = new Loader(SignUpInstituteActivity.this);

    }

    private void goToPickerActivity() {
        startActivityForResult(
                new PlacePicker.IntentBuilder()
                        .accessToken(getString(R.string.mapbox_key))
                        .placeOptions(PlacePickerOptions.builder()
                                .statingCameraPosition(new CameraPosition.Builder()
                                        .target(new LatLng(23.815291, 90.425360)).zoom(16).build())
                                .build())
                        .build(this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {


        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the information from the selected location's CarmenFeature
            carmenFeature = PlacePicker.getPlace(data);

            // Set the TextView text to the entire CarmenFeature. The CarmenFeature
            // also be parsed through to grab and display certain information such as
            // its placeName, text, or coordinates.
            if (carmenFeature != null) {
                selected_place_info.setText(carmenFeature.placeName() + ", " + carmenFeature.address());
            }
        }
    }

    @OnClick({R.id.ivPick, R.id.tvPick})
    public void onViewPickClicked() {
        goToPickerActivity();
    }

    @OnClick(R.id.btnSignUp)
    public void onViewSignUpClicked() {
        List<Double> latLan = new ArrayList<>();
        if (TextUtils.isEmpty(selected_place_info.getText())) {
            Toast.makeText(this, "Please select your institution on map.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(etInstitutionName.getText())) {
            etInstitutionName.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(etAddress1.getText())) {
            etAddress1.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(etInsEmail.getText())) {
            etInsEmail.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(etConfirmPassword.getText())) {
            etConfirmPassword.setError("Required");
            return;
        }

        String instituteName = etInstitutionName.getText().toString().trim().toLowerCase(),
                instituteEmail = etInsEmail.getText().toString().trim().toLowerCase(),
                password = etConfirmPassword.getText().toString(),
                address1 = etAddress1.getText().toString().trim().toLowerCase(),
                address2 = etAddress2.getText().toString().trim().toLowerCase();

        if (!isValidEmail(instituteEmail)) {
            etInsEmail.setError("Please insert valid email");
            return;
        }
        if (password.length() < 6) {
            etConfirmPassword.setError("At least 6 digit required");
            return;
        }


        if (carmenFeature != null) {
            latLan = carmenFeature.center().coordinates();
            //Log.d("RESULT", "onViewSignUpClicked: "+ carmenFeature.center().coordinates().toString()+" ");
        } else {
            Toast.makeText(this, "Please select your institution on map.", Toast.LENGTH_SHORT).show();
        }
        loader.showDialog();

        Institution institution = new Institution();
        institution.setActive(false);
        institution.setEmailAddress(instituteEmail);
        institution.setInstitutionName(instituteName);
        institution.setLatLan(latLan);
        institution.setMapPickerInsAddress(selected_place_info.getText().toString());
        if (!TextUtils.isEmpty(etAddress2.getText())) {
            institution.setUserGivenInsAddress(address1 + " " + address2);
        } else {
            institution.setUserGivenInsAddress(address1);
        }
        institution.setUserPassword(password);

        signUpForInstitutionPresenter.signUpForInstitutionNow(institution);


    }

    @Override
    public void onSignUpSuccessful(FirebaseUser firebaseUser, Institution institution) {
        institution.setFirebaseUid(firebaseUser.getUid());
        signUpForInstitutionPresenter.saveForInstitutionIntoDB(institution);

    }

    @Override
    public void onSignUpFailed(String s) {
        loader.hideDialog();
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveUserDBSuccessful() {
        loader.hideDialog();
        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SignUpInstituteActivity.this, InstituteMainActivity.class));
        finish();


    }

    @Override
    public void onSaveUserDBFailed() {
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();

    }

    @OnClick({R.id.tvGoUserSignUp, R.id.tvGoBackLogin})
    public void onGoViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvGoUserSignUp:
/*                startActivity(new Intent(SignUpInstituteActivity.this, SignUpActivity.class));
                finish();*/
                break;
            case R.id.tvGoBackLogin:
                startActivity(new Intent(SignUpInstituteActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
}