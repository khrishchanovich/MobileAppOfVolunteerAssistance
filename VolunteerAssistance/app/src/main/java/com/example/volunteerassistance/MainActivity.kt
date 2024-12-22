package com.example.volunteerassistance
import TestRoomViewModelFalse
import TestRoomViewModelTrue
import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.volunteerassistance.ui.composable.BottomBar
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.agora.agorauikit_android.AgoraConnectionData
import io.agora.agorauikit_android.AgoraVideoViewer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

const val APP_ID = "03a4f6b25b2647b89a6d4f2641cb64ab"

class MainActivity : ComponentActivity() {

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
                val navController = rememberNavController()

                val currentRoute = navController.currentBackStackEntry?.destination?.route

                Scaffold(
                    bottomBar = {
                        if (currentRoute != "video_screen/{roomName}") {
                            BottomBar(
                                selectedTab = "main",
                                onProfileClick = {
                                    startActivity(Intent(this, ProfileActivity::class.java))
                                },
                                onMainClick = { }
                            )
                        }
                    }
                ) { padding ->
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.padding(padding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "room_screen"
                        ) {
                            composable(route = "room_screen") {
                                RoomScreen(
                                    onNavigate = navController::navigate,
                                    onSpeakClick = {
                                        speakText("Вы можете получить помощь от волонтера по видеозвонку. Для этого нажмите на синюю кнопку ниже.")
                                    })
                            }
                            composable(
                                route = "video_screen/{roomName}",
                                arguments = listOf(
                                    navArgument(name = "roomName") { type = NavType.StringType }
                                )
                            ) {
                                val roomName = it.arguments?.getString("roomName") ?: return@composable
                                VideoScreen(
                                    roomName = roomName,
                                    onSpeakClick = {
                                        if (::textToSpeech.isInitialized) {
                                            speakText("Нажмите на кнопку 'Назад' для выхода из видеозвонка.")
                                        }
                                    }
                                )
                            }
                        }
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
fun RoomScreen(
    onNavigate: (String) -> Unit,
    viewModel: RoomViewModel = viewModel(),
    onSpeakClick: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        viewModel.onJoinEvent.collectLatest { name ->
            onNavigate("video_screen/$name")
        }
    }
    val isHelp = viewModel.isHelp.value
    val roomList = viewModel.roomList.value

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isHelp == true) {
            viewModel.roomName.value.error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSpeakClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            ) {
                Text("🔊", color = Color.White, fontSize = 24.sp, textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = viewModel::onJoinRoom,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(124.dp)
            ) {
                Text(text = "Помощь по видеозвонку", fontSize = 20.sp, color = Color.White)
            }
        } else if (isHelp == false) {
            Text(text = "Возможно, здесь требуется помощь")
            Spacer(modifier = Modifier.height(16.dp))

            if (roomList.isEmpty()) {
                Text(text = "Все хорошо. Помощь не требуется")
            } else {
                roomList.forEach { room -> // Display user names as room names
                    Button(
                        onClick = { onNavigate("video_screen/$room") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = "Комната: $room")  // Show the room name
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = viewModel::fetchRoomList,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp)) {
                Text(text = "Обновить список", fontSize = 20.sp)
            }
        } else {
            Text(text = "Загрузка...", modifier = Modifier.padding(top = 16.dp))
        }
    }
}


class VideoViewModel: ViewModel() {
    private val _hasAudioPermission = mutableStateOf(false)
    val hasAudioPermission: State<Boolean> = _hasAudioPermission

    private val _hasCameraPermission = mutableStateOf(false)
    val hasCameraPermission: State<Boolean> = _hasCameraPermission

    fun onPermissionsResult(
        acceptedAudioPermission: Boolean,
        acceptedCameraPermission: Boolean
    ) {
        _hasAudioPermission.value = acceptedAudioPermission
        _hasCameraPermission.value = acceptedCameraPermission
    }
}

