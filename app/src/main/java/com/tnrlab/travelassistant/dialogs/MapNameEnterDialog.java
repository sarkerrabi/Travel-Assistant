/*
 * *
 *  * Created by Md. Rabiul Ali Sarker, Software Developer, IT, SQ Group
 *  * Created on on 9/30/20 11:30 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 9/30/20 11:30 AM
 *
 */

package com.tnrlab.travelassistant.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.institution.create_map.CreateMapView;

public class MapNameEnterDialog extends Dialog implements View.OnClickListener {

    public Button yes, no;
    Activity activity;
    CreateMapView createMapView;

    EditText etMapName;
    TextView txt_dia;


    public MapNameEnterDialog(Activity activity, CreateMapView createMapView) {
        super(activity);
        this.activity = activity;
        this.createMapView = createMapView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_map_name_entry_layout);
        txt_dia = findViewById(R.id.txt_dia);
        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);
        etMapName = findViewById(R.id.etMMapName);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                if (etMapName.getText().toString().isEmpty()) {
                    etMapName.setError("Please enter a map name");
                    return;

                } else {
                    createMapView.onCreateBlockNameSet(etMapName.getText().toString());
                }
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }

    }
}