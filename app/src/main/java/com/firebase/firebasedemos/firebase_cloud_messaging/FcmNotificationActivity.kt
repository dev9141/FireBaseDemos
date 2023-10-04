package com.firebase.firebasedemos.firebase_cloud_messaging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.widget.Toast
import com.firebase.firebasedemos.R
import com.firebase.firebasedemos.databinding.ActivityFcmNotificationBinding
import com.google.firebase.messaging.FirebaseMessaging

class FcmNotificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityFcmNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFcmNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetTOken.setOnClickListener {
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener {
                    Log.e("GCMToken", "Activity Taken: $it")
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.printStackTrace()}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}