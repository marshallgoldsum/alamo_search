package com.goldsum.austinplacesearch.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.goldsum.austinplacesearch.PlaceResultViewHolder;
import com.goldsum.austinplacesearch.PlacesViewModel;
import com.goldsum.austinplacesearch.R;

public class MainActivity extends AppCompatActivity implements PlaceResultViewHolder.PlaceCardActionListener {

    private RecyclerView mResultsView;
    private PlaceResultsAdapter mResultsAdapter;
    private PlacesViewModel mPlacesViewModel;
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                mPlacesViewModel.searchVenues(s.toString()).observe(MainActivity.this, placeResultList -> {
                    if (placeResultList != null) {
                        // update UI
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
        mResultsView = findViewById(R.id.places_result_list);
        mResultsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mResultsView.setAdapter(mResultsAdapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
