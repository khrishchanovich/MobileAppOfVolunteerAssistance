package com.example.volunteerassistance

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volunteerassistance.ui.loginForNeedHelp.EmailLoginActivity
import com.example.volunteerassistance.ui.loginForNeedHelp.LoginActivity
import com.example.volunteerassistance.ui.registrationForNeedHelp.NameActivity
import com.example.volunteerassistance.ui.registrationForNeedHelp.RegistrationActivity
import com.example.volunteerassistance.ui.theme.State
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme
import java.util.Locale

class PreRegistrationActivity : ComponentActivity() {
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.getDefault()
            } else {
                Toast.makeText(this, "Text-to-Speech не работает", Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            VolunteerAssistanceTheme {
                PreRegistrationScreen(
                    onEmailRegistrationClick = {
                        if (State.isHelp) {
                            val intent = Intent(this, NameActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this, RegistrationActivity::class.java)
                            startActivity(intent)
                        }
                    },
                    onEmailLoginClick = {
                        if (State.isHelp) {
                            val intent = Intent(this, EmailLoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    },
                    onSpeakClick = {
                        if (::textToSpeech.isInitialized) {
                            speakText("Вы можете выбрать зеленую кнопку для регистрации или выбрать синюю кнопку для входа, если вы уже зарегистрированы.")
                        }
                    }
//                    onPhoneRegistrationClick = {
//                        val intent = Intent(this, PhoneRegistrationActivity::class.java)
//                        startActivity(intent)
//                    }
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
fun PreRegistrationScreen(
    onEmailRegistrationClick: () -> Unit,
    onEmailLoginClick: () -> Unit,
    onSpeakClick: () -> Unit
) {
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
            if (State.isHelp) {
                Button(
                    onClick = { onEmailRegistrationClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF249B28)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Text(
                        "Регистрация при помощи почты",
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center)
                }

                Button(
                    onClick = { onEmailLoginClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Text(
                        "Вход при помощи почты",
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center)
                }
            } else {
                Button(
                    onClick = { onEmailRegistrationClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Text(
                        "Регистрация при помощи почты",
                        fontSize = 18.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center)
                }

                Button(
                    onClick = { onEmailLoginClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Text(
                        "Вход при помощи почты",
                        fontSize = 18.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center)
                }
            }

//
//            Button (
//                onClick = { onPhoneRegistrationClick() },
//                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(80.dp)
//            ) {
//                Text(
//                    "Регистрация при помощи номера телефона",
//                    fontSize = 18.sp,
//                    color = Color.White,
//                    textAlign = TextAlign.Center)
//            }
        }
    }
}

