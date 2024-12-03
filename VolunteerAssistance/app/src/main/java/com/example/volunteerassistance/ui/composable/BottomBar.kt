package com.example.volunteerassistance.ui.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomBar(
    onMainClick: () -> Unit,
    onProfileClick: () -> Unit,
    selectedTab: String
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == "main",
            onClick = onMainClick,
            label = { Text("Main") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Main") }
        )

        NavigationBarItem(
            selected = selectedTab == "profile",
            onClick = onProfileClick,
            label = { Text("Profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") }
        )
    }
}