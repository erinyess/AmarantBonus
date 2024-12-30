// DeleteProfileScreen.kt
package com.example.amarantbonuscompose

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.text.KeyboardOptions
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DeleteProfileScreen(navController: NavHostController, phoneNumber: String) {
    var currentLanguage by remember { mutableStateOf("RUS") }
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
                    text = getText("Удаление Профиля", "Профильді жою"),
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
                        backgroundColor = Color(
                            0xFFFFC107
                        )
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

            // Иконка удаления
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Иконка удаления",
                tint = Color.White,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Основной текст
            Text(
                text = getText(
                    "Вы уверены, что хотите УДАЛИТЬ\nсвои пользовательские данные и\nПРОФИЛЬ?",
                    "Пайдаланушы деректеріңізді және\nПРОФИЛІҢІЗДІ ЖОЮҒА сенімдісіз бе?"
                ),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = getText(
                    "Если вы удалите свой ПРОФИЛЬ, то\nвсе накопленные бонусные БАЛЛЫ\nбудут НАВСЕГДА УДАЛЕНЫ, а также\nВы не сможете продолжить\nпользоваться бонусной системой\nв сети Гастромаркетов AMARANT",
                    "Егер сіз ПРОФИЛІҢІЗДІ ЖОЙСАҢЫЗ,\nбарлық жиналған бонус БАЛЛАРЫҢЫЗ\nМӘҢГІЛІК ЖОЙЫЛАДЫ, сонымен қатар\nAMARANT Гастромаркеттер желісінде\nбонус жүйесін пайдалануды\nжалғастыра алмайсыз"
                ),
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопка "Удалить Профиль"
            Button(
                onClick = {
                    coroutineScope.launch {
                        val success = deleteProfileFromServer(phoneNumber)
                        withContext(Dispatchers.Main) {
                            if (success) {
                                Toast.makeText(context, getText("Профиль удален", "Профиль жойылды"), Toast.LENGTH_LONG).show()
                                navController.navigate("register_screen") {
                                    popUpTo("welcome_screen") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, getText("Ошибка при удалении профиля", "Профильді жою кезінде қате"), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF8E24AA)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = getText("Удалить Профиль", "Профильді жою"),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка "Отмена"
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
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

// Функция для удаления профиля с сервера
suspend fun deleteProfileFromServer(phoneNumber: String): Boolean {
    val url = "http://2.135.218.2/ut_api/hs/api/delete_acc/?tel=$phoneNumber" // Составляем URL с номером телефона
    val client = OkHttpClient()

    // Создаем запрос
    val request = Request.Builder()
        .url(url)
        .get() // Используем метод GET для запроса
        .build()

    return try {
        // Выполняем запрос в фоновом потоке
        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        // Проверяем, успешен ли запрос (HTTP 200)
        response.isSuccessful
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}
