package com.weatherapp.api

import com.weatherapp.model.Weather

data class APICurrentWeather (
    var APILocation : APILocation? = null,
    var current : APIWeather? = null
)

fun APICurrentWeather.toWeather() : Weather {
    return Weather (
        date = current?.last_updated?:"...",
        desc = current?.condition?.text?:"...",
        temp = current?.temp_c?:-1.0,
        imgUrl = "https:" + current?.condition?.icon
    )
}
