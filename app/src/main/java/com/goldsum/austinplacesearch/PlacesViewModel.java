package com.goldsum.austinplacesearch;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.goldsum.austinplacesearch.model.PlaceResult;

import java.util.List;

/**
 * Created by marshallgoldsum on 2/27/18.
 */

public class PlacesViewModel extends ViewModel {

    private String mCurrentQuery;

    public LiveData<List<PlaceResult>> searchVenues(String query) {
        if (!query.equals(mCurrentQuery)){
            mCurrentQuery = query;
            PlaceRepository.getInstance().searchVenues(mCurrentQuery);
        }
        return PlaceRepository.getInstance().getPlaces();
    }

    public LiveData<List<PlaceResult>> getVenues() {
        return PlaceRepository.getInstance().getPlaces();
    }

    public LiveData<PlaceResult> toggleFavorite(String placeId) {
        PlaceResult placeResult = PlaceRepository.getInstance().updateFavorite(placeId);

        MutableLiveData<PlaceResult> result = null;
        if (placeResult != null){
            result = new MutableLiveData<>();
            result.setValue(placeResult);
        }
        return result;
    }
}