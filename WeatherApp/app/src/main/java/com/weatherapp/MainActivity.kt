package com.weatherapp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.util.Consumer
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.weatherapp.api.WeatherService
import com.weatherapp.db.fb.FBAuth
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.db.local.LocalDB
import com.weatherapp.model.MainViewModel
import com.weatherapp.model.MainViewModelFactory
import com.weatherapp.monitor.ForecastMonitor
import com.weatherapp.repo.Repository
import com.weatherapp.ui.CityDialog
import com.weatherapp.ui.nav.BottomNavBar
import com.weatherapp.ui.nav.BottomNavItem
import com.weatherapp.ui.nav.MainNavHost
import com.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel : MainViewModel by viewModels()
        setContent {
            val fbAuh = remember { FBAuth() }
            if (fbAuh.currentUser == null) finish()
            val email = fbAuh.currentUser?.email
            val fbDB = remember { FBDatabase () }
            val localDB = remember {LocalDB(this, databaseName = "$email.db")}
            val service = remember { WeatherService() }
            val repo = remember { Repository(fbDB, localDB, service) }
            val monitor = remember { ForecastMonitor(this, repo) }
            val viewModel : MainViewModel by viewModels {
                MainViewModelFactory(repo, service)
            }
            val context = LocalContext.current
            DisposableEffect(Unit) {
                val listener = Consumer<Intent> { intent ->
                    val name = intent.getStringExtra("city")
                    viewModel.city = name
                }
                addOnNewIntentListener(listener)
                onDispose { removeOnNewIntentListener(listener) }
            }
            val navController = rememberNavController()
            var showDialog by remember { mutableStateOf(false) }
            val currentRoute = navController.currentBackStackEntryAsState()
            val showButton = currentRoute.value?.destination?.route !=
                    BottomNavItem.MapPage.route
            val launcher = rememberLauncherForActivityResult(contract =
                ActivityResultContracts.RequestPermission(), onResult = {} )
            WeatherAppTheme {
                if (showDialog) CityDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = { cityName ->
                        if (cityName.isNotBlank())
                            repo.addCity(name = cityName)
                        showDialog = false
                    })
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Bem-vindo/a ${viewModel.user?.name}") },
                            actions = {
                                IconButton( onClick = {
                                    Firebase.auth.signOut()
                                } ) {
                                    Icon(
                                        imageVector = Icons.Filled.ExitToApp,
                                        contentDescription = "Localized description"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavBar(navController = navController)
                    },
                    floatingActionButton = {
                        if (showButton) {
                            FloatingActionButton(onClick = { showDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Adicionar")
                            }
                        }
                    }
                ) {
                        innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        MainNavHost(navController = navController, viewModel = viewModel, context = context, repo = repo)
                    }
                }
            }
        }
    }
}
