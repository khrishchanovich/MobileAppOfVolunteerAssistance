package com.example.volunteerassistance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.volunteerassistance.ui.theme.State
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme

class PreRegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VolunteerAssistanceTheme {
                PreRegistrationScreen()
            }
        }
    }
}

@Composable
fun PreRegistrationScreen() {
    Text("Добро пожаловать на экран регистрации!")
}

@Preview(showBackground = true)
@Composable
fun PreRegistrationScreenPreview() {
    VolunteerAssistanceTheme {
        PreRegistrationScreen()
    }
}