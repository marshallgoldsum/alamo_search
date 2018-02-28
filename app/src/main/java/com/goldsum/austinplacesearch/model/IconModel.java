package com.goldsum.austinplacesearch.model;

/**
 * Created by marshallgoldsum on 2/26/18.
 */

public class IconModel {
    private String prefix;
    private String suffix;

    public String getIconUrl()
    {
        return prefix + "64" + suffix;
    }
}
