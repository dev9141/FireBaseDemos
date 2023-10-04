package com.firebase.firebasedemos.firebase_auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.firebase.firebasedemos.databinding.ActivityFacebookAuthBinding


class FacebookAuthActivity : AppCompatActivity() {

    lateinit var binding:ActivityFacebookAuthBinding
    private lateinit var callbackManager: CallbackManager

    companion object {
        private const val EMAIL = "email"
        private const val USER_POSTS = "user_posts"
        private const val AUTH_TYPE = "rerequest"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacebookAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callbackManager = CallbackManager.Factory.create()

        binding.loginButton.permissions = listOf(EMAIL, USER_POSTS)
        binding.loginButton.authType = AUTH_TYPE

        binding.loginButton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onCancel() {
                    setResult(RESULT_CANCELED)
                    finish()
                }

                override fun onError(error: FacebookException) {
                    // Handle exception
                }
            })

        binding.btnFacebookLogin.setOnClickListener {

            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onCancel() {
                        Toast.makeText(this@FacebookAuthActivity, "onCancel", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(error: FacebookException) {
                        Toast.makeText(this@FacebookAuthActivity, "${error.printStackTrace()}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(result: LoginResult) {
                        startActivity(Intent(this@FacebookAuthActivity, DashboardGoogleAuthActivity::class.java))
                    }
                })
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

}