package com.tnrlab.travelassistant.institution.create_map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.tnrlab.travelassistant.R;
import com.tnrlab.travelassistant.dialogs.MapNameEnterDialog;
import com.tnrlab.travelassistant.loader.Loader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class CreateMapActivity extends AppCompatActivity implements OnMapReadyCallback, CreateMapView {
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final String CIRCLE_SOURCE_ID = "circle-source-id";
    private static final String FILL_SOURCE_ID = "fill-source-id";
    private static final String LINE_SOURCE_ID = "line-source-id";
    private static final String CIRCLE_LAYER_ID = "circle-layer-id";
    private static final String FILL_LAYER_ID = "fill-layer-polygon-id";
    private static final String LINE_LAYER_ID = "line-layer-id";
    @BindView(R.id.save_map_button)
    FloatingActionButton saveMapButton;

    private List<Point> fillLayerPointList = new ArrayList<>();
    private List<Point> lineLayerPointList = new ArrayList<>();
    private List<Feature> circleLayerFeatureList = new ArrayList<>();
    private List<List<Point>> listOfList;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private GeoJsonSource circleSource;
    private GeoJsonSource fillSource;
    private GeoJsonSource lineSource;
    private Point firstPointOfPolygon;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";

    private FeatureCollection featureCollectionForSave;

    private CreateMapPresenter createMapPresenter;
    private Loader loader;
    private MapNameEnterDialog mapNameEnterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Create Institute Map");

        Mapbox.getInstance(this, getString(R.string.mapbox_key));
        setContentView(R.layout.activity_create_map);
        ButterKnife.bind(this);
        loader = new Loader(this);
        createMapPresenter = new CreateMapPresenter(this, CreateMapActivity.this);
        saveMapButton.hide();
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                initSearchFab();
                mapboxMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                .zoom(5)
                                .build()), 4000);

// Add sources to the map
                circleSource = initCircleSource(style);
                fillSource = initFillSource(style);
                lineSource = initLineSource(style);

// Add layers to the map
                initCircleLayer(style);
                initLineLayer(style);
                initFillLayer(style);

                initFloatingActionButtonClickListeners();
                //Toast.makeText(CreateMapActivity.this, R.string.trace_instruction, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Set the button click listeners
     */
    private void initFloatingActionButtonClickListeners() {
        Button clearBoundariesFab = findViewById(R.id.clear_button);
        clearBoundariesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearEntireMap();
            }
        });

        FloatingActionButton dropPinFab = findViewById(R.id.drop_pin_button);
        dropPinFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!saveMapButton.isShown()) {
                    saveMapButton.show();
                }

// Use the map click location to create a Point object
                Point mapTargetPoint = Point.fromLngLat(mapboxMap.getCameraPosition().target.getLongitude(),
                        mapboxMap.getCameraPosition().target.getLatitude());

// Make note of the first map click location so that it can be used to create a closed polygon later on
                if (circleLayerFeatureList.size() == 0) {
                    firstPointOfPolygon = mapTargetPoint;
                }

// Add the click point to the circle layer and update the display of the circle layer data
                circleLayerFeatureList.add(Feature.fromGeometry(mapTargetPoint));
                if (circleSource != null) {
                    circleSource.setGeoJson(FeatureCollection.fromFeatures(circleLayerFeatureList));
                }

// Add the click point to the line layer and update the display of the line layer data
                if (circleLayerFeatureList.size() < 3) {
                    lineLayerPointList.add(mapTargetPoint);
                } else if (circleLayerFeatureList.size() == 3) {
                    lineLayerPointList.add(mapTargetPoint);
                    lineLayerPointList.add(firstPointOfPolygon);
                } else {
                    lineLayerPointList.remove(circleLayerFeatureList.size() - 1);
                    lineLayerPointList.add(mapTargetPoint);
                    lineLayerPointList.add(firstPointOfPolygon);
                }
                if (lineSource != null) {
                    lineSource.setGeoJson(FeatureCollection.fromFeatures(new Feature[]
                            {Feature.fromGeometry(LineString.fromLngLats(lineLayerPointList))}));
                }

