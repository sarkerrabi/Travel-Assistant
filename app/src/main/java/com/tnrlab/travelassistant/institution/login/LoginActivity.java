package com.tnrlab.travelassistant.institution.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.tnrlab.travelassistant.MainActivity;
import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.forgetpass.ForgetActivity;
import com.tnrlab.travelassistant.institution.institute.InstituteMainActivity;
import com.tnrlab.travelassistant.institution.institute_signup.SignUpInstituteActivity;
import com.tnrlab.travelassistant.loader.Loader;
import com.tnrlab.travelassistant.models.institute.Institution;
import com.tnrlab.travelassistant.models.user.User;
import com.tnrlab.travelassistant.shared_db.SharedDB;
import com.tnrlab.travelassistant.user_signup.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tnrlab.travelassistant.common.Common.isValidEmail;

public class LoginActivity extends AppCompatActivity implements LoginView {
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPass;
    Loader loader;
    LoginPresenter loginPresenter;
    @BindView(R.id.rbUser)
    RadioButton rbUser;
    @BindView(R.id.rbInstitute)
    RadioButton rbInstitute;

    SharedDB sharedDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loader = new Loader(LoginActivity.this);
        sharedDB = new SharedDB(LoginActivity.this);
        loginPresenter = new LoginPresenter(LoginActivity.this, this);
        rbUser.setChecked(true);
    }


    @OnClick(R.id.btLoginIn)
    public void onLoginClicked() {

        String email = etEmail.getText().toString().trim().toLowerCase(),
                password = etPass.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPass.setError("Required");
            return;
        }
        if (!isValidEmail(email)) {
            etEmail.setError("Please insert valid email");
            return;
        }
        if (password.length() < 6) {
            etPass.setError("At least 6 digit required");
            return;
        }

        if (!rbUser.isChecked() && !rbInstitute.isChecked()) {
            rbUser.setError("must pick one");
            rbInstitute.setError("must pick one");
            return;
        }

        loader.showDialog();

        if (rbInstitute.isChecked()) {
            loginPresenter.loginNow(email, password, 1);
        } else if (rbUser.isChecked()) {
            loginPresenter.loginNow(email, password, 2);

        }


    }


    @Override
    public void signInInstituteSuccessful(Institution institution) {

        sharedDB.saveUserTypeID(1);
        Intent intent = new Intent(LoginActivity.this, InstituteMainActivity.class);
        intent.putExtra("isActive", institution.isActive());
        Gson gson = new Gson();
        String insGson = gson.toJson(institution);
        intent.putExtra("institute", insGson);
        sharedDB.saveInstituteUserInfo(insGson);
        if (loader != null) {
            loader.hideDialog();
        }

        startActivity(intent);
        finish();


    }

    @Override
    public void signInUserSuccessful(User user) {
        sharedDB.saveUserTypeID(2);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Gson gson = new Gson();
        String insGson = gson.toJson(user);
        intent.putExtra("user", insGson);
        sharedDB.saveUserInfo(insGson);
        if (loader != null) {
            loader.hideDialog();
        }

        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailed(String message) {
        if (loader != null) {
            loader.hideDialog();
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @OnClick({R.id.tvForgetPass, R.id.tvSignUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvForgetPass:
                startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
                break;
            case R.id.tvSignUp:
                if (rbUser.isChecked()) {
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                } else {
                    startActivity(new Intent(LoginActivity.this, SignUpInstituteActivity.class));
                }

                break;
        }
    }
}