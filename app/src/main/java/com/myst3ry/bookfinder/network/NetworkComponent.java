package com.myst3ry.bookfinder.network;

import com.myst3ry.bookfinder.ui.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class})
public interface NetworkComponent {

    void inject(MainActivity mainActivity);

    void inject(BookFinderGlideModule bookFinderGlideModule);
}

