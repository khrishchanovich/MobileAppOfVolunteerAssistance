package com.example.volunteerassistance.ui.registrationForNeedHelp

import androidx.activity.compose.setContent
import com.example.volunteerassistance.LoginScreen
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volunteerassistance.R
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme
import java.util.Locale

class SurnameActivity : ComponentActivity() {
    private lateinit var textToSpeech: TextToSpeech

    private val spokenSurname = mutableStateOf("")

    private val speechRecognizerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        val spokenText = matches?.getOrNull(0) ?: ""
        spokenSurname.value = spokenText
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.getDefault()
            } else {
                Toast.makeText(this, "Text-to-Speech не работает", Toast.LENGTH_SHORT).show()
            }
        }

        val name = intent.getStringExtra("NAME") ?: ""

        setContent {
            VolunteerAssistanceTheme {
                SurnameScreen(
                    name = name,
                    surnameState = spokenSurname,
                    onNextClick = { surname ->
                        val intent = Intent(this, EmailActivity::class.java).apply {
                            putExtra("NAME", name)
                            putExtra("SURNAME", surname)
                        }
                        startActivity(intent)
                    },
                    onSpeakClick = {
                        speakText("Введите вашу фамилию на поле ниже или используйте зелёную голосовую кнопку. После нажмите на синюю кнопку для дальнейшей регистрации.")
                    },
                    onVoiceInputClick = { startVoiceInput() }
                )
            }
        }
    }

    private fun speakText(text: String) {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Скажите вашу фамилию")
        }
        speechRecognizerLauncher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}

@Composable
fun SurnameScreen(
    name: String,
    surnameState: MutableState<String>,
    onNextClick: (String) -> Unit,
    onSpeakClick: () -> Unit,
    onVoiceInputClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onSpeakClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            ) {
                Text("🔊", color = Color.White, fontSize = 24.sp, textAlign = TextAlign.Center)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Введите фамилию:",
                fontSize = 32.sp,
                color = colorResource(id = R.color.black),
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            TextField(
                value = surnameState.value,
                onValueChange = { surnameState.value = it },
                label = { Text("Фамилия", fontSize = 24.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(80.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 24.sp)
            )

            // Кнопка для голосового ввода
            Button(
                onClick = onVoiceInputClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(62.dp)
            ) {
                Text("🎤", fontSize = 20.sp, color = Color.White)
            }

            // Кнопка "Далее"
            Button(
                onClick = {
                    if (surnameState.value.isBlank()) {
                        Toast.makeText(context, "Введите фамилию", Toast.LENGTH_SHORT).show()
                    } else {
                        onNextClick(surnameState.value)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp)
            ) {
                Text("Далее", fontSize = 20.sp)
            }
        }
    }
}
