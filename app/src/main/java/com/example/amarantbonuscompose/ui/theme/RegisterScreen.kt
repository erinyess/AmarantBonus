package com.example.amarantbonuscompose
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.*
import androidx.compose.foundation.shape.RoundedCornerShape
import java.text.SimpleDateFormat
import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.ui.tooling.preview.Preview



@Composable
fun RegisterScreen(navController: NavHostController) {
    var currentLanguage by remember { mutableStateOf("RUS") }

    fun getText(russian: String, kazakh: String): String {
        return if (currentLanguage == "RUS") russian else kazakh
    }

    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf(getText("Алматы", "Алматы")) }

    // Расширенный список городов Казахстана
    val cities = listOf(
        getText("Алматы", "Алматы"),
        getText("Нур-Султан", "Нұр-Сұлтан"),
        getText("Шымкент", "Шымкент"),
        getText("Атырау", "Атырау"),
        getText("Актобе", "Ақтөбе"),
        getText("Караганда", "Қарағанды"),
        getText("Тараз", "Тараз"),
        getText("Павлодар", "Павлодар"),
        getText("Усть-Каменогорск", "Өскемен"),
        getText("Семей", "Семей"),
        getText("Костанай", "Қостанай"),
        getText("Кызылорда", "Қызылорда"),
        getText("Уральск", "Орал"),
        getText("Петропавловск", "Петропавл"),
        getText("Темиртау", "Теміртау"),
        getText("Кокшетау", "Көкшетау"),
        getText("Талдыкорган", "Талдықорған"),
        getText("Экибастуз", "Екібастұз"),
        getText("Рудный", "Рудный"),
        getText("Жезказган", "Жезқазған")
        // Добавьте остальные города по необходимости
    )

    // Для выбора даты рождения
    var birthDate by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Для отображения DatePickerDialog
    var showDatePicker by remember { mutableStateOf(false) }

    // **Добавляем объявление expandedCity**
    var expandedCity by remember { mutableStateOf(false) }

    // Для переключения видимости пароля
    var passwordVisible by remember { mutableStateOf(false) }

    // Состояние прокрутки для вертикального скроллинга
    val scrollState = rememberScrollState()

    // Для отображения ошибки
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF800080),
                        Color(0xFF000000)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(500f, 1000f)
                )
            )
    ) {
        // Верхняя панель с переключателем языка
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Кнопка переключения языка
            Button(
                onClick = {
                    currentLanguage = if (currentLanguage == "RUS") "KAZ" else "RUS"
                },
                modifier = Modifier
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
        }

        // Основной контент
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовок
            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(getText("РЕГИСТРАЦИЯ ", "ТІРКЕЛУ "))
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("В ")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFFFFD700),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(getText("БОНУСНОЙ СИСТЕМЕ", "БОНУС ЖҮЙЕСІНДЕ"))
                }
            }

            Text(
                text = annotatedString,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF800080),
                                Color(0xFF000000)
                            )
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Поле "Фамилия Имя"
            Text(

                text = getText("Фамилия Имя", "Тегі Аты"),
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text(text = getText("Введите фамилию и имя", "Тегі мен атыңызды енгізіңіз"), color = Color.Gray) },
                modifier = Modifier
                    .width(250.dp)
                    .height(56.dp),
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray,
                    placeholderColor = Color.Gray,
                    textColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(12.dp))


            Spacer(modifier = Modifier.height(24.dp))


            // Поле "Номер телефона"
            Text(
                text = getText("Номер телефона", "Телефон нөмірі"),
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { newValue ->
                    // Оставляем только цифры и ограничиваем длину до 10 символов
                    val digitsOnly = newValue.filter { it.isDigit() }
                    if (digitsOnly.length <= 10) {
                        phoneNumber = digitsOnly
                    }
                },
                placeholder = { Text(text = "777 777 7777", color = Color.Gray) },
                leadingIcon = {
                    Text("+7", color = Color.Black, fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .width(250.dp)
                    .height(56.dp),
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray,
                    placeholderColor = Color.Gray,
                    textColor = Color.Black,
                    leadingIconColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Поле "Новый пароль"
            Text(
                text = getText("Новый пароль", "Жаңа құпия сөз"),
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = getText("Цифрами. Не более 6 знаков", "Санмен. 6 таңбадан аспауы керек"),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
            OutlinedTextField(
                value = password,
                onValueChange = { if (it.length <= 6) password = it },
                placeholder = { Text(text = "123456", color = Color.Gray) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null, tint = Color.Gray)
                    }
                },
                modifier = Modifier
                    .width(250.dp)
                    .height(56.dp),
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray,
                    placeholderColor = Color.Gray,
                    textColor = Color.Black,
                    trailingIconColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

// Поле "Дата рождения"
            Text(
                text = "Дата рождения",
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

// Обработчик для открытия выбора даты
            OutlinedTextField(
                value = birthDate,
                onValueChange = {}, // Значение не меняется напрямую, только для отображения
                readOnly = true, // Поле только для отображения
                placeholder = {
                    Text(
                        text = "Выберите дату (ГГГГ-ММ-ДД)",
                        color = Color.Gray
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Выбор даты",
                            tint = Color.Gray
                        )
                    }
                },
                modifier = Modifier
                    .width(250.dp)
                    .height(56.dp),
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Gray,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                )
            )

// Открытие прокручиваемого выбора даты
            if (showDatePicker) {
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val yearRange = 1900..currentYear
                val monthRange = 1..12
                val dayRange = 1..31

                // Состояния для выбранного года, месяца и дня
                var selectedYear by remember { mutableStateOf(currentYear) }
                var selectedMonth by remember { mutableStateOf(1) }
                var selectedDay by remember { mutableStateOf(1) }

                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .width(350.dp)
                        .padding(16.dp)
                        .wrapContentHeight()
                ) {
                    // Панель выбора для Года, Месяца и Дня
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Выбор года
                        Column(
                            modifier = Modifier.width(100.dp)
                        ) {
                            Text(text = "Год", color = Color.Black, fontWeight = FontWeight.Bold)
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 100.dp, max = 200.dp),
                                reverseLayout = true // Для прокрутки снизу вверх
                            ) {
                                items(yearRange.toList()) { year ->
                                    Text(
                                        text = "$year",
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable { selectedYear = year }
                                            .fillMaxWidth(),
                                        style = TextStyle(fontSize = 18.sp, color = if (selectedYear == year) Color.Blue else Color.Black)
                                    )
                                }
                            }
                        }

                        // Выбор месяца
                        Column(
                            modifier = Modifier.width(80.dp)
                        ) {
                            Text(text = "Месяц", color = Color.Black, fontWeight = FontWeight.Bold)
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 100.dp, max = 200.dp),
                                reverseLayout = true // Для прокрутки снизу вверх
                            ) {
                                items(monthRange.toList()) { month ->
                                    Text(
                                        text = month.toString().padStart(2, '0'),
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable { selectedMonth = month }
                                            .fillMaxWidth(),
                                        style = TextStyle(fontSize = 18.sp, color = if (selectedMonth == month) Color.Blue else Color.Black)
                                    )
                                }
                            }
                        }

                        // Выбор дня
                        Column(
                            modifier = Modifier.width(80.dp)
                        ) {
                            Text(text = "День", color = Color.Black, fontWeight = FontWeight.Bold)
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 100.dp, max = 200.dp),
                                reverseLayout = true // Для прокрутки снизу вверх
                            ) {
                                items(dayRange.toList()) { day ->
                                    Text(
                                        text = day.toString().padStart(2, '0'),
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable { selectedDay = day }
                                            .fillMaxWidth(),
                                        style = TextStyle(fontSize = 18.sp, color = if (selectedDay == day) Color.Blue else Color.Black)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Кнопка подтверждения выбора
                    Button(
                        onClick = {
                            // Формируем выбранную дату в формате ГГГГ-ММ-ДД
                            birthDate = "${selectedYear}-${selectedMonth.toString().padStart(2, '0')}-${selectedDay.toString().padStart(2, '0')}"
                            showDatePicker = false // Закрываем выбор даты
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Выбрать")
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))



            // Кнопка "Зарегистрироваться"
            Button(
                onClick = {
                    // Логика регистрации
                    coroutineScope.launch {
                        performRegistration(
                            name = name,
                            phoneNumber = phoneNumber,
                            password = password,
                            city = selectedCity,
                            dateOfBirth = birthDate,
                            navController = navController,
                            onError = { message: String ->
                                errorMessage = message
                                showErrorDialog = true
                            },
                            currentLanguage = currentLanguage
                        )
                    }
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .width(250.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9C27B0))
            ) {
                Text(
                    text = getText("Зарегистрироваться", "Тіркелу"),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Диалог ошибки
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = {
                    Text(text = getText("Ошибка", "Қате"))
                },
                text = {
                    Text(text = errorMessage)
                },
                confirmButton = {
                    Button(
                        onClick = { showErrorDialog = false }
                    ) {
                        Text(text = "OK")
                    }
                }
            )
        }
    }
}

suspend fun performRegistration(
    name: String,
    phoneNumber: String,
    password: String,
    city: String,
    dateOfBirth: String,  // Дата в формате "yyyy-MM-dd" передается сюда
    navController: NavHostController,
    onError: (String) -> Unit,
    currentLanguage: String
) {
    fun getText(russian: String, kazakh: String): String {
        return if (currentLanguage == "RUS") russian else kazakh
    }

    val url = "http://2.135.218.2/ut_api/hs/api/registration/"

    // Преобразование даты в формат "yyyyMMdd"
    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

    // Преобразуем дату, которую мы получаем (например, "2025-01-01") в нужный формат
    val formattedDate = try {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())  // Текущий формат входящей даты
        val parsedDate = inputDateFormat.parse(dateOfBirth) ?: throw IllegalArgumentException("Дата не может быть преобразована.")
        dateFormat.format(parsedDate)  // Преобразуем в формат "yyyyMMdd"
    } catch (e: Exception) {
        Log.e("RegisterUser", "Invalid Date Format: ${e.message}")
        withContext(Dispatchers.Main) {
            onError(getText("Неверный формат даты. Ожидается формат yyyyMMdd", "Қате күн форматы. Талап етілген формат yyyyMMdd"))
        }
        return
    }

    // Создание JSON для тела запроса
    val json = JSONObject().apply {
        put("tel", phoneNumber)
        put("name", name)
        put("password", password)
        put("dateOfBirth", formattedDate)  // Используем отформатированную дату
        put("city", city)
    }

    val mediaType = "application/json".toMediaTypeOrNull()
    val requestBody = json.toString().toRequestBody(mediaType)

    val client = OkHttpClient()

    // Создание запроса
    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    Log.d("RegisterUser", "JSON Data: $json")

    try {
        // Выполнение запроса в фоновом потоке
        val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }

        if (response.isSuccessful) {
            val responseBody = response.body?.string()
            Log.d("RegisterUser", "Response: $responseBody")

            // Проверка, зарегистрирован ли уже пользователь
            if (responseBody?.contains("Пользователь уже зарегистрирован") == true) {
                // Ошибка: пользователь уже существует
                withContext(Dispatchers.Main) {
                    onError(getText("Данный пользователь ранее зарегистрирован", "Бұл пайдаланушы бұрын тіркелген"))
                }
            } else {
                // Регистрация успешна
                withContext(Dispatchers.Main) {
                    navController.navigate("main_screen/$phoneNumber/0") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        } else {
            // Сервер вернул ошибку
            val errorMsg = response.body?.string() ?: "Ошибка сервера"
            Log.e("RegisterUser", "Error: $errorMsg")
            withContext(Dispatchers.Main) {
                onError(getText("Ошибка сервера: ${response.code}", "Сервер қатесі: ${response.code}"))
            }
        }
    } catch (e: Exception) {
        // Обработка исключений (например, проблема с сетью)
        Log.e("RegisterUser", "Exception: ${e.message}", e)
        withContext(Dispatchers.Main) {
            onError(getText("Ошибка сети: ${e.localizedMessage}", "Желі қатесі: ${e.localizedMessage}"))
        }
    }
}
