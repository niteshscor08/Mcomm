package com.mvine.mcomm.data.model.response

data class ContactsResponse(
    val callables: List<Callable>?,
    val companies: List<Company>?,
    val status: String?,
    val total: Int?
)

data class Callable(
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
    val username: String?
)

data class Company(
    val ID: Int?,
    val Value: String?,
    val image_src: String?
)