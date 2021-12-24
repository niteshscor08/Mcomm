package com.mvine.mcomm.di

import android.app.Application
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.service.WebSocketService
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.StreamAdapter
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {

    @Singleton
    @Provides
    fun provideOkhttp() =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()

    @Singleton
    @Provides
    fun provideLifeCycle(application: Application) = AndroidLifecycle.ofApplicationForeground(application)

    @Singleton
    @Provides
    fun provideScarlet(
        client: OkHttpClient,
        lifecycle: Lifecycle
    ) =
        Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory(BuildConfig.ENDPOINT))
            .lifecycle(lifecycle)
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun provideWebSocketService(scarlet: Scarlet) : WebSocketService= scarlet.create(WebSocketService::class.java)

}