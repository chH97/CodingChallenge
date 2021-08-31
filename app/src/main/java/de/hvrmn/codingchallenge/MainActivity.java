package de.hvrmn.codingchallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import de.hvrmn.codingchallenge.datafetching.DataFetcher;
import de.hvrmn.codingchallenge.datafetching.DataFetcherCallback;
import de.hvrmn.codingchallenge.model.Car;
import de.hvrmn.codingchallenge.model.Dataset;
import de.hvrmn.codingchallenge.recycler.OnItemClickListener;
import de.hvrmn.codingchallenge.recycler.OnSnapPositionChangeListener;
import de.hvrmn.codingchallenge.recycler.OnSnapScrollListener;
import de.hvrmn.codingchallenge.recycler.RecyclerAdapter;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String BUNDLE_KEY = "BundleKey";
    private static final int ZOOM_LEVEL_CLOSE = 18;
    private static final int PADDING_AROUND_BOUNDS = 100;

    private MapView mapView;
    private GoogleMap googleMap;
    private LatLngBounds initialFitAllMarkerBounds;
    private List<Marker> markers;
    private Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        markers = new ArrayList<>();
        currentMarker = null;

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
                setUpRecyclerView(dataset);
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
                Marker marker = googleMap.addMarker(new MarkerOptions().position(position).title(car.getName()));
                marker.setTag(car);
                markers.add(marker);
                builder.include(position);
            }
            setUpListeners();
            initialFitAllMarkerBounds = builder.build();
            animateCameraToFitBounds(initialFitAllMarkerBounds);
        } else {
            // TODO
        }
    }

    private void setUpRecyclerView(Dataset dataset) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        RecyclerAdapter adapter = new RecyclerAdapter(this, dataset.getPlacemarks(), new OnItemClickListener<Car>() {
            @Override
            public void onItemClick(Car object, View view) {

            }
        });

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new OnSnapScrollListener(snapHelper, layoutManager, new OnSnapPositionChangeListener() {
            @Override
            public void onSnapPositionChanged(int snapPosition) {
                Marker marker = getMarkerByCarTag(dataset.getPlacemarks().get(snapPosition));
                marker.showInfoWindow();
                displayOnlyOneMarker(marker);
                currentMarker = marker;
                animateCameraToSpecificPosition(marker.getPosition());
            }
        }));
    }

    private void animateCameraToSpecificPosition(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(ZOOM_LEVEL_CLOSE)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private Marker getMarkerByCarTag(Car car) {
        for (Marker marker : markers) {
            if (marker.getTag().equals(car)) {
                return marker;
            }
        }
        return null;
    }

    private void setUpListeners() {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (currentMarker != null && currentMarker.equals(marker)) {
                    setAllMarkersVisible();
                    marker.hideInfoWindow();
                    currentMarker = null;
                    return true;
                } else {
                    currentMarker = marker;
                    displayOnlyOneMarker(marker);
                    marker.showInfoWindow();
                    animateCameraToSpecificPosition(marker.getPosition());
                }
                return true;
            }
        });

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                marker.hideInfoWindow();
                setAllMarkersVisible();
                currentMarker = null;
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                setAllMarkersVisible();
                currentMarker = null;
            }
        });
    }

    private void animateCameraToFitBounds(LatLngBounds bounds) {
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, PADDING_AROUND_BOUNDS);
        googleMap.animateCamera(cu);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
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

    private void displayOnlyOneMarker(Marker highlightedMarker) {
        for (Marker marker : markers) {
            if (!marker.equals(highlightedMarker)) {
                marker.setVisible(false);
            } else {
                marker.setVisible(true);
                marker.showInfoWindow();
            }
        }
    }

    private void setAllMarkersVisible() {
        for (Marker marker : markers) {
            marker.setVisible(true);
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