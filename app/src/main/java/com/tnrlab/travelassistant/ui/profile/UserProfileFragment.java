package com.tnrlab.travelassistant.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.tnrlab.travelassistant.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProfileFragment extends Fragment {

    @BindView(R.id.etName)
    TextView etName;
    @BindView(R.id.etEmailAddress)
    TextView etEmailAddress;
    @BindView(R.id.etAge)
    TextView etAge;
    @BindView(R.id.etHeight)
    TextView etHeight;
    @BindView(R.id.etWidth)
    TextView etWidth;
    private UserProfileViewModel mViewModel;

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_profile_fragment, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        // TODO: Use the ViewModel

        mViewModel.getUserProfileData(getContext()).observe(this, user -> {
            etName.setText(user.getFullName());
            etAge.setText(user.getAge());
            etEmailAddress.setText(user.getEmail());
            etHeight.setText(user.getHeight());
            etWidth.setText(user.getWeight());

        });


    }

}