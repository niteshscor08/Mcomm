package com.mvine.mcomm.util

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.mvine.mcomm.R


fun showSnackBar(view: View, title : String, message : String?= null, isPositiveMessage : Boolean? = true){
    var backGroundColor = R.color.mcomm_green
    if(isPositiveMessage == false)
        backGroundColor = R.color.mcomm_red
    val snackbar: Snackbar =
        Snackbar.make(view, "$title\n$message", Snackbar.LENGTH_SHORT)
    val snackbarView = snackbar.view
    snackbarView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    val textView = snackbarView.findViewById<View>(R.id.snackbar_text) as TextView
    textView.setTextAppearance(R.style.snack_bar_style)
    textView.minLines = 3
    if(!message.isNullOrEmpty()){
        textView.maxLines = 3
    }
    snackbarView.setBackgroundColor(view.context.resources.getColor(backGroundColor))
    snackbar.show()
}