package com.mvine.mcomm.util

import com.mvine.mcomm.util.Row.CallDataRowType
import com.mvine.mcomm.util.Row.ContactRowType
import com.mvine.mcomm.util.Row.InvalidRowType
import com.mvine.mcomm.util.Row.CallSpinnerRowType

/**
 * A Util function to return a [Row] Instance
 *
 * @param rowType The position for which the Row Instance has to be returned
 * @return a [Row] instance based on the [rowType] provided
 */

fun getRowTypeInstance(rowType: Int): Row {
    return when (rowType) {
        CALL_DATA_ROW_TYPE -> CallDataRowType(CALL_DATA_ROW_TYPE)
        CALL_HISTORY_ROW_TYPE -> CallSpinnerRowType(CALL_HISTORY_ROW_TYPE)
        CONTACT_ROW_TYPE -> ContactRowType(CONTACT_ROW_TYPE)
        else -> InvalidRowType(errorMessage = INVALID_ROW_ERROR)
    }
}

const val CALL_DATA_ROW_TYPE = 0
const val CALL_HISTORY_ROW_TYPE = 1
const val CONTACT_ROW_TYPE = 2
const val INVALID_ROW_ERROR = "Invalid Row Type Found"
