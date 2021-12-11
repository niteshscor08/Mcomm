package com.mvine.mcomm.di

import com.mvine.mcomm.data.api.CallsApiService
import com.mvine.mcomm.data.api.ChatsApiService
import com.mvine.mcomm.data.api.ContactsApiService
import com.mvine.mcomm.data.api.LoginApiService
import com.mvine.mcomm.data.mapper.CallsMapper
import com.mvine.mcomm.data.repository.CallsRepoImpl
import com.mvine.mcomm.data.repository.ChatsRepoImpl
import com.mvine.mcomm.data.repository.ContactsRepoImpl
import com.mvine.mcomm.data.repository.LoginRepoImpl
import com.mvine.mcomm.data.repository.dataSource.CallsRemoteRepo
import com.mvine.mcomm.data.repository.dataSource.ChatsRemoteRepo
import com.mvine.mcomm.data.repository.dataSource.ContactsRemoteRepo
import com.mvine.mcomm.data.repository.dataSource.LoginRemoteRepo
import com.mvine.mcomm.data.repository.dataSourceImpl.CallsRemoteRepoImpl
import com.mvine.mcomm.data.repository.dataSourceImpl.ChatsRemoteRepoImpl
import com.mvine.mcomm.data.repository.dataSourceImpl.ContactsRemoteRepoImpl
import com.mvine.mcomm.data.repository.dataSourceImpl.LoginRemoteRepoImpl
import com.mvine.mcomm.domain.repository.CallsRepository
import com.mvine.mcomm.domain.repository.ChatsRepository
import com.mvine.mcomm.domain.repository.ContactsRepository
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
    fun provideCallsRemoteRepository(callsApiService: CallsApiService): CallsRemoteRepo =
        CallsRemoteRepoImpl(callsApiService)

    @Singleton
    @Provides
    fun provideContactsRemoteRepository(contactsApiService: ContactsApiService): ContactsRemoteRepo =
        ContactsRemoteRepoImpl(contactsApiService)

    @Singleton
    @Provides
    fun provideChatsRemoteRepository(chatsApiService: ChatsApiService): ChatsRemoteRepo =
        ChatsRemoteRepoImpl(chatsApiService)

    @Singleton
    @Provides
    fun provideLoginRepository(loginRemoteRepo: LoginRemoteRepo): LoginRepository =
        LoginRepoImpl(loginRemoteRepo)

    @Singleton
    @Provides
    fun provideCallsRepository(callsRemoteRepo: CallsRemoteRepo, callsMapper: CallsMapper): CallsRepository =
        CallsRepoImpl(callsRemoteRepo, callsMapper)

    @Singleton
    @Provides
    fun provideContactsRepository(contactsRemoteRepo: ContactsRemoteRepo): ContactsRepository =
        ContactsRepoImpl(contactsRemoteRepo)

    @Singleton
    @Provides
    fun provideChatsRepository(chatsRemoteRepo: ChatsRemoteRepo): ChatsRepository =
        ChatsRepoImpl(chatsRemoteRepo)

}