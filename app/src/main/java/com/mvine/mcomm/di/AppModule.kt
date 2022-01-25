package com.mvine.mcomm.di

import android.content.Context
import com.mvine.mcomm.util.PreferenceHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun getApplicationContext(@ApplicationContext context: Context) = context

    @Singleton
    @Provides
    fun providePreferenceHandler(@ApplicationContext context: Context): PreferenceHandler = PreferenceHandler(context)
}
