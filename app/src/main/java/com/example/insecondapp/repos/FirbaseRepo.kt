package com.example.insecondapp.repos

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging


class FirbaseRepo {

    val mAuth = Firebase.auth
    val database = Firebase.database
    val firebaseMessaging = Firebase.messaging

    companion object {

        private lateinit var instance: FirbaseRepo

        val firbaseRepoInstance: FirbaseRepo
            get() {
                if (instance == null) {
                    instance = FirbaseRepo()
                }
                return instance
            }
    }


}