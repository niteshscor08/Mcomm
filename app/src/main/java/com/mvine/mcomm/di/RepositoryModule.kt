package com.mvine.mcomm.di

import com.mvine.mcomm.data.api.CallsApiService
import com.mvine.mcomm.data.api.ChangePasswordApiService
import com.mvine.mcomm.data.api.ChatsApiService
import com.mvine.mcomm.data.api.ContactsApiService
import com.mvine.mcomm.data.api.LoginApiService
import com.mvine.mcomm.data.api.LogoutApiService
import com.mvine.mcomm.data.mapper.AllCallsMapper
import com.mvine.mcomm.data.mapper.CallsMapper
import com.mvine.mcomm.data.mapper.ContactsMapper
import com.mvine.mcomm.data.repository.CallsRepoImpl
import com.mvine.mcomm.data.repository.ChangePasswordRepositoryImpl
import com.mvine.mcomm.data.repository.ChatsRepoImpl
import com.mvine.mcomm.data.repository.ContactsRepoImpl
import com.mvine.mcomm.data.repository.LoginRepoImpl
import com.mvine.mcomm.data.repository.LogoutRepositoryImpl
import com.mvine.mcomm.domain.repository.CallsRepository
import com.mvine.mcomm.domain.repository.ChangePasswordRepository
import com.mvine.mcomm.domain.repository.ChatsRepository
import com.mvine.mcomm.domain.repository.ContactsRepository
import com.mvine.mcomm.domain.repository.LoginRepository
import com.mvine.mcomm.domain.repository.LogoutRepository
import com.mvine.mcomm.util.PreferenceHandler
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
    fun provideLoginRepository(loginApiService: LoginApiService): LoginRepository =
        LoginRepoImpl(loginApiService)

    @Singleton
    @Provides
    fun provideCallsRepository(callsApiService: CallsApiService, callsMapper: CallsMapper, preferenceHandler: PreferenceHandler, allCallsMapper: AllCallsMapper): CallsRepository =
        CallsRepoImpl(callsApiService, callsMapper, preferenceHandler, allCallsMapper)

    @Singleton
    @Provides
    fun provideContactsRepository(contactsApiService: ContactsApiService, contactsMapper: ContactsMapper, prefrenceHandler: PreferenceHandler): ContactsRepository =
        ContactsRepoImpl(contactsApiService, contactsMapper, prefrenceHandler)

    @Singleton
    @Provides
    fun provideChatsRepository(chatsApiService: ChatsApiService): ChatsRepository =
        ChatsRepoImpl(chatsApiService)

    @Singleton
    @Provides
    fun provideChangePasswordRepositoryImpl(changePasswordApiService: ChangePasswordApiService, preferenceHandler: PreferenceHandler): ChangePasswordRepository =
        ChangePasswordRepositoryImpl(changePasswordApiService, preferenceHandler)

    @Singleton
    @Provides
    fun provideLogoutRepositoryImpl(logoutApiService: LogoutApiService): LogoutRepository =
        LogoutRepositoryImpl(logoutApiService)
}
