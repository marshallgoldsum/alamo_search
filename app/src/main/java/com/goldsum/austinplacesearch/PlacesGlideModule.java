package com.goldsum.austinplacesearch;

import android.app.ActivityManager;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by marshallgoldsum on 2/27/18.
 */
@GlideModule
public final class PlacesGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        builder.setBitmapPool(new LruBitmapPool(1028*memoryClass/10));
        builder.setMemoryCache(new LruResourceCache(1028*memoryClass/10));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
    }
}