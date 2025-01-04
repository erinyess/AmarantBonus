
package com.example.amarantbonuscompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.rememberCoroutineScope
import android.util.Log

@Composable
fun LoginScreen(navController: NavHostController) {
    var currentLanguage by remember { mutableStateOf("RUS") }

    fun getText(russian: String, kazakh: String): String {
        return if (currentLanguage == "RUS") russian else kazakh
    }

    var showLoginFields by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoginButtonEnabled by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(phoneNumber, password) {
        isLoginButtonEnabled = phoneNumber.isNotBlank() && password.isNotBlank()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFD84386), Color(0xFF050206), Color(0xFF040008)),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            )
    ) {
        Text(
            text = "v.1.0",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        )

        Button(
            onClick = { currentLanguage = if (currentLanguage == "RUS") "KAZ" else "RUS" },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
                .size(width = 30.dp, height = 40.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFC107)),
            contentPadding = PaddingValues(0.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (currentLanguage == "RUS") "ЯЗЫК" else "ТІЛ",
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Text(
                    text = currentLanguage,
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.new_image),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "AMARANT",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "GASTROMARKETI",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!showLoginFields) {
                Text(
                    text = getText(
                        "Если Вы уже зарегистрированы в Бонусной программе, то нажмите Войти",
                        "Егер Сіз Бонустық бағдарламада тіркелген болсаңыз, Кіру батырмасын басыңыз"
                    ),
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showLoginFields = true },
                    modifier = Modifier
                        .width(270.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF791787))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_login_icon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = getText("Войти", "Кіру"),
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = getText(
                        "Если у Вас нет нашей бонусной карты, то нажмите Регистрация",
                        "Егер Сізде біздің бонустық карта болмаса, Тіркелу батырмасын басыңыз"
                    ),
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            else {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {
                        if (it.length <= 10) {
                            phoneNumber = it
                        }
                    },
                    placeholder = { Text(text = "7770000001", color = Color.White.copy(alpha = 0.5f)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = { Text(text = "+7", color = Color.White, fontSize = 16.sp) },
                    modifier = Modifier
                        .width(270.dp)
                        .height(56.dp),
                    textStyle = TextStyle(color = Color.White),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        if (it.length <= 6) {
                            password = it
                        }
                    },
                    placeholder = { Text(text = "123456", color = Color.White.copy(alpha = 0.5f)) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = null, tint = Color.White)
                        }
                    },
                    modifier = Modifier
                        .width(270.dp)
                        .height(56.dp),
                    textStyle = TextStyle(color = Color.White),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            performLogin(phoneNumber, password, navController, { errorMessage = it; showErrorDialog = true }, currentLanguage)
                        }
                    },
                    modifier = Modifier
                        .width(270.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = if (isLoginButtonEnabled) Color(0xFF9C27B0) else Color.Gray),
                    enabled = isLoginButtonEnabled
                ) {
                    Text(text = getText("Войти", "Кіру"), color = Color.White, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("register_screen") },
                modifier = Modifier
                    .width(270.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9C27B0))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_person_icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = getText("Регистрация", "Тіркелу"), color = Color.White, fontSize = 18.sp)
            }
        }

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text(text = getText("Ошибка", "Қате")) },
                text = { Text(text = errorMessage) },
                confirmButton = {
                    Button(onClick = { showErrorDialog = false }) {
                        Text(text = getText("ОК", "Жарайды"))
                    }
                }
            )
        }
    }
}

suspend fun performLogin(
    phoneNumber: String,
    password: String,
    navController: NavHostController,
    onError: (String) -> Unit,
    language: String
) {
    try {
        withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val url = "http://2.135.218.2/ut_api/hs/api/get_bonuses/?tel=$phoneNumber"
            val request = Request.Builder().url(url).build()

            Log.d("LoginDebug", "Sending request to URL: $url")

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            Log.d("LoginDebug", "Response received: $responseBody")

            if (responseBody.isNullOrEmpty()) {
                Log.e("LoginError", "Empty response from server")
                withContext(Dispatchers.Main) { onError("Empty response from server") }
                return@withContext
            }

            val jsonResponse = JSONObject(responseBody)

            val name = jsonResponse.optString("name", "")
            val bonuses = jsonResponse.optInt("bonusi", -1)

            val success = name.isNotEmpty() && bonuses >= 0
            val message = if (success) {
                "Login successful for user: $name"
            } else {
                "Invalid response structure from server"
            }

            Log.d("LoginDebug", "Parsed JSON Response: success=$success, message=$message")

            if (success) {
                Log.d("LoginDebug", "Login successful, preparing navigation")
                withContext(Dispatchers.Main) {
                    navController.navigate("main_screen/$phoneNumber/$bonuses")
                }
            } else {
                Log.e("LoginError", "Server returned error: $message")
                withContext(Dispatchers.Main) { onError(message) }
            }
        }
    } catch (e: Exception) {
        Log.e("LoginError", "Exception occurred: ${e.localizedMessage}", e)
        withContext(Dispatchers.Main) { onError(e.message ?: "Unknown error occurred") }
    }
}

