package com.example.insecondapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.insecondapp.R
import android.view.WindowManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // hide statue bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}