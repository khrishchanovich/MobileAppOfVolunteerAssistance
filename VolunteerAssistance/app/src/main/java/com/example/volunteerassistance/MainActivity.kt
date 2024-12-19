package com.example.volunteerassistance
import TestRoomViewModelFalse
import TestRoomViewModelTrue
import android.Manifest
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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

const val APP_ID = "03a4f6b25b2647b89a6d4f2641cb64ab"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                                RoomScreen(onNavigate = navController::navigate)
                            }
                            composable(
                                route = "video_screen/{roomName}",
                                arguments = listOf(
                                    navArgument(name = "roomName") { type = NavType.StringType }
                                )
                            ) {
                                val roomName = it.arguments?.getString("roomName") ?: return@composable
                                VideoScreen(
                                    roomName = roomName
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RoomScreen(
    onNavigate: (String) -> Unit,
    viewModel: RoomViewModel = viewModel()
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

            Button(onClick = viewModel::onJoinRoom) {
                Text(text = "Помощь по видеозвонку")
            }
        } else if (isHelp == false) {
            Text(text = "Возможно, здесь требуется помощь")
            Spacer(modifier = Modifier.height(16.dp))

            if (roomList.isEmpty()) {
                Text(text = "Все хорошо. Помощь не требуется")
            } else {
                roomList.forEach { room ->
                    Button(
                        onClick = { onNavigate("video_screen/$room") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = "Комната: $room")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = viewModel::fetchRoomList) {
                Text(text = "Обновить список")
            }
        } else {
            Text(text = "Загрузка...", modifier = Modifier.padding(top = 16.dp))
        }
    }
}


@Composable
fun TestRoomScreenFalse(
    onNavigate: (String) -> Unit,
    viewModel: TestRoomViewModelFalse = viewModel()
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

            Button(onClick = viewModel::onJoinRoom) {
                Text(text = "Помощь по видеозвонку")
            }
        } else if (isHelp == false) {
            Text(text = "Возможно, здесь требуется помощь")
            Spacer(modifier = Modifier.height(16.dp))

            if (roomList.isEmpty()) {
                Text(text = "Все хорошо. Помощь не требуется")
            } else {
                roomList.forEach { room ->
                    Button(
                        onClick = { onNavigate("video_screen/$room") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = "Комната: $room")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = viewModel::fetchRoomList) {
                Text(text = "Обновить список")
            }
        } else {
            Text(text = "Загрузка...", modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun TestRoomScreenTrue(
    onNavigate: (String) -> Unit,
    viewModel: TestRoomViewModelTrue = viewModel()
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

        } else if (isHelp == false) {
            Text(text = "Возможно, здесь требуется помощь")
            Spacer(modifier = Modifier.height(16.dp))

            if (roomList.isEmpty()) {
                Text(text = "Все хорошо. Помощь не требуется")
            } else {
                roomList.forEach { room ->
                    Button(
                        onClick = { onNavigate("video_screen/$room") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = "Комната: $room")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = viewModel::fetchRoomList) {
                Text(text = "Обновить список")
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
    roomViewModel: RoomViewModel = viewModel()
) {
    Text(text = "VideoScreen for $roomName")
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
        // Выход из канала Agora
        agoraView?.leaveChannel()

        // Если у пользователя есть помощь, удаляем комнату
        if (roomViewModel.isHelp.value == true) {
            roomViewModel.deleteRoom(roomName)
        }

        // Переход в MainActivity после завершения звонка
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
            .get()
            .addOnSuccessListener { documents ->
                val rooms = documents.mapNotNull { it.getString("roomName") }
                _roomList.value = rooms
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

        val roomData = mapOf(
            "roomName" to userId,
            "timestamp" to System.currentTimeMillis()
        )

        Firebase.firestore.collection("rooms")
            .document(userId)
            .set(roomData)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _onJoinEvent.emit(userId) // UID передается как имя комнаты
                }
            }
            .addOnFailureListener {
                _roomName.value = roomName.value.copy(
                    error = "Failed to create the room"
                )
            }
    }
}


data class TextFieldState(
    val text: String = "",
    val error: String? = null
)