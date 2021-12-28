package com.mvine.mcomm.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    fun<T> get(key: String): T {
       val value = sharedPreferences.getString(key, null)
       return Gson().fromJson(value, object: TypeToken<T>() {}.type)
    }
}
