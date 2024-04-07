package com.example.onestopwellbeing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.example.onestopwellbeing.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private BottomNavigationView bottomNavigationView;


    // Define service locations
    private LatLng[] serviceLocations = {
            new LatLng(52.955, -1.156),
            new LatLng(52.957, -1.153),
            new LatLng(52.953, -1.151),
            new LatLng(52.951, -1.157),
            new LatLng(52.9604, -1.146)
    };

    private String[] serviceTitles = {
            "Yoga Studio",
            "Mental Health Services",
            "Psychotherapy Services",
            "Physiotherapy Services",
            "Deep Yoga"
    };

    private int[] serviceImageResources = {
            R.drawable.yoga_image,
            R.drawable.mental_health_image,
            R.drawable.psychotherapy_image,
            R.drawable.physiotherapy_image,
            R.drawable.deep_yoga_image
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.maps_item:
                    navigateToActivity(MyMapActivity.class);
                    return true;
                case R.id.navigation_profile:
                    navigateToActivity(Search.class);
                    return true;
                case R.id.navigation_home:
                    navigateToActivity(MainActivity.class);
                    return true;
            }
            return false;
        });

    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(MapsActivity.this, activityClass);
        startActivity(intent);
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < serviceLocations.length; i++) {
            addServiceMarker(serviceLocations[i], serviceTitles[i], serviceImageResources[i]);
        }

        performPostcodeRadiusSearch("NG1 4EE", 5.0);

        LatLng defaultLocation = new LatLng(52.9548, -1.1581);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Handle marker click event
                handleMarkerClick(marker);
                return true;
            }
        });
    }

    private void addServiceMarker(LatLng position, String title, int imageResourceId) {
        MarkerOptions options = new MarkerOptions().position(position).title(title);
        Marker marker = mMap.addMarker(options);
        marker.setTag(new MarkerTag(title, imageResourceId));
    }

    private void performPostcodeRadiusSearch(String postcode, double radius) {
        LatLng postcodeCoordinates = convertPostcodeToCoordinates(postcode);
        if (postcodeCoordinates == null) {
            return;
        }

        for (int i = 0; i < serviceLocations.length; i++) {
            LatLng serviceLocation = serviceLocations[i];
            String serviceTitle = serviceTitles[i];
            int serviceImageResource = serviceImageResources[i];

            double distance = calculateDistance(postcodeCoordinates, serviceLocation);

            if (distance <= radius) {
                addServiceMarker(serviceLocation, serviceTitle, serviceImageResource);
            }
        }
    }

    private void handleMarkerClick(Marker marker) {
        MarkerTag markerTag = (MarkerTag) marker.getTag();
        if (markerTag != null) {
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                    TextView titleTextView = view.findViewById(R.id.titleTextView);
                    titleTextView.setText(marker.getTitle());

                    ImageView imageView = view.findViewById(R.id.imageView);
                    imageView.setImageResource(markerTag.getImageResourceId());

                    Button viewMoreButton = view.findViewById(R.id.viewMoreButton);
                    viewMoreButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MapsActivity.this, ServiceDetails.class);
                            intent.putExtra("title", marker.getTitle());
                            startActivity(intent);
                        }
                    });

                    return view;
                }
            });
            marker.showInfoWindow();
        }
    }

    private static class MarkerTag {
        private String title;
        private int imageResourceId;

        public MarkerTag(String title, int imageResourceId) {
            this.title = title;
            this.imageResourceId = imageResourceId;
        }

        public String getTitle() {
            return title;
        }

        public int getImageResourceId() {
            return imageResourceId;
        }
    }

    private LatLng convertPostcodeToCoordinates(String postcode) {
        if ("NG1 4EE".equals(postcode)) {
            return new LatLng(52.956418, -1.148516);
        }
        return null;
    }

    private double calculateDistance(LatLng point1, LatLng point2) {
        final int R = 6371;

        double lat1 = Math.toRadians(point1.latitude);
        double lon1 = Math.toRadians(point1.longitude);
        double lat2 = Math.toRadians(point2.latitude);
        double lon2 = Math.toRadians(point2.longitude);

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
