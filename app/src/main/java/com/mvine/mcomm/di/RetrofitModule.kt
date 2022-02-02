package com.mvine.mcomm.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.data.api.CallsApiService
import com.mvine.mcomm.data.api.ChangePasswordApiService
import com.mvine.mcomm.data.api.ChatsApiService
import com.mvine.mcomm.data.api.ContactsApiService
import com.mvine.mcomm.data.api.LoginApiService
import com.mvine.mcomm.data.api.LogoutApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .followRedirects(false)
        val logging =
            HttpLoggingInterceptor().setLevel(
                if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE // Add Logger only for Debug variant
            )
        okHttpClient.addInterceptor(logging)
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideLoginApi(retrofit: Retrofit.Builder): LoginApiService {
        return retrofit.build().create(LoginApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideContactsApi(retrofit: Retrofit.Builder): ContactsApiService {
        return retrofit.build().create(ContactsApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideChatsApi(retrofit: Retrofit.Builder): ChatsApiService {
        return retrofit.build().create(ChatsApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideCallsApi(retrofit: Retrofit.Builder): CallsApiService {
        return retrofit.build().create(CallsApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideChangePasswordApiService(retrofit: Retrofit.Builder): ChangePasswordApiService {
        return retrofit.build().create(ChangePasswordApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideLogoutApiService(retrofit: Retrofit.Builder): LogoutApiService {
        return retrofit.build().create(LogoutApiService::class.java)
    }
}
