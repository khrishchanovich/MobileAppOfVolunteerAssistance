package com.example.volunteerassistance

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

class PreRegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    }
//                    onPhoneRegistrationClick = {
//                        val intent = Intent(this, PhoneRegistrationActivity::class.java)
//                        startActivity(intent)
//                    }
                )
            }
        }
    }
}

@Composable
fun PreRegistrationScreen(
    onEmailRegistrationClick: () -> Unit,
    onEmailLoginClick: () -> Unit
) {
    Scaffold { padding ->
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
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
                    onClick = { onEmailRegistrationClick() },
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

