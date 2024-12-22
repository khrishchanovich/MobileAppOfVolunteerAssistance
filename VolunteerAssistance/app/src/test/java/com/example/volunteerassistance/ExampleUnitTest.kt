package com.example.volunteerassistance

import android.content.Intent
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Test

import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.robolectric.Robolectric
import org.robolectric.Shadows

class VideoViewModelTest {

    private lateinit var videoViewModel: VideoViewModel

    @Before
    fun setUp() {
        videoViewModel = VideoViewModel()
    }

    @Test
    fun `test onPermissionsResult updates audio and camera permission states`() {
        videoViewModel.onPermissionsResult(true, false)

        assert(videoViewModel.hasAudioPermission.value)
        assert(!videoViewModel.hasCameraPermission.value)
    }
}

class UserProfileTest {

    @Test
    fun `test default user profile`() {
        val userProfile = UserProfile()

        assertEquals("", userProfile.name)
        assertEquals("", userProfile.surname)
        assertFalse(userProfile.is_help)
    }

    @Test
    fun `test custom user profile`() {
        val userProfile = UserProfile(name = "Анастасия", surname = "Хрищанович", is_help = true)

        assertEquals("Анастасия", userProfile.name)
        assertEquals("Хрищанович", userProfile.surname)
        assertTrue(userProfile.is_help)
    }

    @Test
    fun `test user profile equality`() {
        val userProfile1 = UserProfile(name = "Полина", surname = "Найдер", is_help = false)
        val userProfile2 = UserProfile(name = "Полина", surname = "Найдер", is_help = false)

        assertEquals(userProfile1, userProfile2)
    }

    @Test
    fun `test user profile inequality`() {
        val userProfile1 = UserProfile(name = "Полина", surname = "Найдер", is_help = false)
        val userProfile2 = UserProfile(name = "Настя", surname = "Хрищанович", is_help = true)

        assertNotEquals(userProfile1, userProfile2)
    }
}

class TextFieldStateTest {

    @Test
    fun `test default TextFieldState`() {
        val textFieldState = TextFieldState()

        assertEquals("", textFieldState.text)
        assertNull(textFieldState.error)
    }

    @Test
    fun `test custom TextFieldState`() {
        val textFieldState = TextFieldState(text = "Комната", error = "Неверное название комнаты")

        assertEquals("Комната", textFieldState.text)
        assertEquals(
            "Неверное название комнаты",
            textFieldState.error
        )
    }

    @Test
    fun `test TextFieldState with null error`() {
        val textFieldState = TextFieldState(text = "Комната", error = null)

        assertEquals("Комната", textFieldState.text)
        assertNull(textFieldState.error)
    }

    @Test
    fun `test TextFieldState equality`() {
        val textFieldState1 = TextFieldState(text = "Комната 1", error = "Не запускается!")
        val textFieldState2 = TextFieldState(text = "Комната 1", error = "Не запускается!")

        assertEquals(textFieldState1, textFieldState2)
    }

    @Test
    fun `test TextFieldState inequality`() {
        val textFieldState1 = TextFieldState(text = "Комната 1", error = "Не запускается!")
        val textFieldState2 = TextFieldState(text = "Комната 2", error = null)

        assertNotEquals(textFieldState1, textFieldState2)
    }

    @Test
    fun `test empty text with null error`() {
        val textFieldState = TextFieldState(text = "", error = null)

        assertEquals("", textFieldState.text)
        assertNull(textFieldState.error)
    }
}
