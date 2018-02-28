package com.goldsum.austinplacesearch;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.goldsum.austinplacesearch.model.PlaceResult;

/**
 * Created by marshallgoldsum on 2/27/18.
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

    public boolean updateFavorite()
    {
        PlaceResult placeResult = mPlace.getValue();
        boolean favorite = placeResult.toggleFavorite();
        mPlace.postValue(placeResult);
//        PlaceRepository.getInstance().updateFavorite(mPlace.getValue().getId(), favorite);
        return favorite;
    }
}
