package com.tnrlab.travelassistant.ui.show_map_user;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

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
import com.tnrlab.travelassistant.models.create_map.MapDataModel;
import com.tnrlab.travelassistant.models.institute.Institution;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

public class ShowMapUserFragment extends Fragment implements OnMapReadyCallback, ShowMapUserView {

    @BindView(R.id.mapView)
    MapView mapView;
    private ShowMapUserViewModel mViewModel;
    private Institution institution;
    private MapboxMap mapboxMap;
    private List<Double> latiLan = new ArrayList<>();

    public static ShowMapUserFragment newInstance() {
        return new ShowMapUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Mapbox.getInstance(getContext(), getString(R.string.mapbox_key));
        View root = inflater.inflate(R.layout.show_map_user_fragment, container, false);
        ButterKnife.bind(this, root);

        mapView.onCreate(savedInstanceState);


        String mapData = getArguments().getString("institution_data");
        Gson gson = new Gson();
        if (mapData != null) {
            institution = gson.fromJson(mapData, Institution.class);
            latiLan = institution.getLatLan();
        }

        mapView.getMapAsync(this);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ShowMapUserViewModel.class);
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
                                textSize(100f),
                                textColor(Color.RED),
                                textVariableAnchor(
                                        new String[]{TEXT_ANCHOR_TOP, TEXT_ANCHOR_BOTTOM, TEXT_ANCHOR_LEFT, TEXT_ANCHOR_RIGHT}),
                                textJustify(TEXT_JUSTIFY_AUTO),
                                textRadialOffset(0.5f)
                        );

                        style.addLayerBelow(fillLayer, "road-number-shield");

                        style.addLayerAbove(new SymbolLayer("map-layer" + pMap + "txt", "map-source" + pMap)
                                .withProperties(
                                        textField(get("description")),
                                        textSize(15f),
                                        textColor(Color.RED),
                                        textVariableAnchor(
                                                new String[]{TEXT_ANCHOR_TOP, TEXT_ANCHOR_BOTTOM, TEXT_ANCHOR_LEFT, TEXT_ANCHOR_RIGHT}),
                                        textJustify(TEXT_JUSTIFY_AUTO),
                                        textRadialOffset(0.9f)), "road-number-shield");


                    }


                }
            });
        }

    }


    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        // Map is set up and the style has loaded. Now you can add data or make other map adjustments.
        this.mapboxMap = mapboxMap;
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(new LatLng(latiLan.get(1), latiLan.get(0))))
                .zoom(16) // Sets the zoom
                .bearing(90) // Rotate the camera
                .tilt(30) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder

        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 7000);

        mViewModel.getMapDetailsList(institution, this);


    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}