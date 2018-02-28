package com.goldsum.austinplacesearch.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.goldsum.austinplacesearch.PlacesViewModel;
import com.goldsum.austinplacesearch.R;
import com.goldsum.austinplacesearch.model.PlaceResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class PlacesMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Austin's city center and move the camera
        LatLng austin_center = new LatLng(30.2672, -97.7431);
        mMap.addMarker(new MarkerOptions().position(austin_center).title(getString(R.string.austin)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(austin_center));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(), marker.getId(), Toast.LENGTH_SHORT).show();
                String placeId = (String)marker.getTag();
                launchDetailsActivity(placeId);
            }
        });
        PlacesViewModel model = ViewModelProviders.of(this).get(PlacesViewModel.class);
        model.getVenues().observe(this, placeResultList -> {
            // update UI
            LatLngBounds bounds = setPlaceResultMarkers(placeResultList);
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, getResources().getDimensionPixelSize(R.dimen.bounds_padding)));
        });
    }

    //add all of the relevant markers to the map and return a bounding box that contains all of the markers
    private LatLngBounds setPlaceResultMarkers(List<PlaceResult> placeResultList) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (PlaceResult placeResult : placeResultList)
        {
            MarkerOptions placeOptions = new MarkerOptions();
            placeOptions.title(placeResult.getName());
            placeOptions.position(placeResult.getLatLngLocation());
            Marker marker = mMap.addMarker(placeOptions);
            marker.setTag(placeResult.getId());
            builder.include(placeResult.getLatLngLocation());
        }
        return builder.build();
    }

    private void launchDetailsActivity(String placeId) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.PLACE_ID_EXTRA, placeId);
        startActivity(intent);
    }
}
