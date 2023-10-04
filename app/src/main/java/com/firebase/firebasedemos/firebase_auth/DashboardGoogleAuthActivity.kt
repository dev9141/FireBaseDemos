package com.firebase.firebasedemos.firebase_auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.firebase.firebasedemos.R
import com.firebase.firebasedemos.databinding.ActivityDashboardBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DashboardGoogleAuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding
    lateinit var auth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mGoogleSignInOptions: GoogleSignInOptions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()

        val clientIt= "881984452061-ac6dfbn51tmmglbforv65gts8qoftl0u.apps.googleusercontent.com"
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientIt)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)

        binding.btnLogout.setOnClickListener {
            mGoogleSignInClient.signOut()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        finish()
                    }
                }
            auth.signOut()
            auth.removeAuthStateListener {
                it.signOut()
            }
            finish()
        }
    }
}