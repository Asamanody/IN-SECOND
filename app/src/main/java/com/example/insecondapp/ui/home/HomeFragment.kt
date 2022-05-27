package com.example.insecondapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.insecondapp.R
import com.example.insecondapp.databinding.FragmentHomeBinding
import com.example.insecondapp.databinding.FragmentSpalshScreenBinding
import com.example.insecondapp.ui.home.adapters.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint


class HomeFragment : Fragment() {
    lateinit var mBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         initViewPager()



    }


    fun initImages() :List<Int>{
        val images = listOf(R.drawable.first,
            R.drawable.second,
            R.drawable.thered)
        return images
    }
    fun initViewPager(){
        val viewPagerAdapter= ViewPagerAdapter(initImages())
        val viewPager = mBinding.viewPager
        viewPager.adapter= viewPagerAdapter
        val indicator = mBinding.indicator
        indicator.setViewPager(viewPager)
    }
}