package com.goldsum.austinplacesearch.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.goldsum.austinplacesearch.PlacesViewModel;
import com.goldsum.austinplacesearch.R;

public class MainActivity extends AppCompatActivity implements PlaceResultViewHolder.PlaceCardActionListener {

    private PlaceResultsAdapter mResultsAdapter;
    private PlacesViewModel mPlacesViewModel;
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mPlacesViewModel = ViewModelProviders.of(this).get(PlacesViewModel.class);


        mFab = findViewById(R.id.map_button);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(MainActivity.this, PlacesMapActivity.class);
                startActivity(mapIntent);
            }
        });

        EditText editText = findViewById(R.id.search_place);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //The query changed so we want to fetch new data and update the UI when the data
                //changes
                mPlacesViewModel.searchVenues(s.toString()).observe(MainActivity.this, placeResultList -> {
                    if (placeResultList != null) {
                        mResultsAdapter.setPlaces(placeResultList);
                        mFab.setVisibility(placeResultList.isEmpty() ? View.INVISIBLE : View.VISIBLE);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mResultsAdapter = new PlaceResultsAdapter(this);
        RecyclerView resultsRecyclerView = findViewById(R.id.places_result_list);
        resultsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        resultsRecyclerView.setAdapter(mResultsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlacesViewModel.getVenues().observe(this, placeResults -> {
            mResultsAdapter.setPlaces(placeResults);
        });
    }

    @Override
    public void onFavoriteClicked(String placeId) {
        mPlacesViewModel.toggleFavorite(placeId).observe(this, placeResult -> {
            mResultsAdapter.updatePlace(placeResult);
        });
    }
}
