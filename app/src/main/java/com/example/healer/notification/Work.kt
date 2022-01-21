package com.example.healer.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.healer.R

 open class Work(val context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters){

    override fun doWork(): Result {
        val message = inputData.getString("A")
        createNotification("Book an appointment Talk to someone & get help",message.toString())
       return Result.success()
    }

    private fun createNotification(title: String, description: String) {

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel =NotificationChannel("101","channel",NotificationManager.IMPORTANCE_DEFAULT)

        notificationManager.createNotificationChannel(notificationChannel)
        val notificationBuilder =NotificationCompat.Builder(applicationContext,"101")
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setSmallIcon(R.drawable.ic_launcher_foreground)

        notificationManager.notify(1,notificationBuilder.build())
    }
}