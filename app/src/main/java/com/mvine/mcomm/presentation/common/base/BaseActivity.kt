package com.mvine.mcomm.presentation.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.appcompat.app.AppCompatActivity


abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity(){

    var binding: T? = null

    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    private fun performDataBinding() {
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding!!.lifecycleOwner=this
        binding!!.executePendingBindings()
    }

}