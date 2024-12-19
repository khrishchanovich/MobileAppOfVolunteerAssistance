package com.example.volunteerassistance.ui.registrationForNeedHelp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
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
import com.example.volunteerassistance.MainActivity
import com.example.volunteerassistance.ProfileActivity
import com.example.volunteerassistance.R
import com.example.volunteerassistance.ui.theme.State
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class PasswordActivity : ComponentActivity() {
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

        val name = intent.getStringExtra("NAME") ?: ""
        val surname = intent.getStringExtra("SURNAME") ?: ""
        val email = intent.getStringExtra("EMAIL") ?: ""

        setContent {
            VolunteerAssistanceTheme {
                PasswordScreen(
                    name = name,
                    surname = surname,
                    email = email,

                    onSpeakClick = {
                        if (::textToSpeech.isInitialized) {
                            speakText("Введите ваш пароль на поле ниже. После нажмите на синюю кнопку для дальнейшей регистрации.")
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
    name: String,
    surname: String,
    email: String,
    onSpeakClick: () -> Unit,
) {
    val auth = Firebase.auth
    val db = FirebaseFirestore.getInstance()

    val nameState = remember { mutableStateOf(name) }
    val surnameState = remember { mutableStateOf(surname) }
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
                    if (nameState.value.isBlank() || surnameState.value.isBlank() || emailState.value.isBlank() || passwordState.value.isBlank()) {
                        Toast.makeText(context, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailState.value).matches()) {
                        Toast.makeText(context, "Введите корректный email", Toast.LENGTH_SHORT).show()
                    } else if (passwordState.value.length < 8) {
                        Toast.makeText(context, "Пароль должен содержать не менее 6 символов", Toast.LENGTH_SHORT).show()
                    } else {
                        signUp(auth, db, nameState.value, surnameState.value, emailState.value, passwordState.value, context)
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
