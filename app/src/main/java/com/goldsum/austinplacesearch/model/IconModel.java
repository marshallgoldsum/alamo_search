package com.goldsum.austinplacesearch.model;

/**
 * Created by marshallgoldsum on 2/26/18.
 * POJO for Foursquare icon
 */

public class IconModel {
    private String prefix;
    private String suffix;

    //Add 64 in between the prefix and suffix to select that specific version of the icon asset
    public String getIconUrl()
    {
        return prefix + "64" + suffix;
    }
}
