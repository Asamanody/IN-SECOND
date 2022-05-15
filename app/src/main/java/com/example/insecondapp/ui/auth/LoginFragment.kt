package com.example.insecondapp.ui.auth


import androidx.navigation.Navigation.findNavController

import android.view.animation.Animation
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle

import com.bumptech.glide.Glide



import android.view.View
import androidx.fragment.app.Fragment
import com.example.insecondapp.R
import com.example.insecondapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    var phoneAnim: Animation? = null
    var googleAnim: Animation? = null
    private var loginBinding: FragmentLoginBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return loginBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let { onViewStateRestored(it) }
        loadAnimation()
        loginBinding!!.btnPhoneAuth.setOnClickListener { moveToPhoneAuthPage() }
    }

    private fun moveToPhoneAuthPage() {
        val navController = findNavController(view!!)
        navController.navigate(R.id.action_loginFragment_to_phoneAuthFragment)
    }

    private fun loadAnimation() {
        Glide.with(this).load(R.drawable.motorcycle_animation).into(loginBinding!!.imageView2)
    }



}