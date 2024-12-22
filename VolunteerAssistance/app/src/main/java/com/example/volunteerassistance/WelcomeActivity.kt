package com.example.volunteerassistance

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme
import java.util.Locale

class WelcomeActivity : ComponentActivity() {

    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale("ru", "RU")

                val utteranceId = "WelcomeMessageId" // Уникальный ID
                textToSpeech.speak(
                    "Здравствуйте! Вы находитесь в приложении 'Помоги мне!'. Нажмите на красную кнопку, если Вам необходима помощь." +
                            "Далее на каждой странице будет находиться красный элемент - при нажатии на него Вы будете получать подсказку. Удачи!",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    utteranceId
                )

                textToSpeech.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        // Действия при старте речи (если нужно)
                    }

                    override fun onDone(utteranceId: String?) {
                        if (utteranceId == "WelcomeMessageId") { // Проверяем ID
                            val intent = Intent(this@WelcomeActivity, StartActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onError(utteranceId: String?) {
                    }
                })
            }
        }


        setContent {
            VolunteerAssistanceTheme {
                AudioScreen()
            }
        }
    }

    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}

@Composable
fun AudioScreen() {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔊",
                fontSize = 100.sp,
                color = Color.Black
            )
        }
    }
}

