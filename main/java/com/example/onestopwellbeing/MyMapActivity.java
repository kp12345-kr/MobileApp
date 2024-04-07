package com.example.onestopwellbeing;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;




public class
MyMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private SearchView mapSearchView;
    private Geocoder geocoder;
    private static final double RADIUS = 5000;
    private static final String W3W_API_KEY = "QWN3ZFF9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymaps);

        mapSearchView = findViewById(R.id.mapSearch);
        geocoder = new Geocoder(this, Locale.getDefault());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchForAddressWithinRadius(query, RADIUS);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
    }

    private void searchForAddressWithinRadius(String searchQuery, double radius) {
        try {
            List<Address> addresses = geocoder.getFromLocationName(searchQuery, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(latLng).title(searchQuery));
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                convertCoordinatesToW3WInBackground(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class W3WConversionTask extends AsyncTask<Double, Void, String> {
        @Override
        protected String doInBackground(Double... coordinates) {
            try {
                double latitude = coordinates[0];
                double longitude = coordinates[1];
                String apiUrl = "https://api.what3words.com/v3/convert-to-3wa?coordinates=" + latitude + "," + longitude + "&key=" + W3W_API_KEY;
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();
                    Scanner scanner = new Scanner(in);
                    scanner.useDelimiter("\\A");
                    boolean hasInput = scanner.hasNext();
                    if (hasInput) {
                        String response = scanner.next();
                        JSONObject jsonResponse = new JSONObject(response);
                        return jsonResponse.getString("words");
                    }
                    return null;
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String w3wAddress) {
            if (w3wAddress != null) {
            } else {
            }
        }
    }

    private void convertCoordinatesToW3WInBackground(double latitude, double longitude) {
        new W3WConversionTask().execute(latitude, longitude);
    }
    private boolean isWithinRadius(LatLng center, LatLng point, double radius) {
        float[] results = new float[1];
        android.location.Location.distanceBetween(center.latitude, center.longitude, point.latitude, point.longitude, results);
        return results[0] < radius;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_main_activity) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}