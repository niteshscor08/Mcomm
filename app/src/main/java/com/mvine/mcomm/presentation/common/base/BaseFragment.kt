package com.mvine.mcomm.presentation.common.base

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.mvine.mcomm.janus.JanusManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {

   private lateinit var rootView: View

   protected lateinit var binding: T

   private lateinit var viewModel: V

    /**
     * @desc Override for set binding variable
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * @desc Override for set view model
     * @return view model instance
     */
    abstract fun getViewModel(): V


    /**
     *@desc  called to do initial creation of the fragment.
     **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
    }

    /**
     *@desc used to create and returns the view hierarchy associated with the fragment.
     **/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        rootView = binding.root
        return rootView
    }


    /**
     * @desc Called immediately after onCreateView has returned,
     * but before any saved state has been restored in to the view.
     **/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(bindingVariable, viewModel)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

}