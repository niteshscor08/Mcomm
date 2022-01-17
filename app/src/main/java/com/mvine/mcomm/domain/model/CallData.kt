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


data class AllCalls(
    var callables: List<AllCallsClass>?=null,
    var companies: List<AllCallsCompanyData>?=null
)

data class AllCallsClass(
    val Company: String?,
    val Department: String?,
    val STX: String?,
    val admin: String?,
    val companyid: String?,
    val email:String?,
    val id: String?,
    val image_src: String?,
    val usename: String?,
    val username: String?
)
data class AllCallsCompanyData(
    val ID: String?,
    val Value: String?,
    val image_src: String
)