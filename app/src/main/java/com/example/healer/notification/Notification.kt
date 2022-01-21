package com.example.healer.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.healer.R
import com.example.healer.utils.Constants.channelID
import com.example.healer.utils.Constants.messageExtra
import com.example.healer.utils.Constants.notificationId
import com.example.healer.utils.Constants.titleExtra


class Notification :BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notification = NotificationCompat.Builder(context,channelID).setSmallIcon(R.drawable.call2)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentTitle(intent.getStringExtra(messageExtra))
            .build()

        val manger = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manger.notify(notificationId,notification)
    }
}