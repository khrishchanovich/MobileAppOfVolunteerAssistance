package com.example.volunteerassistance.ui.registrationForNeedHelp

import androidx.activity.compose.setContent
import com.example.volunteerassistance.LoginScreen
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme
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
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volunteerassistance.R
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme



class SurnameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = intent.getStringExtra("NAME") ?: ""
        setContent {
            VolunteerAssistanceTheme {
                SurnameScreen(name) { surname ->
                    val intent = Intent(this, EmailActivity::class.java).apply {
                        putExtra("NAME", name)
                        putExtra("SURNAME", surname)
                    }
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun SurnameScreen(name: String, onNextClick: (String) -> Unit) {
    val surnameState = remember { mutableStateOf("") }
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
//            Text("Имя: $name", fontSize = 24.sp)
            Text(
                "Введите вашу фамилию:",
                fontSize = 32.sp,
                color = colorResource(id = R.color.black)
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
