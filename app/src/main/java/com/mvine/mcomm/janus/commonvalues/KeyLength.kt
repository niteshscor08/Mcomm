package com.mvine.mcomm.janus.commonvalues

import androidx.annotation.IntDef

@IntDef(
    KeyLength.CREATE_KEY_LENGTH
)
@Retention(AnnotationRetention.SOURCE)
annotation class KeyLength {
    companion object{
        const val CREATE_KEY_LENGTH = 12
    }
}
