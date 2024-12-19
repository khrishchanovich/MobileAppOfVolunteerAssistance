package com.example.volunteerassistance.ui.registrationForNeedHelp

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volunteerassistance.LoginActivity
import com.example.volunteerassistance.MainActivity
import com.example.volunteerassistance.ProfileActivity
import com.example.volunteerassistance.ui.theme.VolunteerAssistanceTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.example.volunteerassistance.ui.theme.State
import com.google.firebase.auth.ktx.auth

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VolunteerAssistanceTheme {
                RegistrationScreen {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun RegistrationScreen(onNextClick: () -> Unit) {
    val auth = Firebase.auth
    val db = FirebaseFirestore.getInstance()

    val nameState = remember { mutableStateOf("") }
    val surnameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = nameState.value,
                onValueChange = {  nameState.value = it },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )

            TextField(
                value = surnameState.value,
                onValueChange = { surnameState.value = it },
                label = { Text("Фамилия") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )

            TextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Почта") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            TextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Button(
                onClick = {
                    if (nameState.value.isBlank() || surnameState.value.isBlank() || emailState.value.isBlank() || passwordState.value.isBlank()) {
                        Toast.makeText(context, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailState.value).matches()) {
                        Toast.makeText(context, "Введите корректный email", Toast.LENGTH_SHORT).show()
                    } else if (passwordState.value.length < 8) {
                        Toast.makeText(context, "Пароль должен содержать не менее 6 символов", Toast.LENGTH_SHORT).show()
                    } else {
                        signUp(auth, db, nameState.value, surnameState.value, emailState.value, passwordState.value, context)

//                        val intent = Intent(context, MainActivity::class.java)
//                        context.startActivity(intent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Далее", fontSize = 18.sp)
            }

            Button(
                onClick = {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Войти", fontSize = 18.sp)
            }
        }
    }
}

fun signUp(auth: FirebaseAuth, db: FirebaseFirestore, name: String, surname: String, email: String, password: String, context: Context) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("Success", "Sign Up successful")
                Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()

                val user = auth.currentUser

                if (user != null) {
                    val userData = hashMapOf(
                        "user_id" to user.uid,
                        "name" to name,
                        "surname" to surname,
                        "email" to email,
                        "is_help" to State.isHelp
                    )

                    db.collection("users")
                        .document(user.uid)
                        .set(userData)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Data saved successfully")
                            Toast.makeText(context, "Регистрация успешна!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(context, ProfileActivity::class.java)
                            context.startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error writing data: ${e.message}")
                            Toast.makeText(context, "Ошибка сохранения данных: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Log.d("Fail", "Sign Up failure")
                Toast.makeText(context, "Ошибище", Toast.LENGTH_SHORT).show()
            }
        }
}


@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    VolunteerAssistanceTheme {
        RegistrationScreen { }
    }
}