package com.mvine.mcomm.di

import com.mvine.mcomm.data.mapper.CallsMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MapperModule {

    @Provides
    @Singleton
    fun provideCallsMapper() = CallsMapper()
}