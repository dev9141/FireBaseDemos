package com.firebase.firebasedemos.firebase_auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.firebase.firebasedemos.R
import com.firebase.firebasedemos.databinding.ActivityFireBaseAuthBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FireBaseRegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityFireBaseAuthBinding
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fire_base_auth)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        if(auth.currentUser != null){
            Toast.makeText(this, "Already Registered", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, FireBaseLoginActivity::class.java))
        }

        binding.tvRedirectToLogin.setOnClickListener {
            startActivity(Intent(this, FireBaseLoginActivity::class.java))
        }

        binding.btnSignUp.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val mobileNum = binding.editTextMobile.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if(name.isNotEmpty() && mobileNum.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if(it.isSuccessful){
                            val reference = database.reference.child("userData").child(auth.currentUser!!.uid)
                            Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()
                            reference.setValue(usersModel(name, mobileNum, email, password)).addOnCompleteListener {
                                if(it.isSuccessful){
                                    Toast.makeText(this, "Data Inserted Successful", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, FireBaseLoginActivity::class.java))
                                }
                            }
                        }
                    }
                }
        }
    }
}