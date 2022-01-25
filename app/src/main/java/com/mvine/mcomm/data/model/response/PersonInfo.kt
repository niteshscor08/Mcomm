package com.mvine.mcomm.data.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonInfo(
    val view: View
) : Parcelable

@Parcelize
data class View(
    val id: Int,
    val usename: String?,
    val roleName: String?,
    val contact: Contact?,
    val roleId: String?,
    val nSTX: String?,
    val nastrpass: String?
) : Parcelable

@Parcelize
data class Contact(
    val name: String?,
    val firstName: String?,
    val surname: String?,
    val email: String?
) : Parcelable
