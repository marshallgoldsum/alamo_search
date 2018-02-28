package com.goldsum.austinplacesearch;

import android.app.Application;

/**
 * Created by marshallgoldsum on 2/28/18.
 */

public class PlacesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PlaceRepository.init(this);
    }

}
