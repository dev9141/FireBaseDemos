package com.firebase.firebasedemos.firebase_auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.firebase.firebasedemos.R
import com.firebase.firebasedemos.databinding.ActivityFireBaseLoginBinding
import com.google.firebase.auth.FirebaseAuth

class FireBaseLoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityFireBaseLoginBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fire_base_login)

        auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null){
            Toast.makeText(this, "Already Login", Toast.LENGTH_SHORT).show()
            binding.btnLogin.text = "Logout"
        }
        else{
            binding.btnLogin.text = "Login"
        }

        binding.tvRedirectToRegister.setOnClickListener {
            startActivity(Intent(this, FireBaseRegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            if(binding.btnLogin.text.toString().equals("Login", true)){
                val email = binding.editTextEmail.text.toString()
                val password = binding.editTextPassword.text.toString()
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, DashboardActivity::class.java))
                    }
                    else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                auth.signOut()
                binding.btnLogin.text = "Login"
            }
        }
    }
}