package com.example.volunteerassistance

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.volunteerassistance.ui.theme.State
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme


class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VolunteerAssistanceTheme {
                Screen()
            }
        }
    }
}

@Composable
fun Screen() {
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
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Text("Мне нужна помощь", fontSize = 24.sp, color = Color.White)
            }

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Text("Я хочу помочь", fontSize = 24.sp, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    VolunteerAssistanceTheme {
        Screen()
    }
}