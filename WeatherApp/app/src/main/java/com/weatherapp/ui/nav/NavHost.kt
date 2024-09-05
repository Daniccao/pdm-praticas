package com.weatherapp.ui.nav

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weatherapp.model.MainViewModel
import com.weatherapp.ui.HomePage
import com.weatherapp.ui.ListPage
import com.weatherapp.ui.MapPage

@Composable
fun MainNavHost(navController: NavHostController, viewModel: MainViewModel, context: Context) {
    NavHost(navController, startDestination = BottomNavItem.HomePage.route) {
        // composable (route = NOME DESTA DESTINAÇÃO) { UI DA DESTINAÇÃO }
        composable(route = BottomNavItem.HomePage.route) {
            HomePage(viewModel = viewModel, context= context)
        }
        composable(route = BottomNavItem.ListPage.route) {
            ListPage(viewModel = viewModel, context= context)
        }
        composable(route = BottomNavItem.MapPage.route) {
            MapPage(viewModel = viewModel, context= context)
        }
    }
}
