package com.example.amarantbonuscompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SettingsScreen(navController: NavHostController, phoneNumber: String) {
    var currentLanguage by remember { mutableStateOf("RUS") }

    // Функция для мультиязычного текста
    fun getText(russian: String, kazakh: String): String {
        return if (currentLanguage == "RUS") russian else kazakh
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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

                // Заголовок
                Text(
                    text = getText("Настройки", "Параметрлер"),
                    color = Color(0xFFD8A657),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFFF69B4), Color.Black
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 70.dp, vertical = 12.dp)
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
                        androidx.compose.material.Text(
                            text = if (currentLanguage == "RUS") "ЯЗЫК" else "ТІЛ",
                            fontSize = 8.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                        androidx.compose.material.Text(
                            text = currentLanguage,
                            fontSize = 8.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))  // Отступ для того, чтобы кнопки настроек были ниже

            // Кнопки настроек
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Кнопка "Изменить пароль"
                SettingsButton(
                    text = getText("Изменить пароль", "Құпиясөзді өзгерту"),
                    icon = Icons.Default.Lock,
                    onClick = { navController.navigate("change_password_screen/$phoneNumber") }
                )

                // Кнопка "Удалить профиль"
                SettingsButton(
                    text = getText("Удалить профиль", "Профильді жою"),
                    icon = Icons.Default.Delete,
                    onClick = { navController.navigate("delete_profile_screen/$phoneNumber") }
                )

                // Кнопка "Обратная связь"
                SettingsButton(
                    text = getText("Обратная связь", "Кері байланыс"),
                    icon = Icons.Default.Feedback,
                    onClick = {
                        // Пример передачи email в навигацию
                        val feedbackEmail = "user@example.com"
                        navController.navigate("feedback_screen/$feedbackEmail")
                    }
                )

                // Кнопка "Выход из профиля"
                SettingsButton(
                    text = getText("Выход из профиля", "Профильден шығу"),
                    icon = Icons.Default.ExitToApp,
                    onClick = { navController.navigate("login_screen") }
                )
            }
        }
    }
}

@Composable
fun SettingsButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(250.dp)  // Уменьшаем ширину кнопок
            .padding(vertical = 8.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFA72A6F)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
