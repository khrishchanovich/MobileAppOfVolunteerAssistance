package com.example.volunteerassistance

import android.os.Bundle
import android.view.SurfaceView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme
import com.google.firebase.database.*
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VideoCallActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VolunteerAssistanceTheme {
                VideoCallScreen()
            }
        }
    }
}

@Composable
fun VideoCallScreen(viewModel: VideoCallViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val context = LocalContext.current
    val localVideoView = remember { SurfaceView(context) }
    val remoteVideoView = remember { SurfaceView(context) }
    val callState by viewModel.callState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        if (callState == "waiting" || callState == "in_call") {
            Row(Modifier.weight(1f)) {
                AndroidView(factory = { localVideoView }, modifier = Modifier.weight(1f))
                AndroidView(factory = { remoteVideoView }, modifier = Modifier.weight(1f))
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Ожидание подключения...")
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            androidx.compose.material3.Button(onClick = { viewModel.startCall(localVideoView, remoteVideoView) }) {
                androidx.compose.material3.Text("Начать звонок")
            }
            androidx.compose.material3.Button(onClick = { viewModel.endCall() }) {
                androidx.compose.material3.Text("Завершить звонок")
            }
        }
    }
}

class VideoCallViewModel : ViewModel() {
    private val _callState = MutableStateFlow("waiting")
    val callState: StateFlow<String> = _callState

    private val channelName = "test_channel"
    private val userId = "user_1_id"
    private lateinit var rtcEngine: RtcEngine
    private val callRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("calls").child(channelName)

    init {
        // Инициализация Firebase
        callRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val callData = snapshot.getValue(CallData::class.java)
                if (callData != null) {
                    _callState.value = callData.status
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _callState.value = "error"
            }
        })

        // Инициализация Agora
        rtcEngine = RtcEngine.create(
            /* context */ null, "your-agora-app-id", object : IRtcEngineEventHandler() {
                override fun onUserJoined(uid: Int, elapsed: Int) {
                    super.onUserJoined(uid, elapsed)
                }

                override fun onUserOffline(uid: Int, reason: Int) {
                    super.onUserOffline(uid, reason)
                }
            }
        )
    }

    fun startCall(localVideo: SurfaceView, remoteVideo: SurfaceView) {
        rtcEngine.setupLocalVideo(VideoCanvas(localVideo, VideoCanvas.RENDER_MODE_HIDDEN, 0))
        rtcEngine.joinChannel(null, channelName, "extra", 0)
        val callData = mapOf(
            "host" to userId,
            "participants" to mapOf(userId to true),
            "status" to "in_call"
        )
        callRef.setValue(callData)
        rtcEngine.setupRemoteVideo(VideoCanvas(remoteVideo, VideoCanvas.RENDER_MODE_HIDDEN, 1))
    }

    fun endCall() {
        rtcEngine.leaveChannel()
        rtcEngine.setupLocalVideo(null)
        callRef.child("status").setValue("ended")
    }
}

data class CallData(
    val host: String = "",
    val participants: Map<String, Boolean> = mapOf(),
    val status: String = "waiting"
)
