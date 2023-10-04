package com.firebase.firebasedemos.firebase_auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.firebase.firebasedemos.R
import com.firebase.firebasedemos.databinding.ActivityFacebookDashboardBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject


class DashboardFacebookAuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityFacebookDashboardBinding
    lateinit var auth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mGoogleSignInOptions: GoogleSignInOptions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_facebook_dashboard)

        val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()

        val request = GraphRequest.newMeRequest(
            accessToken
        ) { jsonObject, response ->
            val name = jsonObject!!.getString("name")
            binding.tvName.text = name
            val imageUrl = jsonObject!!.getJSONObject("picture").getJSONObject("data").getString("url")
            Glide.with(this)
                .setDefaultRequestOptions(
                    RequestOptions()
                        .placeholder(R.drawable.coverpic)
                        .error(R.drawable.coverpic)
                )
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivImageView)

        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,link,picture.type(large)")
        request.parameters = parameters
        request.executeAsync()

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