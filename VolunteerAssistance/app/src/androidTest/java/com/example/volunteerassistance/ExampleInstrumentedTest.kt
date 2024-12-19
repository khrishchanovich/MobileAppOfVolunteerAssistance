package com.example.volunteerassistance

import TestRoomViewModelFalse
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.rule.ActivityTestRule
import com.example.volunteerassistance.ui.registrationForNeedHelp.RegistrationActivity
import com.example.volunteerassistance.ui.registrationForNeedHelp.RegistrationScreen
import com.example.volunteerassistance.ui.test.TestProfileScreenFalse
import com.example.volunteerassistance.ui.test.TestProfileScreenTrue
import com.example.volunteerassistance.ui.theme.State
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme
import com.google.firebase.auth.FirebaseAuth
import org.junit.After
import org.junit.Before
import org.junit.Rule


@RunWith(AndroidJUnit4::class)
class LoginScreenUITest {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.volunteerassistance", appContext.packageName)
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginButtonIsDisplayed() {
        composeTestRule.setContent {
            LoginScreen()
        }

        composeTestRule.onNodeWithText("Далее").assertIsDisplayed()
    }

    @Test
    fun regButtonIsDisplayed() {
        composeTestRule.setContent {
            LoginScreen()
        }

        composeTestRule.onNodeWithText("Регистрация").assertIsDisplayed()
    }

    @Test
    fun testLoginButton_withEmptyFields_showsErrorToast() {
        composeTestRule.setContent {
            LoginScreen()
        }

        composeTestRule.onNodeWithText("Далее").performClick()
        composeTestRule.onNodeWithText("Далее").assertIsDisplayed()
    }

    @Test
    fun testLoginButton_withValidInputs_startsProfileActivity() {
        Intents.init()

        composeTestRule.setContent {
            LoginScreen()
        }

        composeTestRule.onNodeWithText("Почта").performTextInput("orannon@gmail.com")
        composeTestRule.onNodeWithText("Пароль").performTextInput("Nastya091103")
        composeTestRule.onNodeWithText("Далее").performClick()

        Thread.sleep(2000)

        Intents.intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun testLoginButton_withInvalidEmail_startsProfileActivity() {
        composeTestRule.setContent {
            LoginScreen()
        }

        composeTestRule.onNodeWithText("Почта").performTextInput("testexamplecom")
        composeTestRule.onNodeWithText("Пароль").performTextInput("password123")
        composeTestRule.onNodeWithText("Далее").performClick()
        composeTestRule.onNodeWithText("Далее").assertIsDisplayed()
    }

    @Test
    fun testLoginButton_withInvalidPassword_startsProfileActivity() {
        composeTestRule.setContent {
            LoginScreen()
        }

        composeTestRule.onNodeWithText("Почта").performTextInput("orranon@gmail.com")
        composeTestRule.onNodeWithText("Пароль").performTextInput("password123")
        composeTestRule.onNodeWithText("Далее").performClick()
        composeTestRule.onNodeWithText("Далее").assertIsDisplayed()
    }

    @Test
    fun testRegistrationButton_startsRegistrationActivity() {
        Intents.init()

        composeTestRule.setContent {
            LoginScreen()
        }

        composeTestRule.onNodeWithText("Регистрация").performClick()

        Thread.sleep(2000)

        Intents.intended(IntentMatchers.hasComponent(RegistrationActivity::class.java.name))
        Intents.release()
    }
}


@RunWith(AndroidJUnit4::class)
class SplashActivityLogTest {

    @get:Rule
    val activityRule = ActivityTestRule(SplashActivity::class.java)

    private lateinit var auth: FirebaseAuth

    @Before
    fun setup() {
        auth = FirebaseAuth.getInstance()
    }

    @Test
    fun testUserLoggedIn() {
        auth.signInWithEmailAndPassword("sad@gmail.com", "Nastya091103").addOnSuccessListener {
            Intents.init()

            activityRule.launchActivity(null)

            Thread.sleep(2000)

            Intents.intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))
            Intents.release()
        }
    }
}

@RunWith(AndroidJUnit4::class)
class RegistrationActivityTest {
    lateinit var auth: FirebaseAuth

