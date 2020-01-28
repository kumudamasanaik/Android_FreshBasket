package com.freshbasket.customer.firebase


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.freshbasket.customer.R
import com.freshbasket.customer.screens.home.HomeActivity
import com.freshbasket.customer.screens.notification.NotificationActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMessagingService"
    //private lateinit var notificationManager: NotificationManager
    private val CHHANNEL_ID = "Keep cart"
    private val ADMIN_CHANNEL_ID = "dukan"
    private lateinit var notificationManager: NotificationManager

    private var remoteFireBaseMessage: RemoteMessage? = null

    override fun onNewToken(token: String?) {
        super.onNewToken(token)

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        remoteMessage?.let { message ->

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val intent = Intent(this, NotificationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT)
            //Setting up Notification channels for android O and above
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setupNotificationChannels()
            }
            val notificationId = Random().nextInt(60000)

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, CHHANNEL_ID)
                    .setSmallIcon(R.drawable.app_logo)  //a resource for your custom small icon
                    .setContentTitle(message.data["title"]) //the "title" value you sent in your notification
                    .setContentText(message.data["message"]) //ditto
                    .setAutoCancel(true)  //dismisses the notification on click
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)

            notificationManager.notify(notificationId, notificationBuilder.build())

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupNotificationChannels() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val adminChannelName = getString(R.string.app_name)
        //val adminChannelDescription = getString(R.string.notifications_admin_channel_description)
        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(CHHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_DEFAULT)
        adminChannel.description = "keep kart"
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager.createNotificationChannel(adminChannel)
    }
}
