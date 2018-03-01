package com.goldsum.austinplacesearch;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.goldsum.austinplacesearch.model.PlaceResult;

/**
 * Created by marshallgoldsum on 2/27/18.
 * This ViewModel handles the data for the DetailsActivity.
 */

public class DetailsViewModel extends ViewModel {

    private MutableLiveData<PlaceResult> mPlace;


    public LiveData<PlaceResult> getPlace(String place_id)
    {
        if (mPlace == null){
            mPlace = new MutableLiveData<>();
        }

        mPlace.postValue(PlaceRepository.getInstance().getPlaceById(place_id));

        return mPlace;
    }

    public LiveData<PlaceResult> updateFavorite()
    {
        PlaceResult placeResult = PlaceRepository.getInstance().updateFavorite(mPlace.getValue().getId());
        if (placeResult != null){
            mPlace.setValue(placeResult);
        }
        return mPlace;
    }
}
