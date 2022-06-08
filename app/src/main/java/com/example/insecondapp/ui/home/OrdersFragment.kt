package com.example.insecondapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.example.insecondapp.R
import com.example.insecondapp.databinding.FragmentOrdersBinding

class OrdersFragment : Fragment() {

    lateinit var ordersBinding: FragmentOrdersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout with view binding
        ordersBinding = FragmentOrdersBinding.inflate(inflater, container, false)
        return ordersBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordersBinding.orderButton.setOnClickListener {

            val navController =findNavController(requireView())
            navController.navigate(R.id.action_ordersFragment_to_homeFragment)
        }
    }

}