// NavHost.kt
package com.example.amarantbonuscompose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login_screen") {
        composable("login_screen") {
            LoginScreen(navController)
        }
        composable(
            "main_screen/{phoneNumber}/{bonusPoints}",
            arguments = listOf(
                navArgument("phoneNumber") { type = NavType.StringType },
                navArgument("bonusPoints") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            val bonusPoints = backStackEntry.arguments?.getInt("bonusPoints") ?: 0
            MainScreen(navController, phoneNumber, bonusPoints)
        }
        composable("register_screen") {
            RegisterScreen(navController)
        }
        // Добавьте другие маршруты здесь
    }
}
