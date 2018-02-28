package com.goldsum.austinplacesearch;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.goldsum.austinplacesearch.model.Favorite;

/**
 * Created by marshallgoldsum on 2/28/18.
 */

@Database(entities = {Favorite.class}, version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase{
    public abstract FavoriteDao favoriteDao();
}
