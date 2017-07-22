package jakedeakin.zombiesirl;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.constants.MyBearingTracking;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PermissionsListener{

    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mapbox Access token
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate((savedInstanceState));
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                map = mapboxMap;

                permissionsManager = new PermissionsManager(MainActivity.this);
                if (!PermissionsManager.areLocationPermissionsGranted(MainActivity.this)) {
                    permissionsManager.requestLocationPermissions(MainActivity.this);
                } else {
                    enableLocationTracking();
                }

            }
        });

    }

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

    private void enableLocationTracking() {

        map.setMyLocationEnabled(true);

        // Disable tracking dismiss on map gesture
        map.getTrackingSettings().setDismissAllTrackingOnGesture(false);

        // Enable location and bearing tracking
        map.getTrackingSettings().setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
        map.getTrackingSettings().setMyBearingTrackingMode(MyBearingTracking.COMPASS);
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationTracking();
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted,
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
