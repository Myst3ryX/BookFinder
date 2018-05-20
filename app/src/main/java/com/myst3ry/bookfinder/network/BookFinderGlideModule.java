package com.myst3ry.bookfinder.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.myst3ry.bookfinder.BookFinderApp;

import java.io.InputStream;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

@GlideModule
public final class BookFinderGlideModule extends AppGlideModule {

    @Inject
    OkHttpClient okHttpClient;

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        BookFinderApp.getNetworkComponent(context).inject(this);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }
}
