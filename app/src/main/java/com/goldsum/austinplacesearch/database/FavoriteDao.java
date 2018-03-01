package com.goldsum.austinplacesearch.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.goldsum.austinplacesearch.model.Favorite;

/**
 * Created by marshallgoldsum on 2/28/18.
 * Room architecture Dao (Database Access Object) for interacting with FavoriteDatabase
 */

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorite WHERE id = :placeId")
    Favorite getFavoriteById(String placeId);

    @Insert
    void insertFavorite(Favorite favorite);

    @Delete
    void deleteFavorite(Favorite favorite);
}
