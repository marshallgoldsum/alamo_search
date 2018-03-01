package com.goldsum.austinplacesearch;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by marshallgoldsum on 2/27/18.
 * Required by Glide to access the simpler version of their image fetching calls
 */
@GlideModule
public final class PlacesGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
    }
}