// Add the click point to the fill layer and update the display of the fill layer data
                if (circleLayerFeatureList.size() < 3) {
                    fillLayerPointList.add(mapTargetPoint);
                } else if (circleLayerFeatureList.size() == 3) {
                    fillLayerPointList.add(mapTargetPoint);
                    fillLayerPointList.add(firstPointOfPolygon);
                } else {
                    fillLayerPointList.remove(fillLayerPointList.size() - 1);
                    fillLayerPointList.add(mapTargetPoint);
                    fillLayerPointList.add(firstPointOfPolygon);
                }
                Log.e("CREATE_MAP_TAG", "-----------------fillLayerPointList---------------------------");
                Log.e("CREATE_MAP_TAG", "fillLayerPointList: " + fillLayerPointList);
                listOfList = new ArrayList<>();
                listOfList.add(fillLayerPointList);
                List<Feature> finalFeatureList = new ArrayList<>();
                finalFeatureList.add(Feature.fromGeometry(Polygon.fromLngLats(listOfList)));
                Log.e("CREATE_MAP_TAG", "-----------------finalFeatureList---------------------------");

                Log.e("CREATE_MAP_TAG", "finalFeatureList: " + finalFeatureList);

                FeatureCollection newFeatureCollection = FeatureCollection.fromFeatures(finalFeatureList);
                Log.e("CREATE_MAP_TAG", "-----------------finalFeatureList---------------------------");


                Log.e("CREATE_MAP_TAG", "finalFeatureList: " + newFeatureCollection);


                if (fillSource != null) {
                    featureCollectionForSave = newFeatureCollection;
                    featureCollectionForSave.features().get(0).addStringProperty("description", "nsu");
                    fillSource.setGeoJson(newFeatureCollection);
                }
            }
        });
    }


    private void initSearchFab() {
        findViewById(R.id.fab_location_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_key))
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(CreateMapActivity.this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

// Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);


// Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
// Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

// Move map camera to the selected location
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                }
            }
        }
    }

    /**
     * Remove the drawn area from the map by resetting the FeatureCollections used by the layers' sources
     */
    private void clearEntireMap() {
        if (saveMapButton.isShown()) {
            saveMapButton.hide();
        }


        fillLayerPointList = new ArrayList<>();
        circleLayerFeatureList = new ArrayList<>();
        lineLayerPointList = new ArrayList<>();
        if (circleSource != null) {
            circleSource.setGeoJson(FeatureCollection.fromFeatures(new Feature[]{}));
        }
        if (lineSource != null) {
            lineSource.setGeoJson(FeatureCollection.fromFeatures(new Feature[]{}));
        }
        if (fillSource != null) {
            fillSource.setGeoJson(FeatureCollection.fromFeatures(new Feature[]{}));
        }
    }

    /**
     * Set up the CircleLayer source for showing map click points
     */
    private GeoJsonSource initCircleSource(@NonNull Style loadedMapStyle) {
        FeatureCollection circleFeatureCollection = FeatureCollection.fromFeatures(new Feature[]{});
        GeoJsonSource circleGeoJsonSource = new GeoJsonSource(CIRCLE_SOURCE_ID, circleFeatureCollection);
        loadedMapStyle.addSource(circleGeoJsonSource);
        return circleGeoJsonSource;
    }

    /**
     * Set up the CircleLayer for showing polygon click points
     */
    private void initCircleLayer(@NonNull Style loadedMapStyle) {
        CircleLayer circleLayer = new CircleLayer(CIRCLE_LAYER_ID,
                CIRCLE_SOURCE_ID);
        circleLayer.setProperties(
                circleRadius(7f),
                circleColor(Color.parseColor("#d004d3"))
        );
        loadedMapStyle.addLayer(circleLayer);
    }

    /**
     * Set up the FillLayer source for showing map click points
     */
    private GeoJsonSource initFillSource(@NonNull Style loadedMapStyle) {
        FeatureCollection fillFeatureCollection = FeatureCollection.fromFeatures(new Feature[]{});
        GeoJsonSource fillGeoJsonSource = new GeoJsonSource(FILL_SOURCE_ID, fillFeatureCollection);
        loadedMapStyle.addSource(fillGeoJsonSource);
        return fillGeoJsonSource;
    }

    /**
     * Set up the FillLayer for showing the set boundaries' polygons
     */
    private void initFillLayer(@NonNull Style loadedMapStyle) {
        FillLayer fillLayer = new FillLayer(FILL_LAYER_ID,
                FILL_SOURCE_ID);
        fillLayer.setProperties(
                fillOpacity(.6f),
                fillColor(Color.parseColor("#00e9ff"))
        );
        loadedMapStyle.addLayerBelow(fillLayer, LINE_LAYER_ID);
    }

    /**
     * Set up the LineLayer source for showing map click points
     */
    private GeoJsonSource initLineSource(@NonNull Style loadedMapStyle) {
        FeatureCollection lineFeatureCollection = FeatureCollection.fromFeatures(new Feature[]{});
        GeoJsonSource lineGeoJsonSource = new GeoJsonSource(LINE_SOURCE_ID, lineFeatureCollection);
        loadedMapStyle.addSource(lineGeoJsonSource);
        return lineGeoJsonSource;
    }

    /**
     * Set up the LineLayer for showing the set boundaries' polygons
     */
    private void initLineLayer(@NonNull Style loadedMapStyle) {
        LineLayer lineLayer = new LineLayer(LINE_LAYER_ID,
                LINE_SOURCE_ID);
        lineLayer.setProperties(
                lineColor(Color.WHITE),
                lineWidth(5f)
        );
        loadedMapStyle.addLayerBelow(lineLayer, CIRCLE_LAYER_ID);
    }


    @OnClick(R.id.save_map_button)
    public void onViewClicked() {

        if (featureCollectionForSave != null) {
            mapNameEnterDialog = new MapNameEnterDialog(this, this);
            mapNameEnterDialog.setCancelable(false);
            mapNameEnterDialog.setCanceledOnTouchOutside(false);
            mapNameEnterDialog.show();
        }
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

    @Override
    public void onFailedToGetCurrentUser() {

    }

    @Override
    public void onCreateMapSaveSuccessful(String successMessage) {
        Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCreateMapSaveFailed(String failMessage) {
        Toast.makeText(this, failMessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCreateBlockNameSet(String name) {

        featureCollectionForSave.features().get(0).addStringProperty("description", name);
        createMapPresenter.saveMapDataIntoDB(featureCollectionForSave);
        mapNameEnterDialog.dismiss();

    }
}