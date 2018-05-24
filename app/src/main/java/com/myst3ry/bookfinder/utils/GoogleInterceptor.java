package com.myst3ry.bookfinder.utils;

import android.support.annotation.NonNull;

import com.myst3ry.bookfinder.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class GoogleInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull final Chain chain) throws IOException {
        final Request originalRequest = chain.request();
        final HttpUrl url = originalRequest.url().newBuilder()
                .addQueryParameter("key", BuildConfig.KEY)
                .build();
        return chain.proceed(originalRequest.newBuilder().url(url).build());
    }
}

