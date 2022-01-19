package com.mvine.mcomm.domain.model

data class ContactsData(
    val Company: String?,
    val Department: String?,
    val STX: String?,
    val admin: String?,
    val companyid: String?,
    val email: String?,
    val id: Int?,
    val image_src: String?,
    val surname: String?,
    val usename: String?,
    val username: String?,
    var isExpanded: Boolean = false
)