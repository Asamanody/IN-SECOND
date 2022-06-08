package com.example.insecondapp.ui.home

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.example.insecondapp.R
import com.example.insecondapp.databinding.FragmentProfileBinding
import com.example.insecondapp.helper.Constants.Companion.supportPhone
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.help_dialog_layout.view.*
import kotlinx.android.synthetic.main.rate_dialog_layout.view.*

class ProfileFragment : Fragment() {
    val rate :String= "Rate Us"
    var rateValue :Int =1

    lateinit var profileBinding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       profileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileBinding.rateLayout.setOnClickListener { showRateDialog() }
        profileBinding.helpLayout.setOnClickListener { showHelpDialog() }
        profileBinding.helpTv1.setOnClickListener { makeCall() }

    }

    fun showRateDialog() {
       val rateDialog = BottomSheetDialog(context!!)
        val rateView = LayoutInflater.from(context).inflate(R.layout.rate_dialog_layout, null)
        rateDialog.setContentView(rateView)
        rateView.rateBtn.setOnClickListener {
            rateDialog.dismiss()
        rateValue = rateView.ratingBar.rating.toInt()
            Toast.makeText(context, "Thanks for rating us $rateValue", Toast.LENGTH_SHORT).show()
        }
        rateDialog.show()
    }
    fun showHelpDialog() {
        val helpDialog = BottomSheetDialog(context!!)
        val helpView = LayoutInflater.from(context).inflate(R.layout.help_dialog_layout, null)
        helpDialog.setContentView(helpView)
        helpView.button.setOnClickListener {
            helpDialog.dismiss()
            makeCall()
        }
        helpDialog.show()

    }

    // intent to make call
    fun makeCall() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel: $supportPhone")
        startActivity(intent)
    }



}