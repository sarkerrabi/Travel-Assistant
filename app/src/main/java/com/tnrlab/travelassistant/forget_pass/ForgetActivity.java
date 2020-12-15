package com.tnrlab.travelassistant.forget_pass;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.loader.Loader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tnrlab.travelassistant.common.Common.isValidEmail;

public class ForgetActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @BindView(R.id.etEmail)
    TextInputEditText etEmail;
    Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        ButterKnife.bind(this);
        loader = new Loader(ForgetActivity.this);
    }

    @OnClick(R.id.btForgetPass)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError("Required");
            return;
        }

        if (!isValidEmail(etEmail.getText())) {
            etEmail.setError("Please insert valid email");
            return;
        }
        loader.showDialog();

        auth.sendPasswordResetEmail(etEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loader.hideDialog();
                            Toast.makeText(ForgetActivity.this, "Please check your email to reset your password", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loader.hideDialog();
                        Toast.makeText(ForgetActivity.this, "Email address not found. Please provide a valid email address.", Toast.LENGTH_SHORT).show();
                    }
                })

        ;


    }
}

