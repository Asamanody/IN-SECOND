package com.example.insecondapp.ui.auth

import androidx.navigation.Navigation.findNavController
import com.example.insecondapp.storage.SeesionManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.example.insecondapp.R
import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.insecondapp.ui.home.HomeActivity
import com.example.insecondapp.databinding.FragmentSpalshScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpalshScreenFragment : Fragment() {
    lateinit var mBinding: FragmentSpalshScreenBinding
    var seesionManager: SeesionManager? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View? {
        // Inflate the layout for this fragment
        mBinding = FragmentSpalshScreenBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Restore instance state
        savedInstanceState?.let { onViewStateRestored(it) }
        seesionManager = SeesionManager(requireActivity())
        runAnimation()

        //moveToLoginPage();
        checkUserStatus()
    }

    //check if the user register or not from sharedPrefrence
    private fun checkUserStatus() {
        if (seesionManager!!.isLogin) {
            moveToHomepage()
        } else {
            moveToLoginPage()
        }
    }

    // animation in the spalsh screen
    private fun runAnimation() {
        val a = AnimationUtils.loadAnimation(requireActivity().applicationContext, R.anim.up_animation)
        mBinding!!.appNameTv.animation = a
    }

    private fun moveToLoginPage() {
        val handler = Handler()
        handler.postDelayed({ navigateToNextFrag() }, 3000)
    }

    private fun navigateToNextFrag() {
        val navController = findNavController(requireView())
        navController.navigate(R.id.action_spalshScreenFragment_to_loginFragment)
       // navController.navigate(R.id.action_spalshScreenFragment_to_phoneAuthFragment)
    }

    private fun moveToHomepage() {
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }, 3000)

    }
}