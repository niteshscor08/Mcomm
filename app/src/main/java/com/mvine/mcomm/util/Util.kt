package com.mvine.mcomm.util

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.mvine.mcomm.R
import com.google.gson.Gson
import com.mvine.mcomm.domain.model.CredentialData


fun showSnackBar(view: View, title : String, message : String?= null, isPositiveMessage : Boolean? = true) {
    var backGroundColor = R.color.mcomm_green
    if (isPositiveMessage == false)
        backGroundColor = R.color.mcomm_red
    val snackbar: Snackbar =
        Snackbar.make(view, "$title\n$message", Snackbar.LENGTH_SHORT)
    val snackbarView = snackbar.view
    snackbarView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    val textView = snackbarView.findViewById<View>(R.id.snackbar_text) as TextView
    textView.setTextAppearance(R.style.snack_bar_style)
    textView.minLines = 3
    if (!message.isNullOrEmpty()) {
        textView.maxLines = 3
    }
    snackbarView.setBackgroundColor(view.context.resources.getColor(backGroundColor))
    snackbar.show()
}

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

fun saveCredentials(preferenceHandler: PreferenceHandler, credentialData : CredentialData) {
    preferenceHandler.save(LOGIN_TOKEN, credentialData.token)
    preferenceHandler.save( CREDENTIAL_DATA, Gson().toJson(credentialData))
}

fun getCredentials(preferenceHandler: PreferenceHandler): CredentialData {
    preferenceHandler.getValue(CREDENTIAL_DATA)?.let {
        return   Gson().fromJson(it, CredentialData::class.java)
    }?:  return CredentialData()
}