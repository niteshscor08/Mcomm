package com.mvine.mcomm.util

import com.google.gson.Gson
import com.mvine.mcomm.domain.model.CredentialData

fun extractEpochTime(token : String): Long {
    var epochTime = EMPTY_STRING
    token.forEach {
        epochTime += when {
            it.isDigit() -> {
                it
            }
            else -> {
                return epochTime.toLong()
            }
        }
    }
    return epochTime.toLong()
}

fun getSubStringBasedOnIndex(input: String, index : Int): String {
    return input.substring(0,index)
}

fun saveCredentials(preferenceHandler: PreferenceHandler,
                                token : String,
                                username: String,
                                password: String,
                                isRefresh : Boolean = false) {
    val credentialData = CredentialData(
        userName = username,
        password = password,
        token = token,
        isRefresh = isRefresh
    )
    preferenceHandler.save(LOGIN_TOKEN, token)
    preferenceHandler.save( CREDENTIAL_DATA, Gson().toJson(credentialData))
}

fun getCredentials(preferenceHandler: PreferenceHandler): CredentialData {
    preferenceHandler.getValue(CREDENTIAL_DATA)?.let {
        return   Gson().fromJson(it, CredentialData::class.java)
    }?:  return CredentialData()
}