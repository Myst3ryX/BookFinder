package com.myst3ry.bookfinder;

import android.app.Application;
import android.content.Context;

import com.myst3ry.bookfinder.network.DaggerNetworkComponent;
import com.myst3ry.bookfinder.network.NetworkComponent;
import com.myst3ry.bookfinder.network.NetworkModule;

public final class BookFinderApp extends Application {

    private NetworkComponent networkComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        prepareDaggerComponents();
    }

    private void prepareDaggerComponents() {
        networkComponent = DaggerNetworkComponent.builder()
                .networkModule(new NetworkModule())
                .build();
    }

    public static NetworkComponent getNetworkComponent(Context context) {
        return ((BookFinderApp) context.getApplicationContext()).networkComponent;
    }
}
