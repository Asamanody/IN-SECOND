package com.example.insecondapp.di

import android.content.Context
import android.content.SharedPreferences
import com.example.insecondapp.databinding.FragmentProfileBinding
import com.example.insecondapp.helper.Constants.Companion.prefName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesharedPrefrence(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }


    @Singleton
    @Provides
    fun provideEditor(sharedPreferences: SharedPreferences) : SharedPreferences.Editor{
        return sharedPreferences.edit()

    }

    @Singleton
    @Provides
   fun provideFirbaseAuth(): FirebaseAuth {
        return Firebase.auth
    }
    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return Firebase.database
    }

    @Singleton
    @Provides
    fun provideFirbaseMessaging(): FirebaseMessaging {
       return Firebase.messaging
    }






}