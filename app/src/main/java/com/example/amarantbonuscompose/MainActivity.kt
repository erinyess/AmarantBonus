package com.example.amarantbonuscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmarantApp()
        }
    }
}

@Composable
fun AmarantApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome_screen") {
        composable(route = "welcome_screen") { WelcomeScreen(navController) }

        // Навигация на главный экран с параметрами
        composable(
            route = "main_screen/{phoneNumber}/{bonusPoints}",
            arguments = listOf(
                navArgument("phoneNumber") { type = NavType.StringType },
                navArgument("bonusPoints") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            val bonusPoints = backStackEntry.arguments?.getInt("bonusPoints") ?: 0
            MainScreen(navController, phoneNumber, bonusPoints)
        }

        // Другие экраны
        composable(route = "register_screen") { RegisterScreen(navController) }
        composable(route = "login_screen") { LoginScreen(navController) }
        composable(
            route = "profile_screen/{phoneNumber}",
            arguments = listOf(
                navArgument("phoneNumber") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            ProfileScreen(navController, phoneNumber)
        }
        composable(
            route = "settings_screen/{phoneNumber}",
            arguments = listOf(
                navArgument("phoneNumber") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            SettingsScreen(navController, phoneNumber)
        }
        composable(
            route = "change_password_screen/{phoneNumber}",
            arguments = listOf(
                navArgument("phoneNumber") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            ChangePasswordScreen(navController, phoneNumber)
        }
        composable(
            route = "delete_profile_screen/{phoneNumber}",
            arguments = listOf(
                navArgument("phoneNumber") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            DeleteProfileScreen(navController, phoneNumber)
        }

        // Для экрана feedback_screen добавляем параметр email
        composable(
            route = "feedback_screen/{feedbackEmail}", // Указываем параметр feedbackEmail
            arguments = listOf(
                navArgument("feedbackEmail") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val feedbackEmail = backStackEntry.arguments?.getString("feedbackEmail") ?: ""
            // Передаем параметр feedbackEmail в FeedbackScreen
            FeedbackScreen(feedbackEmail = feedbackEmail, navController = navController)
        }
    }
}
