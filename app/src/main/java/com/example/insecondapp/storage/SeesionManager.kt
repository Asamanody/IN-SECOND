package com.example.insecondapp.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.insecondapp.models.User
import com.example.insecondapp.storage.SeesionManager
import java.util.HashMap

class SeesionManager(var context: Context) {

    val sharedPreferences: SharedPreferences
    val editor: SharedPreferences.Editor

    companion object {
        const val prefName = "LOGIN"
        const val login = "IS_LOGIN"
        const val deviceToken = "device_token"
        const val userPhone = "USER_PHONE"
        const val userId = "USER_ID"
        const val userName = "USER_NAME"
        const val userCreated = "USER_CREATED"
       // const val userType = "USER_TYPE"
    }

    init {
        sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun createSeesion(
      user :User
    ) {
        editor.putBoolean(login, true)
        editor.putString(deviceToken, user.device_token)
        editor.putString(userPhone, user.phone_number)
        editor.putString(userId, user.id)
        editor.putString(userName, user.name)
        editor.putString(userCreated, user.user_created)
        editor.apply()
    }

    val userDetails: HashMap<String, String?>
        get() {
            val user = HashMap<String, String?>()
            user.put(deviceToken, sharedPreferences.getString(deviceToken, null));
            user.put(userPhone, sharedPreferences.getString(userPhone, null));
            user.put(userId, sharedPreferences.getString(userId, null));
            user.put(userName, sharedPreferences.getString(userName, null));
            user.put(userCreated, sharedPreferences.getString(userCreated, null));
           // user.put(userType, sharedPreferences.getString(userType, null));

            return user
        }

    val isLogin: Boolean
        get() = if (sharedPreferences.getBoolean(login, true)) {
            true
        } else {
            false
        }

    fun logoutUserFromSeesion() {
        editor.clear()
        editor.commit()
    }


}