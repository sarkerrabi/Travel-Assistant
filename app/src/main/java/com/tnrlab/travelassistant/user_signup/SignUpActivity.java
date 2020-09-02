package com.tnrlab.travelassistant.user_signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.institution.login.LoginActivity;
import com.tnrlab.travelassistant.loader.Loader;
import com.tnrlab.travelassistant.models.user.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tnrlab.travelassistant.common.Common.isValidEmail;

public class SignUpActivity extends AppCompatActivity implements SignUpView {
    Loader loader;

    @BindView(R.id.etFullName)
    EditText etFullName;
    @BindView(R.id.etHeight)
    EditText etHeight;
    @BindView(R.id.etWeight)
    EditText etWeight;
    @BindView(R.id.etAge)
    EditText etAge;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPass)
    EditText etPass;
    @BindView(R.id.btSignUp)
    Button btSignUp;

    SignUpPresenter signUpPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Sign Up as user");
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        signUpPresenter = new SignUpPresenter(this, SignUpActivity.this);
        loader = new Loader(SignUpActivity.this);


    }


    @Override
    public void onSignUpSuccessful(FirebaseUser firebaseUser, User user) {
        loader.hideDialog();
        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();


    }

    @Override
    public void onSignUpFailed(String message) {
        loader.hideDialog();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @OnClick(R.id.btSignUp)
    public void onViewClicked() {
        String fullName = etFullName.getText().toString().trim().toLowerCase(),
                email = etEmail.getText().toString().trim().toLowerCase(),
                password = etPass.getText().toString(),
                age = etAge.getText().toString().trim(),
                height = etHeight.getText().toString().trim(),
                weight = etWeight.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(height)) {
            etHeight.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(weight)) {
            etWeight.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(age)) {
            etAge.setError("Required");
            return;
        }

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


        // todo: done
        // validate age, height, weight
        if (Integer.parseInt(String.valueOf(etAge.getText())) < 3) {
            etAge.setError("Invalid Age!! Age must be greater than 3yrs");
            return;
        }
        /*        if( Integer.parseInt(String.valueOf(etHeight.getText())) > 220 *//*7 feet*//*
                || Integer.parseInt(String.valueOf(etHeight.getText())) < 60 *//*2 feet*//*){
            etHeight.setError("Invalid Height(cm)");
            return;
        }
        if( Integer.parseInt(String.valueOf(etWeight.getText())) < 20
                || Integer.parseInt(String.valueOf(etWeight.getText())) > 200 ){
            etWeight.setError("Invalid Weight(kg)");
            return;
        }*/


        User user = new User();
        user.setAge(age);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(password);
        user.setHeight(height);
        user.setWeight(weight);

        loader.showDialog();
        signUpPresenter.signUpNow(user);

    }
}