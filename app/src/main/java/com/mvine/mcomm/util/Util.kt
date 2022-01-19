package com.mvine.mcomm.util

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.mvine.mcomm.R
import com.google.gson.Gson
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.domain.model.CredentialData
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


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

fun getDateFromTimestamp(timeStamp: String): String{
   return getTimeDifference(timeStamp, RECENT_CALL_RECEIVED_DATE_FORMAT, RECENT_CALL_OUTPUT_FORMAT)
}

fun getTimeDifference(receivedTimeStamp: String, receivedDateFormat: String, outputDateFormat: String ): String {
    var convertedTimeStamp: String = receivedTimeStamp
     try {
         val receivedDate = getSimpleDateFormat(receivedDateFormat).parse(receivedTimeStamp)
         val currentDate = Date()
         val timeDifference = currentDate.time - receivedDate.time
         val hourDifference = TimeUnit.MILLISECONDS.toHours(timeDifference)
         val dayDifference = TimeUnit.MILLISECONDS.toDays(timeDifference)
         val receivedDateTime = getSimpleDateFormat(TIME_FROM_DATE).format(receivedDate.time)
         convertedTimeStamp = when {
             hourDifference in ONE_L..HOUR_LIMIT -> {
                 TODAY.plus(AT).plus(receivedDateTime)
             }
             dayDifference == ONE_L -> {
                 YESTERDAY.plus(AT).plus(receivedDateTime)
             }
             dayDifference <= DAY_LIMIT -> {
                 getSimpleDateFormat(MONTH_FROM_DATE).format(receivedDate.time).plus(AT).plus(receivedDateTime)
             }
             else -> {
                 getSimpleDateFormat(outputDateFormat).format(receivedDate.time)
             }
         }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    return convertedTimeStamp
}

fun getSimpleDateFormat(dateFormat : String): SimpleDateFormat {
    return SimpleDateFormat(dateFormat,Locale.ENGLISH)
}

fun <T> sortDataAlphabetically(arrayList: ArrayList<T>, compare: (T, T) -> Int): ArrayList<T>{
        (arrayList as MutableList<T>).sortWith(Comparator { o1: T, o2: T ->
            compare(o1,o2)
        })
    return arrayList
}


