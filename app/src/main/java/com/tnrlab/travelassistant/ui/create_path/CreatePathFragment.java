package com.tnrlab.travelassistant.ui.create_path;

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

public class CreatePathFragment extends Fragment {

    @BindView(R.id.tvData)
    TextView tvData;
    private CreatePathViewModel mViewModel;


    public static CreatePathFragment newInstance() {
        return new CreatePathFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.create_path_fragment, container, false);
        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CreatePathViewModel.class);
        // TODO: Use the ViewModel

        mViewModel.getSensorEventLiveData(getContext()).observe(this, data -> {
            tvData.setText("Sensor Data " + data);
        });

    }

}