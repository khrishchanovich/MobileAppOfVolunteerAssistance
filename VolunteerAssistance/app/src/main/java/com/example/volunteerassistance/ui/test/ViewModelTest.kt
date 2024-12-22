import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.volunteerassistance.TextFieldState
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class TestRoomViewModelFalse : ViewModel() {
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
        _isHelp.value = false
    }

    fun updateRoomList(newRooms: List<String>) {
        _roomList.value = newRooms
    }

    fun fetchRoomList() {
        _roomList.value = listOf("Test Room 1", "Test Room 2")
    }

    fun deleteRoom(roomName: String) {
        _roomList.value = _roomList.value.filter { it != roomName }
    }

    fun onJoinRoom() {
        val testRoomName = "TestRoom"
        _roomName.value = TextFieldState(testRoomName)
        viewModelScope.launch {
            _onJoinEvent.emit(testRoomName)
        }
    }
}


class TestRoomViewModelTrue : ViewModel() {
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
        _isHelp.value = true
    }

    fun updateRoomList(newRooms: List<String>) {
        _roomList.value = newRooms
    }

    fun fetchRoomList() {
        // Тестовая реализация: можно задавать значения вручную в тестах
    }

    fun deleteRoom(roomName: String) {
        // Тестовая реализация: обновляет список комнат
        _roomList.value = _roomList.value.filter { it != roomName }
    }

    fun onJoinRoom() {
        val testRoomName = "TestRoom"
        _roomName.value = TextFieldState(testRoomName)
        viewModelScope.launch {
            _onJoinEvent.emit(testRoomName)
        }
    }
}
