package com.goldsum.austinplacesearch.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.goldsum.austinplacesearch.model.Favorite;

/**
 * Created by marshallgoldsum on 2/28/18.
 * Room architecture database for storing favorite information in SQL
 */

@Database(entities = {Favorite.class}, version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase{
    public abstract FavoriteDao favoriteDao();
}
