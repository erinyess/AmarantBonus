// NetworkUtils.kt

package com.example.amarantbonuscompose.network

import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull



suspend fun performRegistration(
    name: String,
    phoneNumber: String,
    password: String,
    city: String,
    birthDate: String,
    navController: NavHostController,
    onError: (String) -> Unit
) {
    val url = "http://2.135.218.2/ut_api/hs/api/get_bonuses/?tel="
    val client = OkHttpClient()


    val jsonObject = JSONObject().apply {
        put("name", name)
        put("phoneNumber", phoneNumber)
        put("password", password)
        put("city", city)
        put("birthDate", birthDate)
    }

    val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    try {
        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        if (response.isSuccessful) {
            withContext(Dispatchers.Main) {
                navController.navigate("main_screen/$phoneNumber")
            }
        } else {
            withContext(Dispatchers.Main) {
                onError("Ошибка сервера: ${response.code}")
            }
        }
    } catch (e: IOException) {
        withContext(Dispatchers.Main) {
            onError("Ошибка сети: ${e.message}")
        }
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            onError("Ошибка: ${e.message}")
        }
    }
}
