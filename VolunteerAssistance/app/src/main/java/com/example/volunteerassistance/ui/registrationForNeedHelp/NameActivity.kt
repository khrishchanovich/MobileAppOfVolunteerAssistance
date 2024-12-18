package com.example.volunteerassistance.ui.registrationForNeedHelp

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import java.util.*

class NameActivity : ComponentActivity() {
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize TextToSpeech
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.getDefault()
            } else {
                Toast.makeText(this, "Text-to-Speech не работает", Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            VolunteerAssistanceTheme {
                NameScreen(
                    onNextClick = { name ->
                        val intent = Intent(this, EmailActivity::class.java).apply {
                            putExtra("NAME", name)
                        }
                        startActivity(intent)
                    },
                    onSpeakClick = {
                        speakText("Введите ваше имя на поле ниже")
                    }
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

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}

@Composable
fun NameScreen(
    onNextClick: (String) -> Unit,
    onSpeakClick: () -> Unit
) {
    val nameState = remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Red Circular Button
            Button(
                onClick = onSpeakClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            ) {
                Text("🔊", color = Color.White, fontSize = 24.sp, textAlign = TextAlign.Center)
            }

            Text(
                "Введите ваше имя:",
                fontSize = 32.sp,
                color = colorResource(id = R.color.black)
            )
            TextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text("Имя", fontSize = 24.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(80.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 24.sp)
            )
            Button(
                onClick = {
                    if (nameState.value.isBlank()) {
                        Toast.makeText(context, "Введите имя", Toast.LENGTH_SHORT).show()
                    } else {
                        onNextClick(nameState.value)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp)
            ) {
                Text(
                    "Далее",
                    fontSize = 20.sp
                )
            }
        }
    }
}
