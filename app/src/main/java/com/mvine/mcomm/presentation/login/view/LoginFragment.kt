package com.mvine.mcomm.presentation.login.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.mvine.mcomm.BR
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
        loadUserInformation()
    }

    private fun subscribeObservers() {
        loginViewModel.cookieLiveData.observe(viewLifecycleOwner, { result ->
            result?.let { response ->
                when (response) {
                    is Success -> {
                        response.data?.let { token ->
                            loginViewModel.updateTokenAndLogin(token, binding.etEmail.text.toString(), binding.etPassword.text.toString())
                        } ?: showToastMessage(R.string.login_failed, R.string.incorrect_email_pass)
                    }
                    is Resource.Error -> {
                        preferenceHandler.clearData()
                        showToastMessage(R.string.login_failure, null)
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
                        val intent = Intent(activity, HomeActivity::class.java)
                        intent.putExtra(LOGGED_IN, true)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is Resource.Error -> {
                        preferenceHandler.clearData()
                        showToastMessage(R.string.login_failure)
                    }
                    else -> {
                        preferenceHandler.clearData()
                        showToastMessage(R.string.login_failure)
                    }
                }

            }
        })

        loginViewModel.loginError.observe(viewLifecycleOwner, {
            if(it) {
                preferenceHandler.clearData()
                showToastMessage(R.string.login_failure)
            }
        })
    }

    private fun loadUserInformation() {
        val credentialData = getCredentials(preferenceHandler)
            val username = credentialData.userName
            val password = credentialData.password
            username?.let {
                binding.etEmail.setText(it, TextView.BufferType.EDITABLE)
            }
            password?.let {
                binding.etPassword.setText(it, TextView.BufferType.EDITABLE)
            }
            credentialData.token?.let {
                username?.let { userName -> password?.let { password ->
                    loginViewModel?.login(userName,
                        password
                    )
                } }
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

    private fun showToastMessage(title : Int, message : Int?= null, isPositiveMessage : Boolean? = false){
        binding.loginProgress.visibility = View.GONE
        showSnackBar(binding.root,resources.getString(title),
            message?.let { resources.getString(it) }, isPositiveMessage )
    }

}