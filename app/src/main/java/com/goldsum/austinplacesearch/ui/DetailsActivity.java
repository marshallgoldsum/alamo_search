package com.goldsum.austinplacesearch.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.goldsum.austinplacesearch.DetailsViewModel;
import com.goldsum.austinplacesearch.GlideApp;
import com.goldsum.austinplacesearch.R;
import com.goldsum.austinplacesearch.model.PlaceResult;
import com.google.android.gms.maps.model.LatLng;

public class DetailsActivity extends AppCompatActivity {

    public static final String PLACE_ID_EXTRA = "place_id";
    private static final String TAG = DetailsActivity.class.getName();

    private ImageView mMapImage;
    private TextView mPlaceName;
    private TextView mPlaceCategory;
    private TextView mPlaceUrl;
    private ImageView mFavoriteIcon;
    private TextView mPlaceDistance;
    private String mPlaceId;

    private DetailsViewModel detailsViewModel;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);


        mPlaceDistance = findViewById(R.id.details_place_distance);
        mPlaceName = findViewById(R.id.details_place_name);
        mPlaceCategory = findViewById(R.id.details_place_category);
        mPlaceUrl = findViewById(R.id.details_place_url);
        mPlaceUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlaceUrl.getText() != null) {
                    launchBrowserIntent();
                }
            }
        });
        mMapImage = findViewById(R.id.details_map_image);
        mFavoriteIcon = findViewById(R.id.details_favorite_icon);
        mFavoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsViewModel.updateFavorite().observe(DetailsActivity.this, placeResult -> {
                    updatePlaceUI(placeResult);
                });
            }
        });

        mPlaceId = getIntent().getStringExtra(PLACE_ID_EXTRA);

        detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        detailsViewModel.getPlace(mPlaceId).observe(this, placeResult -> {
            updatePlaceUI(placeResult);
            initializeStaticMap(placeResult.getLatLngLocation());
        });
    }

    private void updatePlaceUI(PlaceResult placeResult) {
        mCollapsingToolbar.setTitle(placeResult.getName());
        mPlaceName.setText(placeResult.getName());
        mPlaceCategory.setText(placeResult.getCategory());
        mPlaceUrl.setText(placeResult.getUrl());
        if (placeResult.isFavorite()){
            mFavoriteIcon.setImageResource(R.drawable.ic_favorite);
        } else {
            mFavoriteIcon.setImageResource(R.drawable.ic_favorite_border);
        }
        mPlaceDistance.setText(getString(R.string.details_distance, placeResult.getDistanceFromSearchPoint()));
    }

    private void launchBrowserIntent() {
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(mPlaceUrl.getText().toString()));
        PackageManager packageManager = getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "No Intent available to handle action");
        }
    }

    private void initializeStaticMap(LatLng latLngLocation) {
        String apiKey = getString(R.string.google_static_map_key);
        String url = "https://maps.googleapis.com/maps/api/staticmap?markers=30.2672,-97.7431%7C"+latLngLocation.latitude+","+latLngLocation.longitude+"&size=400x300&key="+apiKey;
        GlideApp.with(this).asBitmap().load(url).into(mMapImage);
    }
}
