package com.mvine.mcomm.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.data.api.LoginApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideLoginApi(retrofit: Retrofit.Builder): LoginApiService {
        return retrofit.build().create(LoginApiService::class.java)
    }

}