package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class MapsActivity extends AppCompatActivity {
    private MapView mapView;

    private static final String TAG = "MapsActivity";


    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, MapsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On create called");
        Mapbox.getInstance(this, getString(R.string.mapbox_key));

        Log.d(TAG, "Map Instance retrieved");
        setContentView(R.layout.activity_maps);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
    }

    public void onMapReady(MapboxMap mapboxMap) {

        LocationComponent locationComponent = mapboxMap.getLocationComponent();

        // TODO: figure out how to fix this, checkpoint 6? NFC: security
        locationComponent.activateLocationComponent(this);
        locationComponent.setLocationComponentEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
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