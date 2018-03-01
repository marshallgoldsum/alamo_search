package com.goldsum.austinplacesearch.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by marshallgoldsum on 2/28/18.
 */

@Entity
public class Favorite {
    @PrimaryKey @NonNull
    private String id;

    private boolean favorite;

    public Favorite(String id, boolean favorite) {
        this.id = id;
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "id='" + id + '\'' +
                ", favorite=" + favorite +
                '}';
    }
}
