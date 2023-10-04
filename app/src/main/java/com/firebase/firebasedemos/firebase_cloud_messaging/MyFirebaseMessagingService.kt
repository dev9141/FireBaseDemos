package com.firebase.firebasedemos.firebase_cloud_messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.firebase.firebasedemos.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.RemoteMessage.Notification

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.e("GCMToken", "token : $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("GCMToken", "data : ${message.data}")
        Log.e("GCMToken", "raw data : ${message.rawData}")
        Log.e("GCMToken", "notification Title : ${message.notification!!.title}")
        Log.e("GCMToken", "notification body (content) : ${message.notification!!.body}")

        showNotification(this, message.notification!!.title!!, message.notification!!.body!!)
    }

    private fun showNotification(context: Context, title:String, body:String){
        val notificationBuilder = NotificationCompat.Builder(context, "com.fcm.demo")
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText(title)
                .setBigContentTitle(title)
                .setSummaryText(title)
            )

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelID = "com.fcm.demo"
            val notificationChannel = NotificationChannel(channelID, "FCM Demo", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
            notificationBuilder.setChannelId(channelID)
        }

        notificationManager.notify(102, notificationBuilder.build())
    }
}