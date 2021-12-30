package com.mvine.mcomm.di

import android.app.Application
import android.content.Context
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.janus.JanusManager
import com.mvine.mcomm.presentation.common.GsonMessageAdapter
import com.mvine.mcomm.janus.WebSocketService
import com.mvine.mcomm.util.LOGIN_TOKEN
import com.mvine.mcomm.util.MCOMM_SHARED_PREFERENCES
import com.mvine.mcomm.util.PreferenceHandler
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {

    @Singleton
    @Provides
    fun provideOkhttp(@ApplicationContext context:Context):OkHttpClient {
        val httpClient : OkHttpClient.Builder = OkHttpClient.Builder()
        val sharedPreferences =
            context.getSharedPreferences(MCOMM_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.getString(LOGIN_TOKEN, null)?.let { cookie ->
            httpClient.addInterceptor(
                Interceptor { chain: Interceptor.Chain ->
                    val request: Request = chain.request()
                    val newReq: Request = request
                        .newBuilder()
                        .addHeader("Cookie",cookie)
                        .addHeader("Sec-WebSocket-Protocol", "janus-protocol")
                        .build()
                    chain.proceed(newReq)
                })
                .build()
        }
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        return httpClient.build()
    }

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
            .addMessageAdapterFactory(GsonMessageAdapter.Factory())
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun provideWebSocketService(scarlet: Scarlet) : WebSocketService= scarlet.create(WebSocketService::class.java)

    @Singleton
    @Provides
    fun provideJanusManager(@ApplicationContext context: Context, preferenceHandler: PreferenceHandler) : JanusManager = JanusManager(context, preferenceHandler)
}