@Composable
fun VideoScreen(
    roomName: String,
    viewModel: VideoViewModel = viewModel(),
    roomViewModel: RoomViewModel = viewModel(),
    onSpeakClick: () -> Unit,
) {

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
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

    val context = LocalContext.current
    var agoraView: AgoraVideoViewer? = null

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            viewModel.onPermissionsResult(
                acceptedAudioPermission = perms[Manifest.permission.RECORD_AUDIO] == true,
                acceptedCameraPermission = perms[Manifest.permission.CAMERA] == true
            )
        }
    )
    LaunchedEffect(key1 = true) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            )
        )
    }

    val onChannelLeave = {
        agoraView?.leaveChannel()

        if (roomViewModel.isHelp.value == true) {
            roomViewModel.deleteRoom(roomName)
        }

        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }

    BackHandler(enabled = true) {
        agoraView?.leaveChannel()

        if (roomViewModel.isHelp.value == true) {
            roomViewModel.deleteRoom(roomName)
        }

        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }

    if (viewModel.hasAudioPermission.value && viewModel.hasCameraPermission.value) {
        AndroidView(
            factory = {
                AgoraVideoViewer(
                    it, connectionData = AgoraConnectionData(
                        appId = APP_ID
                    )
                ).also {
                    it.join(roomName)
                    agoraView = it
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        DisposableEffect(Unit) {
            onDispose {
                onChannelLeave()
            }
        }
    }
}

class RoomViewModel : ViewModel() {
    private val _roomName = mutableStateOf(TextFieldState())
    val roomName: State<TextFieldState> = _roomName

    private val _onJoinEvent = MutableSharedFlow<String>()
    val onJoinEvent = _onJoinEvent.asSharedFlow()

    private val _isHelp = mutableStateOf<Boolean?>(null)
    val isHelp: State<Boolean?> = _isHelp

    private val _roomList = mutableStateOf<List<String>>(emptyList())
    val roomList: State<List<String>> = _roomList

    init {
        fetchUserHelpStatus()
        fetchRoomList()
    }

    fun fetchUserHelpStatus() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            _isHelp.value = false  // Пользователь не авторизован, можно задать значение по умолчанию
            return
        }
        val userId = currentUser!!.uid

        Firebase.firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                _isHelp.value = document.getBoolean("is_help")
                if (_isHelp.value == false) {
                    fetchRoomList()
                }
            }
            .addOnFailureListener {
                _isHelp.value = false
            }
    }

    fun fetchRoomList() {
        Firebase.firestore.collection("rooms")
            .whereEqualTo("isVolunteer", 0)
            .get()
            .addOnSuccessListener { documents ->
                val rooms = documents.mapNotNull { it.getString("roomName") }
                _roomList.value = rooms // Use room name as the user name
            }
            .addOnFailureListener {
                _roomList.value = emptyList()
            }
    }


    fun deleteRoom(roomName: String) {
        Firebase.firestore.collection("rooms")
            .document(roomName)
            .delete()
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
                println("Ошибка при удалении комнаты: ${exception.message}")
            }
    }

    fun onJoinRoom() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser!!.uid

        // Fetch user's name from Firestore
        Firebase.firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val userName = document.getString("name") ?: "Unknown User" // Get the name or fallback to "Unknown User"

                val roomData = mapOf(
                    "roomName" to userName,  // Use the user's name here
                    "timestamp" to System.currentTimeMillis(),
                    "isVolunteer" to 0 // Default isVolunteer to 0
                )

                Firebase.firestore.collection("rooms")
                    .document(userName)  // Set document name to the user's name
                    .set(roomData)
                    .addOnSuccessListener {
                        viewModelScope.launch {
                            _onJoinEvent.emit(userName) // Emit the user's name instead of UID
                        }
                    }
                    .addOnFailureListener {
                        _roomName.value = _roomName.value.copy(
                            error = "Не удалось создать комнату"
                        )
                    }
            }
            .addOnFailureListener {
                _roomName.value = _roomName.value.copy(
                    error = "Не удалось получить имя пользователя"
                )
            }
    }


    fun onVolunteerJoin(roomName: String) {
        Firebase.firestore.collection("rooms")
            .document(roomName)
            .update("isVolunteer", 1)
            .addOnSuccessListener {
                println("Волонтёр присоединился к комнате: $roomName")
            }
            .addOnFailureListener { exception ->
                println("Не удалось обновить isVolunteer: ${exception.message}")
            }
    }

    private fun checkIfUserIsVolunteer(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return false
        var isVolunteer = false
        Firebase.firestore.collection("users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                isVolunteer = document.getBoolean("isVolunteer") == true
            }
            .addOnFailureListener {
                println("Не удалось проверить роль пользователя: ${it.message}")
            }
        return isVolunteer
    }
}


data class TextFieldState(
    val text: String = "",
    val error: String? = null
)