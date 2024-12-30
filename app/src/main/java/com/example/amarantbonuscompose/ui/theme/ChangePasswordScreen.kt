package com.example.amarantbonuscompose

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun ChangePasswordScreen(navController: NavHostController, phoneNumber: String) {
    var currentLanguage by remember { mutableStateOf("RUS") }
    var newPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Функция для мультиязычного текста
    fun getText(russian: String, kazakh: String): String {
        return if (currentLanguage == "RUS") russian else kazakh
    }

    // Фон экрана
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFD84386), Color(0xFF050206), Color(0xFF040008)),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Верхняя панель
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Кнопка "Назад"
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Назад",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                // Заголовок с градиентом
                Text(
                    text = getText("Изменение пароля", "Құпиясөзді өзгерту"),
                    color = Color(0xFFD8A657),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFFF69B4), Color(0xFFFF69B4), Color.Black
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                // Кнопка переключения языка
                androidx.compose.material.Button(
                    onClick = {
                        currentLanguage = if (currentLanguage == "RUS") "KAZ" else "RUS"
                    },
                    modifier = Modifier
                        .size(width = 30.dp, height = 40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFFFC107)
                    ),
                    contentPadding = PaddingValues(0.dp)

                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = getText("ЯЗЫК", "ТІЛ"),
                            color = Color.White,
                            fontSize = 8.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = currentLanguage,
                            color = Color.White,
                            fontSize = 8.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Иконка "Пароль"
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Иконка пароля",
                tint = Color.White,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Текстовая информация
            Text(
                text = getText("НОВЫЙ ПАРОЛЬ", "ЖАҢА ҚҰПИЯСӨЗ"),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = getText("Новый пароль должен содержать не менее\n6 цифр", "Жаңа құпиясөз кем дегенде 6 цифрдан тұруы керек"),
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Поле ввода нового пароля
            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    // Ограничиваем только длину пароля до 6 символов
                    if (it.length <= 6) {
                        newPassword = it
                    }
                },
                placeholder = { Text(text = "******", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(), // Преобразование визуализации текста (звезды)
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword), // Используем NumberPassword для числового ввода
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                textStyle = TextStyle(color = Color.White),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    placeholderColor = Color.Gray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка "Изменить"
            Button(
                onClick = {
                    if (newPassword.length < 6) {
                        // Если длина пароля меньше 6 символов
                        Toast.makeText(context, getText("Пароль должен содержать 6 цифр", "Құпиясөз 6 цифрдан тұруы керек"), Toast.LENGTH_LONG).show()
                    } else {
                        isLoading = true
                        coroutineScope.launch {
                            // Получаем результат функции смены пароля
                            val success = changePasswordOnServer(phoneNumber, newPassword)
                            withContext(Dispatchers.Main) {
                                isLoading = false
                                if (success) {
                                    // Если смена пароля прошла успешно
                                    Toast.makeText(context, getText("Пароль успешно изменен", "Құпиясөз сәтті өзгертілді"), Toast.LENGTH_LONG).show()
                                    navController.popBackStack()  // Возвращаемся на предыдущий экран
                                } else {
                                    // Если возникла ошибка при смене пароля
                                    Toast.makeText(context, getText("Ошибка при смене пароля", "Құпиясөзді өзгерту кезінде қате"), Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .width(200.dp) // Уменьшаем кнопку "Изменить"
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF8E24AA)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = getText("ИЗМЕНИТЬ", "ӨЗГЕРТУ"),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Log.d("ChangePassword", "Sending request with tel: $phoneNumber, new password: $newPassword")

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка "Отмена"
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .width(200.dp) // Уменьшаем кнопку "Отмена"
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF8E24AA)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = getText("Отмена", "Бас тарту"),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Функция для смены пароля на сервере
suspend fun changePasswordOnServer(phoneNumber: String, newPassword: String): Boolean {
    val url = "http://2.135.218.2/ut_api/hs/api/change_pass/"
    val client = OkHttpClient()

    val json = JSONObject().apply {
        put("tel", phoneNumber)
        put("new_password", newPassword)
    }

    val mediaType = "application/json".toMediaTypeOrNull()
    val requestBody = json.toString().toRequestBody(mediaType)

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    return try {
        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        // Логируем ответ от сервера
        if (response.isSuccessful) {
            Log.d("ChangePassword", "Password change successful: ${response.body?.string()}")
        } else {
            Log.e("ChangePassword", "Error: ${response.code}")
        }

        response.isSuccessful
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}
