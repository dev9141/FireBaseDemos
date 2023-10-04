package com.firebase.firebasedemos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.firebase.firebasedemos.databinding.ActivityFireBaseOptionsBinding
import com.firebase.firebasedemos.firebase_auth.FacebookAuthActivity
import com.firebase.firebasedemos.firebase_auth.FireBaseLoginActivity
import com.firebase.firebasedemos.firebase_auth.GoogleAuthActivity
import com.firebase.firebasedemos.firebase_cloud_firestore.NotesListStoreActivity
import com.firebase.firebasedemos.firebase_cloud_messaging.FcmNotificationActivity
import com.firebase.firebasedemos.firebases_realtime_database.NotesListActivity

class FireBaseOptionsActivity : AppCompatActivity() {
    lateinit var binding: ActivityFireBaseOptionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fire_base_options)

        binding.btnFBAuth.setOnClickListener {
            startActivity(Intent(this, FireBaseLoginActivity::class.java))
        }

        binding.btnFBAuthWithGoogle.setOnClickListener {
            startActivity(Intent(this, GoogleAuthActivity::class.java))
        }

        binding.btnFBAuthWithFacebook.setOnClickListener {
            startActivity(Intent(this, FacebookAuthActivity::class.java))
        }

        binding.btnFBRealTimeDataBase.setOnClickListener {
            startActivity(Intent(this, NotesListActivity::class.java))
        }

        binding.btnFBFireStoreDataBase.setOnClickListener {
            startActivity(Intent(this, NotesListStoreActivity::class.java))
        }

        binding.btnFCM.setOnClickListener {
            startActivity(Intent(this, FcmNotificationActivity::class.java))
        }
    }
}