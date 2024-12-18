package com.example.volunteerassistance.ui.registrationForNeedHelp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volunteerassistance.R
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class PasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = intent.getStringExtra("NAME") ?: ""
        val surname = intent.getStringExtra("SURNAME") ?: ""
        val email = intent.getStringExtra("EMAIL") ?: ""
        setContent {
            VolunteerAssistanceTheme {
                PasswordScreen(name, surname, email) { password ->
                    signUp(
                        Firebase.auth,
                        FirebaseFirestore.getInstance(),
                        name,
                        surname,
                        email,
                        password,
                        this
                    )
                }
            }
        }
    }
}

@Composable
fun PasswordScreen(name: String, surname: String, email: String, onSignUpClick: (String) -> Unit) {
    val passwordState = remember { mutableStateOf("") }
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
//            Text("Имя: $name", fontSize = 18.sp)
//            Text("Фамилия: $surname", fontSize = 18.sp)
//            Text("Почта: $email", fontSize = 18.sp)
            Text(
                "Придумайте пароль:",
                fontSize = 32.sp,
                color = colorResource(id = R.color.black)
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
                    if (passwordState.value.isBlank()) {
                        Toast.makeText(context, "Введите пароль", Toast.LENGTH_SHORT).show()
                    } else {
                        onSignUpClick(passwordState.value)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp)
            ) {
                Text("Зарегистрироваться", fontSize = 20.sp)
            }
        }
    }
}
