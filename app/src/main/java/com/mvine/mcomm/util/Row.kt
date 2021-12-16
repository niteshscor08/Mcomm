package com.mvine.mcomm.util

/**
 * A Sealed class that holds different Row Type possible Instances
 */
sealed class Row {

    class CallDataRowType(val dataRowType: Int) : Row()

    class CallSpinnerRowType(val spinnerRowType: Int) : Row()

    class ContactRowType(val contactRowType: Int) : Row()

    class InvalidRowType(val errorMessage: String) : Row()
}
