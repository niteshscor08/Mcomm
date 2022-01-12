package com.mvine.mcomm.domain.model

data class CredentialData(
    var userName : String?= null,
    var password : String?= null,
    var token : String?= null,
    var isRefresh : Boolean? = false
)
