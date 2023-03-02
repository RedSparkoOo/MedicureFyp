package com.example.docportal.Patient;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.docportal.R;
import com.example.docportal.databinding.ActivityAllMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class allMaps extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityAllMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        binding = ActivityAllMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.lab_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

     try {
         Bundle recieved_coordinates = getIntent().getExtras();
         double rec_latitude = recieved_coordinates.getDouble("latitude");
         double rec_longitude = recieved_coordinates.getDouble("longitude");
         String rec_lab_name= recieved_coordinates.getString("lab_name");


         LatLng lab_bank = new LatLng(rec_latitude, rec_longitude);
         float zoom = 20;
         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lab_bank, zoom));
         mMap.addMarker(new MarkerOptions().position(lab_bank).title(rec_lab_name));
         mMap.moveCamera(CameraUpdateFactory.newLatLng(lab_bank));
     }
     catch (Exception e){
         Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
     }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}