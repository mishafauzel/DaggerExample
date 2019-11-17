package com.example.myapplication.di;

import com.example.myapplication.di.auth.AuthModule;
import com.example.myapplication.di.auth.AuthScope;
import com.example.myapplication.di.auth.AuthViewModelsModule;
import com.example.myapplication.di.main.MainFragmentBuildersModule;
import com.example.myapplication.di.main.MainModule;
import com.example.myapplication.di.main.MainScope;
import com.example.myapplication.di.main.MainViewModelsModule;
import com.example.myapplication.ui.auth.AuthActivity;
import com.example.myapplication.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
            modules = {AuthViewModelsModule.class, AuthModule.class})
    abstract AuthActivity contributeAuthActivity();


    @MainScope
    @ContributesAndroidInjector(
            modules = {MainFragmentBuildersModule.class, MainViewModelsModule.class, MainModule.class}
    )
    abstract MainActivity contributeMainActivity();

}
