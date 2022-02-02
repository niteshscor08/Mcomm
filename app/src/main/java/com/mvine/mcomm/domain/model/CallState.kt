package com.mvine.mcomm.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CallState(
    var remoteUsername: String? = null,
    var remoteDisplayName: String? = null,
    var remoteUrl: String? = null
) : Parcelable
