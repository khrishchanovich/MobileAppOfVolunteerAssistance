package com.example.volunteerassistance

import android.content.Intent
import android.os.Bundle
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

data class UserProfile(
    val name: String = "",
    val surname: String = "",
    val is_help: Boolean = false
)

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        ProfileScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
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

                Toast.makeText(context, "${document.data}", Toast.LENGTH_SHORT).show()

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
            ProfileContent(user = it)
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
fun ProfileContent(user: UserProfile) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Фото профиля",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${user.name} ${user.surname}",
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (!user.is_help) {
            Text(
                text = "Статус",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {}) {
            Text(text = "Редактировать")
        }
        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, StartActivity::class.java)
            context.startActivity(intent)
            (context as? ProfileActivity)?.finish()
        }) {
            Text(text = "Выход")
        }
    }
}

