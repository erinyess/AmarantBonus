// MainScreen.kt
package com.example.amarantbonuscompose

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

// Функция для генерации QR-кода
fun generateQrCode(content: String): Bitmap {
    val size = 512
    val hints = mapOf(EncodeHintType.MARGIN to 1)
    val bitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
    return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).apply {
        for (x in 0 until size) {
            for (y in 0 until size) {
                setPixel(
                    x,
                    y,
                    if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, phoneNumber: String, initialBonusPoints: Int) {
    var currentLanguage by remember { mutableStateOf("RUS") }
    var selectedItem by remember { mutableStateOf("BONUS") }

    // Переменная состояния для бонусных баллов
    var bonusPoints by remember { mutableStateOf(initialBonusPoints) }

    // Переменная состояния для таймера
    var timerSeconds by remember { mutableStateOf(0) }

    // Переменная состояния для кнопки "Обновить"
    var isRefreshButtonEnabled by remember { mutableStateOf(true) }

    // Получаем корутинный скоуп
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    // Функция для мультиязычного текста
    fun getText(russian: String, kazakh: String): String {
        return if (currentLanguage == "RUS") russian else kazakh
    }


    // Функция для получения бонусов с сервера
    suspend fun fetchBonusPoints(tel: String) {
        val url = "http://2.135.218.2/ut_api/hs/api/get_bonuses/?tel=$tel"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).get().build()

        try {
            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }

            // Получаем тело ответа
            val responseBody = response.body?.string()

            // Логируем полный ответ
            Log.d(
                "MainScreen",
                "Server response: $responseBody"
            )  // Это выведет полный ответ в Logcat

            // Если ответ не пустой
            if (!responseBody.isNullOrEmpty()) {
                try {
                    // Парсим JSON
                    val jsonObject = JSONObject(responseBody)
                    val fetchedBonusPoints = jsonObject.optInt("bonusi", 0) // Извлекаем бонусы
                    Log.d(
                        "MainScreen",
                        "Fetched bonus points: $fetchedBonusPoints"
                    ) // Логируем количество бонусов

                    // Обновляем состояние бонусов на главном потоке
                    withContext(Dispatchers.Main) {
                        bonusPoints = fetchedBonusPoints
                    }
                } catch (e: Exception) {
                    // Логируем ошибку, если парсинг JSON не удался
                    Log.e("MainScreen", "Ошибка парсинга JSON: ${e.message}")
                }
            } else {
                Log.e("MainScreen", "Empty response body")  // Логируем, если тело ответа пустое
            }
        } catch (e: IOException) {
            Log.e("MainScreen", "Ошибка сети: ${e.message}")  // Логируем ошибки сети
        } catch (e: Exception) {
            Log.e("MainScreen", "Ошибка: ${e.message}")  // Логируем другие ошибки
        }
    }


    // Запускаем таймер после нажатия кнопки "Обновить"
    LaunchedEffect(timerSeconds) {
        if (timerSeconds > 0) {
            delay(1000L)
            timerSeconds--
            if (timerSeconds == 0) {
                isRefreshButtonEnabled = true
            }
        }
    }

    // Загружаем бонусы при первом запуске экрана
    LaunchedEffect(Unit) {
        fetchBonusPoints(phoneNumber)
    }

    // Градиент для надписи "БОНУСНЫЕ БАЛЛЫ"
    val bonusTitleGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFFF69B4), Color.Black, Color.Black, Color(0xFFFF69B4))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                // Исходный градиент фона
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFD84386), Color(0xFF050206), Color(0xFF040008)),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            )
    ) {
        // Кнопка переключения языка
        Button(
            onClick = { currentLanguage = if (currentLanguage == "RUS") "KAZ" else "RUS" },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
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

        // Основной контент
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, bottom = 130.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовок с розовым и чёрным градиентом
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .background(
                        brush = bonusTitleGradient,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = getText("БОНУСНЫЕ БАЛЛЫ", "БОНУС БАЛДАРЫ"),
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Yellow
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Карточка с бонусами и кнопкой обновления
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color(0xFF757575), shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Логотип и текст "BONUS" под ним
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.new_image), // Замените на ваш логотип
                        contentDescription = "Логотип",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "BONUS",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }

                // Отображение фактического количества бонусов пользователя
                Text(
                    text = "$bonusPoints",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Кнопка обновления бонусов
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable(enabled = isRefreshButtonEnabled) {
                            // Обновляем бонусы при нажатии
                            coroutineScope.launch {
                                fetchBonusPoints(phoneNumber)
                                isRefreshButtonEnabled = false
                                timerSeconds = 180 // 3 минуты = 180 секунд
                            }
                        }
                ) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = "Обновить",
                        tint = if (isRefreshButtonEnabled) Color.White else Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                    if (isRefreshButtonEnabled) {
                        Text(
                            text = getText("Обновить", "Жаңарту"),
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "${timerSeconds / 60}:${
                                (timerSeconds % 60).toString().padStart(2, '0')
                            }",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Текст с инструкцией для QR-кода
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = getText("Покажите QR-код", "QR кодын көрсетіңіз"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = getText("консультанту", "кеңесшіге"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // QR-код на основе номера телефона
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                val qrCodeBitmap = generateQrCode(phoneNumber)
                Image(
                    bitmap = qrCodeBitmap.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier = Modifier.size(180.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Номер телефона
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = getText("Ваш номер телефона:", "Сіздің телефон нөміріңіз:"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "+7$phoneNumber",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // Нижняя панель навигации
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Розовая панель с нижней навигацией
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Уменьшаем ширину панели
                    .height(70.dp) // Высота панели
                    .clip(RoundedCornerShape(35.dp)) // Овальная форма панели
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF800080), Color(0xFFBB2F72)) // Градиент фона
                        )
                    )
                    .align(Alignment.BottomCenter) // Панель будет привязана к низу экрана с отступом
            ) {
                // Содержимое панели: иконки и текст
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly, // Равномерное распределение кнопок
                    verticalAlignment = Alignment.CenterVertically // Центрируем иконки и текст по вертикали
                ) {
                    // Кнопка AMARANT.KZ
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center, // Центрируем элементы по вертикали
                        modifier = Modifier.clickable {
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amarant.kz"))
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_website), // Замените на ваш ресурс
                            contentDescription = "AMARANT.KZ",
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterHorizontally), // Центрируем иконку
                            tint = Color.White
                        )
                        Text(
                            "AMARANT.KZ",
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally) // Центрируем текст
                        )
                    }

                    // Кнопка "Профиль"
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center, // Центрируем иконку и текст по вертикали
                        modifier = Modifier.clickable {
                            navController.navigate("profile_screen/$phoneNumber") {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    ) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "Профиль",
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterHorizontally), // Центрируем иконку
                            tint = Color.White
                        )
                        Text(
                            getText("Профиль", "Профиль"),
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally) // Центрируем текст
                        )
                    }

                    // Кнопка "Настройки"
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center, // Центрируем иконку и текст по вертикали
                        modifier = Modifier.clickable {
                            navController.navigate("settings_screen/$phoneNumber") {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    ) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Настройки",
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterHorizontally), // Центрируем иконку
                            tint = Color.White
                        )
                        Text(
                            getText("Настройки", "Параметрлер"),
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally) // Центрируем текст
                        )

                    }
                }
            }
        }
    }
}








