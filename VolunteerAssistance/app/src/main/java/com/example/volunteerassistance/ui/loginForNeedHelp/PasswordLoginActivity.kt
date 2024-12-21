package com.example.volunteerassistance.ui.loginForNeedHelp

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volunteerassistance.ProfileActivity
import com.example.volunteerassistance.R
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Locale

class PasswordLoginActivity : ComponentActivity() {
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

        val email = intent.getStringExtra("EMAIL") ?: ""

        setContent {
            VolunteerAssistanceTheme {
                PasswordScreen(
                    email = email,

                    onSpeakClick = {
                        if (::textToSpeech.isInitialized) {
                            speakText("Введите ваш пароль на поле ниже. После нажмите на синюю кнопку для дальнейшего входа.")
                        }
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
fun PasswordScreen(
    email: String,
    onSpeakClick: () -> Unit,
) {
    val auth = Firebase.auth

    val emailState = remember { mutableStateOf(email) }
    val passwordState = remember { mutableStateOf("") }
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
                "Введите пароль:",
                fontSize = 32.sp,
                color = colorResource(id = R.color.black),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            TextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Пароль", fontSize = 24.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(80.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                )
            )

            Button(
                onClick = {
                    val email = emailState.value.trim()
                    val password = passwordState.value.trim()

                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Вход выполнен", Toast.LENGTH_SHORT).show()

                                    val intent = Intent(context, ProfileActivity::class.java)
                                    context.startActivity(intent)
                                } else {
                                    Toast.makeText(context, "Ошибка входа: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Пожалуйста, заполните все поля для ввода", Toast.LENGTH_SHORT).show()
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
