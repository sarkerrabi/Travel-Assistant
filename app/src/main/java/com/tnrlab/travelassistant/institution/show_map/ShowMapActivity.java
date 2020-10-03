package com.tnrlab.travelassistant.institution.show_map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.institution.login.LoginActivity;
import com.tnrlab.travelassistant.models.create_map.MapDataModel;
import com.tnrlab.travelassistant.models.institute.Institution;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.Property.TEXT_ANCHOR_BOTTOM;
import static com.mapbox.mapboxsdk.style.layers.Property.TEXT_ANCHOR_LEFT;
import static com.mapbox.mapboxsdk.style.layers.Property.TEXT_ANCHOR_RIGHT;
import static com.mapbox.mapboxsdk.style.layers.Property.TEXT_ANCHOR_TOP;
import static com.mapbox.mapboxsdk.style.layers.Property.TEXT_JUSTIFY_AUTO;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textJustify;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textRadialOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textVariableAnchor;

public class ShowMapActivity extends AppCompatActivity implements ShowMapView, OnMapReadyCallback {

    ShowMapPresenter showMapPresenter;
    @BindView(R.id.mapView)
    MapView mapView;
    Institution institution;
    private MapboxMap mapboxMap;
    private List<Double> latiLan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_key));
        getSupportActionBar().setTitle("Show Area map");
        setContentView(R.layout.activity_show_map);
        ButterKnife.bind(this);

        showMapPresenter = new ShowMapPresenter(this, ShowMapActivity.this);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.

        mapView.onCreate(savedInstanceState);

//        23.815291, 90.425360
        String institutionString = getIntent().getStringExtra("institute");

        if (institutionString != null) {
            Gson gson = new Gson();
            institution = gson.fromJson(institutionString, Institution.class);

        }

        if (institution != null) {

            latiLan = institution.getLatLan();

        } else {


            latiLan.add(90.425360);
            latiLan.add(23.815291);
        }

        mapView.getMapAsync(this);


    }

    @OnClick(R.id.save_map_button)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onFailedToGetCurrentUser() {
        startActivity(new Intent(ShowMapActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onMapDataListReady(List<MapDataModel> mapDataModels) {
//        Log.d("TAG_SHOW_MAP", "onStyleLoaded: " + mapDataModels.size());
        if (mapboxMap != null) {
            mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {


                    for (int pMap = 0; pMap < mapDataModels.size(); pMap++) {
                        FeatureCollection featureCollection = FeatureCollection.fromJson(mapDataModels.get(pMap).getMapData());
                        style.addSource(new GeoJsonSource("map-source" + pMap, featureCollection));


                        FillLayer fillLayer = new FillLayer("map-layer" + pMap,
                                "map-source" + pMap);
                        fillLayer.setProperties(
                                fillOpacity(.6f),
                                fillColor(Color.parseColor("#00ab66")),
                                textField(get("description")),
                                textSize(17f),
                                textColor(Color.RED),
                                textVariableAnchor(
                                        new String[]{TEXT_ANCHOR_TOP, TEXT_ANCHOR_BOTTOM, TEXT_ANCHOR_LEFT, TEXT_ANCHOR_RIGHT}),
                                textJustify(TEXT_JUSTIFY_AUTO),
                                textRadialOffset(0.5f)
                        );

                        style.addLayerBelow(fillLayer, "water");

                        style.addLayerAbove(new SymbolLayer("map-layer" + pMap + "txt", "map-source" + pMap + "txt")
                                .withProperties(
                                        textField(get("description")),
                                        textSize(100f),
                                        textColor(Color.RED),
                                        textVariableAnchor(
                                                new String[]{TEXT_ANCHOR_TOP, TEXT_ANCHOR_BOTTOM, TEXT_ANCHOR_LEFT, TEXT_ANCHOR_RIGHT}),
                                        textJustify(TEXT_JUSTIFY_AUTO),
                                        textRadialOffset(0.5f)), "road-number-shield");


                    }


                }
            });
        }

    }

    @Override
    public void onMapDataListFailed(String message) {

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

// Map is set up and the style has loaded. Now you can add data or make other map adjustments.

                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(new LatLng(latiLan.get(1), latiLan.get(0))))
                        .zoom(17) // Sets the zoom
                        .bearing(90) // Rotate the camera
                        .tilt(30) // Set the camera tilt
                        .build(); // Creates a CameraPosition from the builder

                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 7000);
                showMapPresenter.getInstMapDataList();

            }
        });

    }


    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
