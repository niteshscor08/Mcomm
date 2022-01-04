package com.mvine.mcomm.presentation.common.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference


abstract class BaseViewModel : ViewModel() {

    private val isLoading = ObservableBoolean(false)

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.set(isLoading)
    }

    fun getIsLoading(): Boolean {
        return isLoading.get()
    }
}