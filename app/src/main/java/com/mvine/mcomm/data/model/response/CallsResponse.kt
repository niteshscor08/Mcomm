package com.mvine.mcomm.data.model.response

data class CallsResponse(
    val recents: List<Recent>?,
    val status: String?,
    val stx: String?,
    val total: Int?
)

data class Recent(
    val dialstatus: String?,
    val image_src: String?,
    val othercaller_company: String?,
    val othercaller_company_id: String?,
    val othercaller_department: String?,
    val othercaller_stx: String?,
    val timestamp: String?,
    val type: String?
)