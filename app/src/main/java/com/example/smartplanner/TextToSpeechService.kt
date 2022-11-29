package com.example.smartplanner

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener
import android.util.Log
import java.util.*

class TextToSpeechService: Service(), TextToSpeech.OnInitListener, OnUtteranceCompletedListener {

    private lateinit var textToSpeech: TextToSpeech
    private var text: String? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("loll", "heheheheh")
        textToSpeech = TextToSpeech(this, this)
        text = intent.getStringExtra("text")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.UK)
            if(result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            }
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith("stopSelf()"))
    override fun onUtteranceCompleted(uttId: String?) {
        stopSelf()
    }

    override fun onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}