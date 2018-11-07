package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.optimization.v1.MapboxOptimization;
import com.mapbox.api.optimization.v1.models.OptimizationResponse;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;

import static com.mapbox.core.constants.Constants.PRECISION_6;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// Most of this comes from https://github.com/mapbox/mapbox-android-demo/blob/master/MapboxAndroidDemo/src/main/java/com/mapbox/mapboxandroiddemo/examples/plugins/LocationComponentFragmentActivity.java
// and its corresponding xml file
// Also here https://www.mapbox.com/android-docs/java/examples/generate-an-optimized-route/
public class MapsActivity extends AppCompatActivity implements PermissionsListener,
        MapboxMap.OnMapClickListener, MapboxMap.OnMapLongClickListener, View.OnClickListener {

    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private DirectionsRoute optimizedRoute;
    private MapboxOptimization optimizedClient;
    private Polyline optimizedPolyline;
    private List<Point> stops;
    private double mDist;
    private TextView mDistView;
    private Button mBeginButton;
    // constant strings for the optimized route
    private static final String FIRST = "first";
    private static final String ANY = "any";
    private static final String TEAL_COLOR = "#23D2BE";
    private static final int POLYLINE_WIDTH = 5;

    private static final String TAG = "MapsActivity";


    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, MapsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        stops = new ArrayList<>();

        mDistView = this.findViewById(R.id.distance_view);
        mBeginButton = this.findViewById(R.id.begin_button);

        if(mBeginButton!=null){
            mBeginButton.setOnClickListener(this);
        }

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_key));

        // Create supportMapFragment
        SupportMapFragment mapFragment;
        if (savedInstanceState == null) {

            // Create fragment
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Build mapboxMap
            MapboxMapOptions options = new MapboxMapOptions();
            options.camera(new CameraPosition.Builder()
                    .zoom(15)
                    .build());

            // Create map fragment
            mapFragment = SupportMapFragment.newInstance(options);

            // Add map fragment to parent container
            transaction.add(R.id.location_frag_container, mapFragment, "com.mapbox.map");
            transaction.commit();
        } else {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
        }

        mapFragment.getMapAsync(mapboxMap -> {
            MapsActivity.this.mapboxMap = mapboxMap;
            mapboxMap.addOnMapClickListener(this);
            mapboxMap.addOnMapLongClickListener(this);
            enableLocationComponent();
        });
    }


    @Override
    public void onClick(View v) {
        ActivityRecord act = new ActivityRecord(ActivityRecord.WALK);
        act.setstart_Time( new Date());
        act.setwalk_dist(mDist);
        List<List<Double>> coords = new ArrayList<List<Double>>();
        for (Point p: stops){
            coords.add(p.coordinates());
        }
        act.setroute(coords);
        CurrentUser.addActivity(act);
        Intent intent = HomeScreenActivity.newIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        // Optimization API is limited to 12 coordinate sets
        if (alreadyTwelveMarkersOnMap()) {
            // TODO fix the toast
            Toast.makeText(MapsActivity.this, "Too many pins.", Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "onMapClick called");
            addDestinationMarker(point);
            addPointToStopsList(point);
            if (stops.size() >= 2) {
                getOptimizedRoute(stops);
            }
        }
    }

    @Override
    public void onMapLongClick(@NonNull LatLng point) {
        Log.d(TAG, "onMapLongClick called");
        mapboxMap.clear();
        stops.clear();
        mDistView.setText(getString(R.string.distance));
        //addFirstStopToStopsList();
    }

    private boolean alreadyTwelveMarkersOnMap() {
        if (stops.size() == 12) {
            return true;
        } else {
            return false;
        }
    }


    private void addPointToStopsList(LatLng point) {
        stops.add(Point.fromLngLat(point.getLongitude(), point.getLatitude()));
    }

    private void addDestinationMarker(LatLng point) {
        Log.d(TAG, "addDestinationMarker called");
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(point.getLatitude(), point.getLongitude()))
                .title("Destination")); // TODO fix this string
    }


    private void getOptimizedRoute(List<Point> coordinates) {
        optimizedClient = MapboxOptimization.builder()
                .source(FIRST)
                .destination(ANY)
                .coordinates(coordinates)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(Mapbox.getAccessToken())
                .build();

        optimizedClient.enqueueCall(new Callback<OptimizationResponse>() {
            @Override
            public void onResponse(Call<OptimizationResponse> call, Response<OptimizationResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d("DirectionsActivity", "Nope"); // TODO fix with string
                    Toast.makeText(MapsActivity.this, "Nope", Toast.LENGTH_SHORT).show(); // TODO fix with string
                    return;
                } else {
                    if (response.body().trips().isEmpty()) {
                        Log.d("DirectionsActivity", "Yes, but no" + " size = " + response.body().trips().size());
                        Toast.makeText(MapsActivity.this, "Yes, but no", // TODO fix with string
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Get most optimized route from API response
                optimizedRoute = response.body().trips().get(0);
                drawOptimizedRoute(optimizedRoute);
            }

            @Override
            public void onFailure(Call<OptimizationResponse> call, Throwable throwable) {
                Log.d("DirectionsActivity", "Error: " + throwable.getMessage());
            }
        });
    }

    private void drawOptimizedRoute(DirectionsRoute route) {
        // Remove old polyline
        if (optimizedPolyline != null) {
            mapboxMap.removePolyline(optimizedPolyline);
        }
        // Draw points on MapView
        DecimalFormat df = new DecimalFormat("#.00");
        mDist = route.distance()/1609.344;
        mDistView.setText(getString(R.string.distance) + " " + df.format(mDist) + " mi");
        LatLng[] pointsToDraw = convertLineStringToLatLng(route);
        optimizedPolyline = mapboxMap.addPolyline(new PolylineOptions()
                .add(pointsToDraw)
                .color(Color.parseColor(TEAL_COLOR))
                .width(POLYLINE_WIDTH));
    }

    private LatLng[] convertLineStringToLatLng(DirectionsRoute route) {
        // Convert LineString coordinates into LatLng[]
        LineString lineString = LineString.fromPolyline(route.geometry(), PRECISION_6);
        List<Point> coordinates = lineString.coordinates();
        LatLng[] points = new LatLng[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            points[i] = new LatLng(
                    coordinates.get(i).latitude(),
                    coordinates.get(i).longitude());
        }
        return points;
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Get an instance of the location component. Adding in LocationComponentOptions is also an optional
            // parameter
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        // TODO fix this toast
        Toast.makeText(this, "Cannot plan walks without location permission.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent();
        } else {
            // TODO fix the toast
            Toast.makeText(this, "Cannot plan walks without location permission.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}