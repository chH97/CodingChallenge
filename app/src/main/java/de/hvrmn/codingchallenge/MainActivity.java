package de.hvrmn.codingchallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import de.hvrmn.codingchallenge.datafetching.DataFetcher;
import de.hvrmn.codingchallenge.datafetching.DataFetcherCallback;
import de.hvrmn.codingchallenge.model.Car;
import de.hvrmn.codingchallenge.model.Dataset;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String BUNDLE_KEY = "BundleKey";

    private MapView mapView;
    private GoogleMap googleMap;

    private LatLngBounds initialFitAllMarkerBounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpMapView(savedInstanceState);

        retrieveData();
    }

    private void setUpMapView(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(BUNDLE_KEY);
        }
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    private void retrieveData() {
        DataFetcher dataFetcher = new DataFetcher(this);
        dataFetcher.fetchData(new DataFetcherCallback() {
            @Override
            public void onDataRetrieved(Dataset dataset) {
                addAllMarkersToMap(dataset);
            }

            @Override
            public void onError() {
                // TODO
            }
        });
    }

    private void addAllMarkersToMap(Dataset dataset) {
        if (googleMap != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Car car : dataset.getPlacemarks()) {
                LatLng position = new LatLng(car.getCoordinates().get(1), car.getCoordinates().get(0));
                googleMap.addMarker(new MarkerOptions().position(position).title(car.getName()));
                builder.include(position);
            }
            initialFitAllMarkerBounds = builder.build();
            animateCameraToFitBounds(initialFitAllMarkerBounds);
        } else {
            // TODO
        }
    }

    private void animateCameraToFitBounds(LatLngBounds bounds) {
        int padding = 100;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        requestPermissionAndEnableMyLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestPermissionAndEnableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
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
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}