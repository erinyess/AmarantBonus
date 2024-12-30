// ProfileScreen.kt
package com.example.amarantbonuscompose

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController, phoneNumber: String) {
    var currentLanguage by remember { mutableStateOf("RUS") }
    val context = LocalContext.current

    // Состояния для данных пользователя
    var fullName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    // Функция для мультиязычного текста
    fun getText(russian: String, kazakh: String): String {
        return if (currentLanguage == "RUS") russian else kazakh
    }

    // Функция для получения данных пользователя с сервера
    suspend fun fetchUserData(tel: String) {
        val url = "http://2.135.218.2/ut_api/hs/api/get_bonuses/?tel=$tel"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).get().build()

        try {
            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }

            if (response.isSuccessful) {
                val responseBody = response.body?.string()

                // Выводим ответ сервера для отладки
                println("Server response: $responseBody")

                val jsonObject = JSONObject(responseBody ?: "{}")
                withContext(Dispatchers.Main) {
                    fullName = jsonObject.optString("name", "")
                    birthDate = jsonObject.optString("dateOfBirth", "")
                    city = jsonObject.optString("city", "")
                }
            } else {
                Log.e("ProfileScreen", "Ошибка сервера: ${response.code}")
                println("Server error code: ${response.code}")
            }
        } catch (e: IOException) {
            Log.e("ProfileScreen", "Ошибка сети: ${e.message}")
            println("Network error: ${e.message}")
        } catch (e: Exception) {
            Log.e("ProfileScreen", "Ошибка: ${e.message}")
            println("Error: ${e.message}")
        }
    }

    // Загружаем данные пользователя при первом запуске экрана
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            fetchUserData(phoneNumber)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF7900D5),
                        Color(0xFF050206),
                        Color(0xFF050206),
                        Color(0xFFC638A0)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Верхняя панель с кнопкой "Назад" и языковой кнопкой
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Назад",
                        tint = Color.White
                    )
                }

                // Кнопка переключения языка (как на других экранах)
                Button(
                    onClick = { currentLanguage = if (currentLanguage == "RUS") "KAZ" else "RUS" },
                    modifier = Modifier
                        .size(width = 30.dp, height = 40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
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
            }

            // Информация о пользователе
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.8f), shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    UserInfoRow(
                        label = getText("Фамилия Имя", "Тегі Аты"),
                        value = fullName
                    )
                    UserInfoRow(
                        label = getText("Дата рождения", "Туған күні"),
                        value = birthDate
                    )
                    UserInfoRow(
                        label = getText("Номер телефона", "Телефон нөмірі"),
                        value = "+7$phoneNumber"
                    )
                    UserInfoRow(
                        label = getText("Город", "Қала"),
                        value = city
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Отодвигаем кнопки вниз

            // Текст "Больше информации об AMARANT"
            Text(
                text = getText(
                    "Больше информации об\nAMARANT вы найдете по\nследующим ссылкам",
                    "AMARANT туралы көбірек ақпаратты\nкелесі сілтемелерден таба аласыз"
                ),
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Центрированные кнопки
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ExternalLinkButton(
                    text = getText("Интернет магазин", "Интернет дүкен"),
                    icon = painterResource(id = R.drawable.ic_cart),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amarant.kz"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(70.dp)
                        .padding(bottom = 12.dp)
                )

                ExternalLinkButton(
                    text = "INSTAGRAM",
                    icon = painterResource(id = R.drawable.ic_instagram),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/amarant.kz/"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(70.dp)
                        .padding(bottom = 12.dp)
                )

                ExternalLinkButton(
                    text = "YOUTUBE",
                    icon = painterResource(id = R.drawable.ic_youtube),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/@amarantkz"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(70.dp)
                        .padding(bottom = 12.dp)
                )
            }
        }
    }
}

@Composable
fun ExternalLinkButton(
    text: String,
    icon: androidx.compose.ui.graphics.painter.Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = icon,
                contentDescription = text,
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun UserInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}
