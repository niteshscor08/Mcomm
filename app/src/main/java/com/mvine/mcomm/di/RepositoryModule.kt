package com.mvine.mcomm.di

import com.mvine.mcomm.data.api.LoginApiService
import com.mvine.mcomm.data.repository.LoginRepoImpl
import com.mvine.mcomm.data.repository.dataSource.LoginRemoteRepo
import com.mvine.mcomm.data.repository.dataSourceImpl.LoginRemoteRepoImpl
import com.mvine.mcomm.domain.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideLoginRemoteRepository(loginApiService: LoginApiService): LoginRemoteRepo =
        LoginRemoteRepoImpl(loginApiService)

    @Singleton
    @Provides
    fun provideLoginRepository(loginRemoteRepo: LoginRemoteRepo): LoginRepository =
        LoginRepoImpl(loginRemoteRepo)

}