    @Before
    fun setUp() {
        auth = FirebaseAuth.getInstance()
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @After
    fun tearDown() {
        val user = auth.currentUser
        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("User deleted successfully.")
            } else {
                println("Failed to delete user: ${task.exception?.message}")
            }
        }
    }

    @Test
    fun testInitialUIElements() {
        composeTestRule.setContent {
            RegistrationScreen { }
        }

        composeTestRule.onNodeWithText("Имя").assertIsDisplayed()
        composeTestRule.onNodeWithText("Фамилия").assertIsDisplayed()
        composeTestRule.onNodeWithText("Почта").assertIsDisplayed()
        composeTestRule.onNodeWithText("Пароль").assertIsDisplayed()
        composeTestRule.onNodeWithText("Далее").assertIsDisplayed()
        composeTestRule.onNodeWithText("Войти").assertIsDisplayed()
    }

    @Test
    fun testRegButton_withEmptyFields_showsErrorToast() {
        composeTestRule.setContent {
            RegistrationScreen { }
        }

        composeTestRule.onNodeWithText("Далее").performClick()
        composeTestRule.onNodeWithText("Далее").assertIsDisplayed()
    }

    @Test
    fun testRegButton_withValidInputs_startsProfileActivity() {
        Intents.init()

        composeTestRule.setContent {
            RegistrationScreen {  }
        }

        State.isHelp = true

        composeTestRule.onNodeWithText("Имя").performTextInput("test")
        composeTestRule.onNodeWithText("Фамилия").performTextInput("test")
        composeTestRule.onNodeWithText("Почта").performTextInput("kidegarden@mail.ru")
        composeTestRule.onNodeWithText("Пароль").performTextInput("password12345")

        composeTestRule.onNodeWithText("Далее").performClick()

        Thread.sleep(2000)

        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
        Intents.release()
    }
}


@RunWith(AndroidJUnit4::class)
class ProfileScreenTestTrue {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testEditButton() {
        composeTestRule.setContent {
            TestProfileScreenTrue()
        }

        composeTestRule.onNodeWithText("Редактировать").assertIsDisplayed()
    }

    @Test
    fun testPhotoButton() {
        composeTestRule.setContent {
            TestProfileScreenTrue()
        }

        composeTestRule.onNodeWithContentDescription("Фото профиля").assertIsDisplayed()
    }

    @Test
    fun testExitButton() {
        composeTestRule.setContent {
            TestProfileScreenTrue()
        }

        composeTestRule.onNodeWithText("Выход").assertIsDisplayed()
    }

    @Test
    fun testExitButtonAction() {
        Intents.init()

        composeTestRule.setContent {
            TestProfileScreenTrue()
        }

        composeTestRule.onNodeWithText("Выход").performClick()

        Thread.sleep(2000)

        Intents.intended(IntentMatchers.hasComponent(StartActivity::class.java.name))
        Intents.release()
    }
}

@RunWith(AndroidJUnit4::class)
class ProfileScreenTestFalse {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testStatusButton() {
        composeTestRule.setContent {
            TestProfileScreenFalse()
        }

        composeTestRule.onNodeWithText("Статус").assertIsDisplayed()
    }
}

class RoomScreenTestFalse {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRoomScreenDisplaysRooms() {
        val testViewModel = TestRoomViewModelFalse().apply {
            fetchRoomList()
        }

        composeTestRule.setContent {
            VolunteerAssistanceTheme {
                TestRoomScreenFalse(
                    onNavigate = {},
                    viewModel = testViewModel
                )
            }
        }

        composeTestRule.onNodeWithText("Комната: Test Room 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Комната: Test Room 2").assertIsDisplayed()
    }

    @Test
    fun testRoomScreenDisplayUpdateButton() {
        val testViewModel = TestRoomViewModelFalse().apply {
            fetchRoomList()
        }

        composeTestRule.setContent {
            VolunteerAssistanceTheme {
                TestRoomScreenFalse(
                    onNavigate = {},
                    viewModel = testViewModel
                )
            }
        }

        composeTestRule.onNodeWithText("Обновить список").assertIsDisplayed()
    }

    @Test
    fun testNavigateToVideoScreenOnRoomClick() {
        val testViewModel = TestRoomViewModelFalse().apply {
            fetchRoomList()
        }

        val viewModel = VideoViewModel()

        composeTestRule.setContent {
            VolunteerAssistanceTheme {
                val navController = rememberNavController()

                viewModel.onPermissionsResult(
                    acceptedAudioPermission = true,
                    acceptedCameraPermission = true
                )

                NavHost(
                    navController = navController,
                    startDestination = "room_screen"
                ) {
                    composable(route = "room_screen") {
                        TestRoomScreenFalse(
                            onNavigate = { route -> navController.navigate(route) },
                            viewModel = testViewModel
                        )
                    }
                    composable(
                        route = "video_screen/{roomName}",
                        arguments = listOf(navArgument("roomName") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val roomName = backStackEntry.arguments?.getString("roomName")
                        VideoScreen(roomName = roomName ?: "", viewModel = viewModel)
                    }
                }
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Комната: Test Room 1").assertIsDisplayed()

        composeTestRule.onNodeWithText("Комната: Test Room 1")
            .performClick()

        composeTestRule.waitForIdle()
    }
}



