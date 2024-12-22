package com.example.volunteerassistance.ui.test

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.volunteerassistance.MainActivity
import com.example.volunteerassistance.ProfileContent
import com.example.volunteerassistance.UserProfile
import com.example.volunteerassistance.ui.composable.BottomBar
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme

//class ProfileActivityFalse : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            VolunteerAssistanceTheme {
//                Scaffold(
//                    bottomBar = {
//                        BottomBar(
//                            selectedTab = "profile",
//                            onMainClick = {
//                                startActivity(Intent(this, MainActivity::class.java))
//                            },
//                            onProfileClick = { }
//                        )
//                    }
//                ) { padding ->
//                    Surface(
//                        modifier = Modifier.padding(padding),
//                        color = MaterialTheme.colorScheme.background
//                    ) {
//                        TestProfileScreenFalse()
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun TestProfileScreenFalse() {
//    val testUser = UserProfile(
//        name = "Иван",
//        surname = "Иванов",
//        is_help = false
//    )
//
//    ProfileContent(user = testUser)
//}
//
//@Composable
//fun TestProfileScreenTrue() {
//    val testUser = UserProfile(
//        name = "Иван",
//        surname = "Иванов",
//        is_help = true
//    )
//
//    ProfileContent(user = testUser)
//}
//
