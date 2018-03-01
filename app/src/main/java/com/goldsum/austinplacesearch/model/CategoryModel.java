package com.goldsum.austinplacesearch.model;

/**
 * Created by marshallgoldsum on 2/26/18.
 * POJO for a Foursquare category
 */

public class CategoryModel {

    private String id;
    private String name;
    private IconModel icon;
    private boolean primary;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public IconModel getIcon() {
        return icon;
    }

    public boolean isPrimary() {
        return primary;
    }
}
