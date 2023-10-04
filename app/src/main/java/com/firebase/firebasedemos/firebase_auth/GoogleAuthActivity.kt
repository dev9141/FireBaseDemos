package com.firebase.firebasedemos.firebase_auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.firebase.firebasedemos.R
import com.firebase.firebasedemos.databinding.ActivityGoogleAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider

class GoogleAuthActivity : AppCompatActivity() {

    lateinit var binding: ActivityGoogleAuthBinding
    private lateinit var auth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mGoogleSignInOptions: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_google_auth)
        auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null){
            val loggedInUser = auth.currentUser
            Toast.makeText(this, "Name: ${loggedInUser!!.displayName} \n" +
                    "id: ${loggedInUser.uid} \n" +
                    "Profile Image: ${loggedInUser.photoUrl.toString()}", Toast.LENGTH_SHORT).show()

            //insert data in firebase data base if required
            startActivity(Intent(this, DashboardGoogleAuthActivity::class.java))
        }

        val clientIt= "881984452061-ac6dfbn51tmmglbforv65gts8qoftl0u.apps.googleusercontent.com"
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientIt)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)

        binding.btnGoogleLogin.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val intent = mGoogleSignInClient.signInIntent
        //startActivityForResult(intent, 102)
        googleSheetLauncher.launch(intent)
    }

    private val googleSheetLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val taskList = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            val googleAccount = taskList.getResult(ApiException::class.java)
            firebaseAuth(googleAccount.idToken)
        }
    }

    private fun firebaseAuth(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                val loggedInUser = auth.currentUser
                Toast.makeText(this, "Name: ${loggedInUser!!.displayName} \n" +
                        "id: ${loggedInUser.uid} \n" +
                        "Profile Image: ${loggedInUser.photoUrl.toString()}", Toast.LENGTH_SHORT).show()

                //insert data in firebase data base if required
                startActivity(Intent(this, DashboardGoogleAuthActivity::class.java))
            }
            else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "${it.printStackTrace()}", Toast.LENGTH_SHORT).show()
        }
    }
}