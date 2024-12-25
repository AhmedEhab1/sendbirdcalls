package com.example.sendbirdcalls

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.sendbird.calls.AuthenticateParams
import com.sendbird.calls.DirectCall
import com.sendbird.calls.RoomInvitation
import com.sendbird.calls.SendBirdCall
import com.sendbird.calls.SendBirdCall.registerPushToken
import com.sendbird.calls.SendBirdException
import com.sendbird.calls.handler.DirectCallListener
import com.sendbird.calls.handler.SendBirdCallListener
import com.sendbird.calls.internal.PushTokenType
import java.util.UUID


class MainActivity : AppCompatActivity() {
    // TODO: get you access token from last fun
    val accessToken = "edRzB11MTZqqphunvk24Rb:APA91bET9k49o_JInIOwI7Ch8WQjdP3yvJhEPIGBPEofeFGvEitItFBhwM0ONB_qlasuQgdN6hskrfwuFKuLfLaiPPjg_xGty6ndUS6hPBdEmcOl8rU3e4I"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val initSdkButton = findViewById<Button>(R.id.init_sdk)
        val authenticateUser = findViewById<Button>(R.id.authenticate_user)
        val registerPushToken = findViewById<Button>(R.id.registerPushToken)
        val callListener = findViewById<Button>(R.id.callListener)

        initSdkButton.setOnClickListener {
            // Initialize SendBirdCall instance to use APIs in your app.
            val appId = "" // TODO: add app id
            SendBirdCall.init(applicationContext, appId)
        }


        authenticateUser.setOnClickListener {
            authenticateUser()
        }


        registerPushToken.setOnClickListener {
            registerPushToken()
        }

        callListener.setOnClickListener {
            callListener()
        }


    }

    private fun registerPushToken(){
        registerPushToken(accessToken, PushTokenType.FCM_VOIP, true) { exception ->
            if (exception != null) {
                Log.d("sendbird", "Register chat push token failed: error : ${exception.message} ")
            } else {
                Log.d("sendbird", "Register chat push token finished (status: Success) ")
            }
        }
    }


    private fun authenticateUser() {

        val authenticateParams = AuthenticateParams(userId = "1122").setAccessToken(accessToken)
        SendBirdCall.authenticate(authenticateParams) { _, e ->
            if (e == null) {
                Log.d("sendbird", "setCallsLoggedInUser: Success")
            } else {
                Log.d("sendbird", "setCallsLoggedInUser: $e")
            }
        }
    }

    private fun callListener() {
        val identifier = UUID.randomUUID().toString()

        SendBirdCall.addListener(identifier, object : SendBirdCallListener() {
            override fun onInvitationReceived(invitation: RoomInvitation) {
                Log.d("SendBirdCall", "onInvitationReceived: ")
            }

            override fun onRinging(call: DirectCall) {
                Log.d("SendBirdCall", "onRinging: ")
                call.setListener(object : DirectCallListener() {
                    override fun onEstablished(call: DirectCall) {
                        Log.d("SendBirdCall", "onEstablished: ")
                    }

                    override fun onConnected(call: DirectCall) {
                        Log.d("SendBirdCall", "onConnected: ")

                    }

                    override fun onEnded(call: DirectCall) {
                        Log.d("SendBirdCall", "onEnded: ")
                    }

                    override fun onRemoteAudioSettingsChanged(call: DirectCall) {
                        Log.d("SendBirdCall", "onRemoteAudioSettingsChanged: ")
                    }
                })

            }
        })
    }


    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Retrieve the token
                val token = task.result
                Log.d("FirebaseToken", "Token: $token")
            } else {
                Log.e("FirebaseToken", "Fetching FCM token failed", task.exception)
            }
        }
    }


}