package com.weatherapp.monitor

import android.app.NotificationManager
import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.weatherapp.model.City
import com.weatherapp.model.User
import com.weatherapp.repo.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ForecastMonitor (context: Context, private val repo : Repository ) {
    private val wm = WorkManager.getInstance(context)
    private val nm = context.getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager
    private var ioScope : CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val monitoring = mutableSetOf<String>()
    init {
        ioScope.launch {
            repo.cities.collect { cities ->
                cities.forEach { updateMonitor(it) }
            }
        }
    }
    private fun updateMonitor(city: City) {
        if (city.name in monitoring && !city.isMonitored) {
            monitoring.remove(city.name)
            cancelCity(city)
            return;
        }
        if (!city.isMonitored) return;
        monitoring.add(city.name)
        val inputData = Data.Builder().putString("city", city.name).build()
        val request = PeriodicWorkRequestBuilder<ForecastWorker>(
            repeatInterval = 15, repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).setInitialDelay(
            duration = 10, timeUnit = TimeUnit.SECONDS
        ).setInputData(inputData).build()
        wm.enqueueUniquePeriodicWork(city.name,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, request )
    }
    private fun cancelCity(city : City) {
        wm.cancelUniqueWork(city.name)
        nm.cancel(city.name.hashCode())
    }
//    private fun cancelAll() {
//        wm.cancelAllWork()
//        nm.cancelAll()
//    }
//    override fun onUserLoaded(user: User) { /* DO NOTHING */ }
//    override fun onUserSignOut() = cancelAll()
//    override fun onCityAdded(city: City) = updateMonitor(city)
//    override fun onCityRemoved(city: City) = cancelCity(city)
//    override fun onCityUpdated(city: City) = updateMonitor(city)
}
