package com.example.volunteerassistance

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit
import com.example.volunteerassistance.ui.theme.State


class PhoneRegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VolunteerAssistanceTheme {
                PhoneRegistrationScreen {
                }
            }
        }
    }
}


private var verificationId: String? = null

@Composable
fun PhoneRegistrationScreen(onComplete: () -> Unit) {
    val phoneNumber = remember { mutableStateOf("") }
    val verificationCode = remember { mutableStateOf("") }
    val isCodeSent = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val auth = FirebaseAuth.getInstance()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isCodeSent.value) {
                TextField(
                    value = phoneNumber.value,
                    onValueChange = { phoneNumber.value = it },
                    label = { Text("Введите номер телефона") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (phoneNumber.value.isNotBlank()) {
                            sendVerificationCode(phoneNumber.value, context, auth, isCodeSent)
                        } else {
                            Toast.makeText(context, "Введите номер телефона", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Отправить код")
                }
            } else {
                TextField(
                    value = verificationCode.value,
                    onValueChange = { verificationCode.value = it },
                    label = { Text("Введите код подтверждения") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (verificationCode.value.isNotBlank()) {
                            verifyCode(verificationCode.value, auth, context, onComplete)
                        } else {
                            Toast.makeText(context, "Введите код подтверждения", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Подтвердить")
                }
            }
        }
    }
}

fun sendVerificationCode(
    phoneNumber: String,
    context: Context,
    auth: FirebaseAuth,
    isCodeSent: MutableState<Boolean>
) {
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(phoneNumber)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(context as ComponentActivity)
        .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(auth, credential, context)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(context, "Ошибка отправки кода: ${e.message}", Toast.LENGTH_LONG).show()
                println("Ошибка отправки кода: ${e.message}")
                Log.e("PhoneAuth", "Verification failed: ${e.message}")
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(id, token)
                verificationId = id
                isCodeSent.value = true
                Toast.makeText(context, "Код отправлен", Toast.LENGTH_SHORT).show()
            }
        })
        .build()

    PhoneAuthProvider.verifyPhoneNumber(options)
}


fun verifyCode(
    code: String,
    auth: FirebaseAuth,
    context: Context,
    onComplete: () -> Unit
) {
    val id = verificationId
    if (id != null) {
        val credential = PhoneAuthProvider.getCredential(id, code)
        signInWithPhoneAuthCredential(auth, credential, context)
    } else {
        Toast.makeText(context, "Ошибка: не найден ID проверки", Toast.LENGTH_SHORT).show()
    }
}

fun signInWithPhoneAuthCredential(
    auth: FirebaseAuth,
    credential: PhoneAuthCredential,
    context: Context,
) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    saveUserToFirestore(user, context)
                }
            } else {
                Toast.makeText(context, "Ошибка входа: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}

fun saveUserToFirestore(user: FirebaseUser, context: Context) {
    val db = FirebaseFirestore.getInstance()

    val userData = hashMapOf(
        "user_id" to user.uid,
        "phone_number" to user.phoneNumber,
        "is_help" to State.isHelp,
    )

    db.collection("users")
        .document(user.uid)
        .set(userData)
        .addOnSuccessListener {
            Toast.makeText(context, "Пользователь сохранён в Firestore", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Ошибка сохранения данных: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("Firestore", "Error writing data: ${e.message}")
        }
}
