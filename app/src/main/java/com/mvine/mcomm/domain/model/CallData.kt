package com.mvine.mcomm.domain.model

data class CallData(
    val dialstatus: String?,
    val image_src: String?,
    val othercaller_company: String?,
    val othercaller_company_id: String?,
    val othercaller_department: String?,
    val othercaller_stx: String?,
    val timestamp: String?,
    val type: String?,
    val callHistory: ArrayList<SpinnerItem>?,
    var isExpanded: Boolean = false
)

data class SpinnerItem(
    val callImageSrc: Int,
    val itemText: String
)