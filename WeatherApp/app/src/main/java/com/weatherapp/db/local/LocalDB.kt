package com.weatherapp.db.local

import android.content.Context
import androidx.room.Room
import com.weatherapp.model.City
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocalDB (context : Context, databaseName : String) {
    private var roomDB : LocalCityDatabase = Room.databaseBuilder(
        context = context, klass = LocalCityDatabase::class.java,
        name = databaseName
    ).build()
    val cities = roomDB.localCityDao().getCities()
    suspend fun insert(city: City) = withContext(Dispatchers.IO) {
        roomDB.localCityDao().upsert(city.toLocalCity())
    }

    suspend fun update(city: City) = withContext(Dispatchers.IO) {
        roomDB.localCityDao().upsert(city.toLocalCity())
    }
    suspend fun delete(city: City) = withContext(Dispatchers.IO) {
        roomDB.localCityDao().delete(city.toLocalCity())
    }
}
