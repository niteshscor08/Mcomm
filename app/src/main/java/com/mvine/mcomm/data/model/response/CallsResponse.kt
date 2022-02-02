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

data class CallablesResponse(
    val callables: List<CallableClass>?,
    val companies: List<CompanyData>?,
    val status: String?,
    val total: Int?
)

data class CallableClass(
    val Company: String?,
    val Department: String?,
    val STX: String?,
    val admin: String?,
    val companyid: String?,
    val email: String?,
    val id: String?,
    val image_src: String?,
    val surname: String?,
    val usename: String?,
    val username: String?
)
data class CompanyData(
    val ID: String?,
    val Value: String?,
    val image_src: String
)
