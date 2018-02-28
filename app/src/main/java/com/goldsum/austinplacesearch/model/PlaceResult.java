package com.goldsum.austinplacesearch.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by marshallgoldsum on 2/26/18.
 * POJO for holding place information
 */

public class PlaceResult{

    private String id;
    private String name;
    private LocationModel location;
    private ArrayList<CategoryModel> categories;
    private String url;
    private boolean favorite;

    public String getName() {
        return name;
    }

    public LatLng getLatLngLocation() {
        return new LatLng(location.getLat(), location.getLng());
    }

    public double getDistanceFromSearchPoint()
    {
        return location.getDistance();
    }

    public String getIconUrl() {
        for (CategoryModel categoryModel : categories)
        {
            if (categoryModel.isPrimary()){
                return categoryModel.getIcon().getIconUrl();
            }
        }
        return "";
    }

    public String getCategory() {
        for (CategoryModel categoryModel : categories)
        {
            if (categoryModel.isPrimary()){
                return categoryModel.getName();
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "PlaceResult{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public boolean toggleFavorite() {
        favorite = !favorite;
        return favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
