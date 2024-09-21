package com.weatherapp.repo

import com.google.android.gms.maps.model.LatLng
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.model.City
import com.weatherapp.model.User

class Repository (private var listener : Listener): FBDatabase.Listener {
    private var fbDb = FBDatabase (this)
    //private var weatherService = WeatherService()
    interface Listener {
        fun onUserLoaded(user: User)
        fun onCityAdded(city: City)
        fun onCityRemoved(city: City)
    }
    fun addCity(name: String) {
        fbDb.add(City(name, LatLng(0.0,0.0)))
    }
    fun addCity(lat: Double, lng: Double) {
        fbDb.add(City("Cidade@$lat:$lng", LatLng(lat,lng)))
    }
    fun remove(city: City) {
        fbDb.remove(city)
    }
    fun register(userName: String, email: String) {
        fbDb.register(User(userName, email))
    }
    override fun onUserLoaded(user: User) {
        listener.onUserLoaded(user)
    }
    override fun onCityAdded(city: City) {
        listener.onCityAdded(city)
    }
    override fun onCityRemoved(city: City) {
        listener.onCityRemoved(city)
    }
}
