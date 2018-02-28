package com.goldsum.austinplacesearch;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.goldsum.austinplacesearch.model.Favorite;
import com.goldsum.austinplacesearch.model.PlaceResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by marshallgoldsum on 2/27/18.
 */
public class PlaceRepository {
    private static final String TAG = PlaceRepository.class.getName();
    private static PlaceRepository sPlaceRepository;

    public static PlaceRepository getInstance()
    {
        return sPlaceRepository;
    }

    public static void init(Context context)
    {
        if (sPlaceRepository == null){
            sPlaceRepository = new PlaceRepository();
            sPlaceRepository.initializeDatabase(context);
        }
    }

    private void initializeDatabase(Context context) {
        FavoriteDatabase favoriteDatabase = Room.databaseBuilder(context, FavoriteDatabase.class, "favorite_database").build();
        favoriteDao = favoriteDatabase.favoriteDao();
    }

    private FavoriteDao favoriteDao;
    private MutableLiveData<List<PlaceResult>> placeResults;

    private PlaceRepository()
    {
    }

    public void searchVenues(String query) {
        if (placeResults == null) {
            placeResults = new MutableLiveData<>();
        }
        loadPlaces(query);
    }

    private void loadPlaces(String query) {
        PlacesNetworkManager.getInstance().searchVenues(query, new Callback<PlacesNetworkManager.FoursquareResponse>() {
            @Override
            public void onResponse(Call<PlacesNetworkManager.FoursquareResponse> call, Response<PlacesNetworkManager.FoursquareResponse> response) {
                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                if(response.isSuccessful() && response.body() != null) {
                    new AsyncLoadFavorites().execute(response.body().response.getPlaces());
                }
            }

            @Override
            public void onFailure(Call<PlacesNetworkManager.FoursquareResponse> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });
    }

    public LiveData<List<PlaceResult>> getPlaces() {
        if (placeResults == null) {
            placeResults = new MutableLiveData<>();
        }
        return placeResults;
    }

    public PlaceResult getPlaceById(@NonNull String id){
        for (PlaceResult placeResult : placeResults.getValue()) {
            Log.d(TAG, "getPlaceById() called with: id = [" + placeResult.getId() + "]");
            if (id.equals(placeResult.getId())){
                return placeResult;
            }
        }
        return null;
    }

    public PlaceResult updateFavorite(String id) {

        //update
        for (PlaceResult place : placeResults.getValue()) {
            if (id.equals(place.getId())) {
                place.toggleFavorite();
                updateFavoriteInDatabase(id, place.isFavorite());
                return place;
            }
        }
        return null;
    }

    private class AsyncLoadFavorites extends AsyncTask<List<PlaceResult>, Void, List<PlaceResult>>
    {
        @Override
        protected void onPostExecute(List<PlaceResult> results) {
            super.onPostExecute(results);
            placeResults.setValue(results);
        }

        @Override
        protected List<PlaceResult> doInBackground(List<PlaceResult>[] lists) {
            List<PlaceResult> list = lists[0];
            for (PlaceResult result : list) {
                Favorite favorite = favoriteDao.getFavoriteById(result.getId());
                if (favorite != null) {
                    result.setFavorite(favorite.isFavorite());
                }
            }
            return list;
        }
    }

    private class AsyncUpdateFavorite extends AsyncTask<Favorite, Void, Void>
    {
        @Override
        protected Void doInBackground(Favorite... favorites) {
            for (Favorite favorite :
                    favorites) {
                if (favorite.isFavorite()) {
                    //add to database
                    favoriteDao.insertFavorite(favorite);
                } else {
                    //remove from database
                    favoriteDao.deleteFavorite(favorite);
                }
            }
            return null;
        }
    }

    private void updateFavoriteInDatabase(final String id, final boolean favorite) {
        new AsyncUpdateFavorite().execute(new Favorite(id, favorite));
    }
}
