package com.mvine.mcomm.presentation.login.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.mvine.mcomm.BR
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentLoginBinding
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.domain.util.Resource.Success
import com.mvine.mcomm.presentation.common.base.BaseFragment
import com.mvine.mcomm.presentation.home.HomeActivity
import com.mvine.mcomm.presentation.login.viewmodel.LoginViewModel
import com.mvine.mcomm.util.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding,LoginViewModel >() {

    @Inject
    lateinit var preferenceHandler: PreferenceHandler

    private val loginViewModel: LoginViewModel by viewModels()

    override val bindingVariable: Int
        get() = BR.loginViewModel

    override val layoutId: Int
        get() = R.layout.fragment_login

    override fun getViewModel(): LoginViewModel {
        return loginViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        initListeners()
        setUpData()
    }

    private fun subscribeObservers() {
        loginViewModel.cookieLiveData.observe(viewLifecycleOwner, { result ->
            result?.let { response ->
                when (response) {
                    is Success -> {
                        response.data?.let { token ->
                            preferenceHandler.save(LOGIN_TOKEN, token)
                            loginViewModel.getUserInfo(token)
                        } ?: showToastMessage(R.string.login_failure)
                    }
                    is Resource.Error -> {
                        showToastMessage(R.string.login_failure)
                    }
                    is Resource.Loading -> {
                        binding.loginProgress.visibility = View.VISIBLE
                    }
                }
            }
        })

        loginViewModel.userInfo.observe(viewLifecycleOwner, { result ->
            result?.let { response ->
                when(response){
                    is Success -> {
                        preferenceHandler.save(USER_INFO, Gson().toJson(response.data))
                        showToastMessage(R.string.login_successful)
                        val intent = Intent(activity, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    is Resource.Error -> {
                        showToastMessage(R.string.login_failure)
                    }
                    else -> {
                        showToastMessage(R.string.something_wrong)
                    }
                }

            }
        })
    }

    private fun setUpData() {
        if (BuildConfig.DEBUG) {
            binding.etEmail.setText("jordan+1@mvine.com", TextView.BufferType.EDITABLE)
            binding.etPassword.setText("Abcd1!", TextView.BufferType.EDITABLE)
        }
    }

    private fun initListeners() {

        binding.etEmail.apply {
            setOnClickListener {
                showKeyboard()
            }
        }

        binding.etPassword.apply {
            setOnClickListener {
                showKeyboard()
            }
        }

        binding.apply {
            btnLogin.setOnClickListener {
                binding.etPassword.hideKeyboard()
                binding.etEmail.hideKeyboard()
                val username = etEmail.text.toString()
                val password = etPassword.text.toString()
                if(username.isEmpty() || password.isEmpty() ){
                    showToastMessage(R.string.empty_credential)
                }else if(loginViewModel?.hideEmailErrorMsg?.value == false){
                    showToastMessage(R.string.invalid_email)
                } else if(loginViewModel?.hidePasswordErrorMsg?.value == false){
                    showToastMessage(R.string.invalid_password)
                }else{
                    loginViewModel?.login(username, password)
                }
            }
        }
    }

    private fun showToastMessage(msgId : Int){
        binding.loginProgress.visibility = View.GONE
        Toast.makeText(activity, resources.getString(msgId), Toast.LENGTH_LONG).show()
    }

}