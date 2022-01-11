package com.mvine.mcomm.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mvine.mcomm.data.model.response.PersonInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceHandler @Inject constructor(@ApplicationContext val context: Context) {

    private val  sharedPreferences = context.getSharedPreferences(
        MCOMM_SHARED_PREFERENCES,
        Context.MODE_PRIVATE
    )

    fun save(key: String, value: String) {
        sharedPreferences.edit()?.let {
            it.putString(key, value)
            it.apply()
        }
    }

    fun getValue(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun get(key: String): PersonInfo {
       val value = sharedPreferences.getString(key, null)
       return Gson().fromJson(value, PersonInfo::class.java)
    }
}
