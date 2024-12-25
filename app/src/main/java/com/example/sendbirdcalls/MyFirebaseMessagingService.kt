package com.example.sendbirdcalls

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseToken", "New token: $token")
        // Send the token to your server or save it
    }
}