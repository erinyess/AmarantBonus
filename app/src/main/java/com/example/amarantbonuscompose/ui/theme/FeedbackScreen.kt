package com.example.amarantbonuscompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController

@Composable
fun FeedbackScreen(feedbackEmail: String, navController: NavController) {
    var currentLanguage by remember { mutableStateOf("RUS") } // Переменная для текущего языка

    // Функция для мультиязычного текста
    val getText = { russian: String, kazakh: String ->
        if (currentLanguage == "RUS") russian else kazakh
    }

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
            // Верхняя панель с кнопкой "Назад"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Кнопка "Назад", которая будет вызывать возврат на предыдущий экран
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Заголовок экрана
                Text(
                    text = getText("Контактная информация", "Контакттық ақпарат"),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )

                // Кнопка переключения языка
                Button(
                    onClick = {
                        currentLanguage = if (currentLanguage == "RUS") "KAZ" else "RUS"
                    },
                    modifier = Modifier
                        .size(width = 30.dp, height = 40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)), // Используем containerColor
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
                Spacer(modifier = Modifier.height(32.dp))

            // Иконка письма
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = "Контакты",
                modifier = Modifier.size(80.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Текст с контактной информацией
            Text(
                text = getText(
                    "Контактная информация для связи с разработчиком:",
                    "Даму жасаушыға хабарласу үшін байланыс ақпараты:"
                ),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Жестко прописанный Email
            Text(
                text = getText("E-MAIL: HARZEEV.ALEKS@gmail.com", "E-MAIL: HARZEEV.ALEKS@gmail.com"),
                color = Color(0xFF03A9F4),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопка для перехода в главное меню
            Button(
                onClick = {
                    // Пример: переход на главный экран с передачей телефона и бонусных баллов
                    navController.navigate("main_screen/1234567890/100")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E44AD))
            ) {
                Text(
                    text = getText("Перейти в Главное меню", "Негізгі мәзірге өту"),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
