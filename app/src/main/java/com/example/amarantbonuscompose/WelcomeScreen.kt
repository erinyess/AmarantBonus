// WelcomeScreen.kt
package com.example.amarantbonuscompose

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.clip

@Composable
fun WelcomeScreen(navController: NavHostController) {
    var currentLanguage by remember { mutableStateOf("RUS") }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Проверяем, есть ли сохраненный номер телефона
    val savedPhoneNumber = sharedPreferences.getString("phoneNumber", null)
    val savedBonusPoints = sharedPreferences.getInt("bonusPoints", 0)

    fun getText(russian: String, kazakh: String): String {
        return if (currentLanguage == "RUS") russian else kazakh
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFD5006D),
                        Color(0xFF050206),
                        Color(0xFF050206),
                        Color(0xFF7438C3)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            )
    ) {
        // Кнопка смены языка
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    currentLanguage = if (currentLanguage == "RUS") "KAZ" else "RUS"
                },
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
                        text = "ЯЗЫК",
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Логотип
            Image(
                painter = painterResource(id = R.drawable.new_image), // Замените на ваш ресурс или используйте Icon
                contentDescription = "Логотип",
                modifier = Modifier
                    .size(220.dp)
                    .offset(y = (-20).dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Заголовок
            Text(
                text = getText("Добро пожаловать в", "AMARANT Bonus-қа қош келдіңіз"),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = "AMARANT Bonus",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Описание
            Text(
                text = getText(
                    "Это приложение для накопления и  \n списания бонусных баллов в сети \n Гастромаркетов AMARANT",
                    "Бұл AMARANT Гастромаркеттер \n желісінде бонустық ұпайларды жинауға \n және есептен шығаруға арналған  қосымша"
                ),
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = getText(
                    "Накапливайте 3% с каждой покупки в \n виде бонусных баллов. \n Баллы можно использовать в \n виде скидки на приобретаемый \n товар. \n Максимальный размер скидки 30% от стоимости товара.",
                    "Әр сатып алудан 3% бонустық ұпай \n түрінде жинаңыз. Ұпайларды сіз \n сатып алатын затқа жеңілдік ретінде \n жұмсай аласыз. Максималды \n жеңілдік мөлшері тауар құнынан 30% \n құрайды."
                ),
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    if (savedPhoneNumber != null) {
                        // Если пользователь уже авторизован, переходим на главный экран
                        navController.navigate("main_screen/$savedPhoneNumber/$savedBonusPoints") {
                            popUpTo("welcome_screen") { inclusive = true }
                        }
                    } else {
                        // Иначе переходим на экран входа
                        navController.navigate("login_screen")
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier
                    .width(180.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF9C27B0),
                                Color(0xFFCE26B4)
                            )
                        )
                    )
            ) {
                Text(
                    text = getText("ДАЛЕЕ", "КЕЛЕСІ"),
                    color = Color.White,
                    fontSize = 18.sp,
                    maxLines = 1
                )
            }
        }
    }
}
