package com.mvine.mcomm.di

import android.content.Context
import com.mvine.mcomm.domain.model.CallState
import com.mvine.mcomm.janus.AudioFocusHandler
import com.mvine.mcomm.janus.JanusManager
import com.mvine.mcomm.janus.MediaPlayerHandler
import com.mvine.mcomm.util.PreferenceHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {

    @Singleton
    @Provides
    fun provideMediaPlayerHandler(@ApplicationContext context: Context) : MediaPlayerHandler = MediaPlayerHandler(context)

    @Singleton
    @Provides
    fun provideAudioFocusHandler(@ApplicationContext context: Context) : AudioFocusHandler = AudioFocusHandler(context)

    @Singleton
    @Provides
    fun provideCallState() : CallState= CallState()

    @Singleton
    @Provides
    fun provideJanusManager(@ApplicationContext context: Context,
                            preferenceHandler: PreferenceHandler,
                            mediaPlayerHandler: MediaPlayerHandler,
                            audioFocusHandler: AudioFocusHandler,
                            callState: CallState) : JanusManager =
        JanusManager(context,
            preferenceHandler,
            mediaPlayerHandler,
            callState,
            audioFocusHandler)
}