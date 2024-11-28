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
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme

class PreRegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VolunteerAssistanceTheme {
                PreRegistrationScreen(
                    onEmailRegistrationClick = {
                        val intent = Intent(this, RegistrationActivity::class.java)
                        startActivity(intent)
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
fun PreRegistrationScreen( onEmailRegistrationClick: () -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

@Preview(showBackground = true)
@Composable
fun PreRegistrationScreenPreview() {
    VolunteerAssistanceTheme {
        PreRegistrationScreen(
            onEmailRegistrationClick = { }
        )
    }
}