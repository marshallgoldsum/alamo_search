package com.goldsum.austinplacesearch;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.goldsum.austinplacesearch.database.FavoriteDao;
import com.goldsum.austinplacesearch.database.FavoriteDatabase;
import com.goldsum.austinplacesearch.model.Favorite;
import com.goldsum.austinplacesearch.model.PlaceResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by marshallgoldsum on 2/27/18.
 * This Repository is responsible for owning the data and its truth.  All of the ViewModels get
 * their information from here to make sure the data is consistent between activities
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
        placeResults = new MutableLiveData<>();
    }

    public void searchVenues(String query) {

        loadPlaces(query);
    }

    private void loadPlaces(String query) {
        PlacesNetworkManager.getInstance().searchVenues(query, new Callback<PlacesNetworkManager.FoursquareResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlacesNetworkManager.FoursquareResponse> call, @NonNull Response<PlacesNetworkManager.FoursquareResponse> response) {
                if(response.isSuccessful()) {
                    new AsyncLoadFavorites().execute(response.body().response.getPlaces());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlacesNetworkManager.FoursquareResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public LiveData<List<PlaceResult>> getPlaces() {
        return placeResults;
    }

    public PlaceResult getPlaceById(@NonNull String id){
        if (placeResults.getValue() != null) {
            for (PlaceResult placeResult : placeResults.getValue()) {
                Log.d(TAG, "getPlaceById() called with: id = [" + placeResult.getId() + "]");
                if (id.equals(placeResult.getId())) {
                    return placeResult;
                }
            }
        }
        return null;
    }

    public PlaceResult updateFavorite(String id) {
        if (placeResults.getValue() != null) {
            for (PlaceResult place : placeResults.getValue()) {
                if (id.equals(place.getId())) {
                    place.toggleFavorite();
                    updateFavoriteInDatabase(id, place.isFavorite());
                    return place;
                }
            }
        }
        return null;
    }

    //Accessing the database needs to be done off the main thread, so we create AsyncTask objects
    //to handle it
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
                Log.d(TAG, "doInBackground() called with: result = [" + result + "]");
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
