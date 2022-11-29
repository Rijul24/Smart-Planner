package com.example.smartplanner

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*

class Notification: BroadcastReceiver() {
    private lateinit var textToSpeech: TextToSpeech
    override fun onReceive(context: Context, intent: Intent) {
//        val intent = Intent(context, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val sharedPreferences = context.getSharedPreferences("smarty", Context.MODE_PRIVATE)
        Log.d("lol", System.currentTimeMillis().toString())
        val notification = NotificationCompat.Builder(context, "smarty")
            .setSmallIcon(R.drawable.alarm)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.alarm))
            .setContentTitle("REMINDER")
            .setContentText(intent.getStringExtra("title"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        with(NotificationManagerCompat.from(context)) {
            notify(1, notification)
            val serviceIntent = Intent(context, TextToSpeechService::class.java)
            serviceIntent.putExtra("text", "REMINDER, ${intent.getStringExtra("title")}")
            context.startService(serviceIntent)
            sharedPreferences.edit()
                .remove(intent.getStringExtra("date"))
                .apply()
        }
    }
}