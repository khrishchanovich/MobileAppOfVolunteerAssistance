package com.example.volunteerassistance

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import android.window.SurfaceSyncGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volunteerassistance.ui.composable.BottomBar
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Locale

data class UserProfile(
    val name: String = "",
    val surname: String = "",
    val is_help: Boolean = false
)

class ProfileActivity : ComponentActivity() {

    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.getDefault()
            } else {
//                Toast.makeText(this, "Text-to-Speech не работает", Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            VolunteerAssistanceTheme {
                Scaffold(
                    bottomBar = {
                        BottomBar(
                            selectedTab = "profile",
                            onMainClick = {
                                startActivity(Intent(this, MainActivity::class.java))
                            },
                            onProfileClick = { }
                        )
                    }
                ) { padding ->
                    Surface(
                        modifier = Modifier.padding(padding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ProfileScreen(
                            speakText = { text ->
                                speakText(text)
                            }
                        )
                    }
                }
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
fun ProfileScreen(speakText: (String) -> Unit) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (currentUser != null) {
            try {
                val document = firestore.collection("users").document(currentUser.uid).get().await()

                val user = document.data?.let { data ->
                    UserProfile(
                        name = data["name"] as? String ?: "",
                        surname = data["surname"] as? String ?: "",
                        is_help = data["is_help"] as? Boolean ?: false
                    )
                }

                //Toast.makeText(context, "${document.data}", Toast.LENGTH_SHORT).show()

                if (user != null) {
                    userProfile = user
                    println("${document.data}")
                } else {
                    println("User not found in Firestore")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        } else {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        userProfile?.let {
            ProfileContent(user = it, onSpeakClick = {
                speakText("Нажмите на синюю кнопку для выхода из аккаунта")
            })
        } ?: run {
            Text(
                text = "User profile not found",
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun ProfileContent(
    user: UserProfile,
    onSpeakClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold { padding ->
        if (user.is_help) {
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
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
//        Image(
//            painter = painterResource(id = R.drawable.ic_launcher_foreground),
//            contentDescription = "Фото профиля",
//            modifier = Modifier
//                .size(100.dp)
//                .clip(CircleShape)
//        )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${user.name} ${user.surname}",
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!user.is_help) {
                Text(
                    text = "Вы волонтер!",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

//        Button(onClick = {}) {
//            Text(text = "Редактировать")
//        }

            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut() // Выход из Firebase

                    // Создаем Intent с флагами для очистки стека активности
                    val intent = Intent(context, StartActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp)
            ) {
                Text("Выход", fontSize = 20.sp)
            }

        }



    }
}

