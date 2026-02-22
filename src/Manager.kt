import javafx.collections.FXCollections
import javafx.collections.ObservableList

import java.io.File

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.String
import kotlin.math.abs

class Manager() : Logic {
    private val apiHandler: Api = ApiHandler()
    private var fetchedWeather: Weather? = null
    private var fetchedLocations: MutableList<Location> = mutableListOf()
    private var hourlyWeather: MutableList<WeatherData> = mutableListOf()
    private val favoritesList: ObservableList<Favorite> = FXCollections.observableArrayList()
    private val fileHandler: Storabledata = WeatherData()

    private val weatherCodeCategories = listOf(
        listOf(0),                          // sonnig, klar
        listOf(1,2,3),                      // bewölkt
        listOf(45,48),                      // Nebel
        listOf(51,53,55,56,57),             // leichter Regen
        listOf(60,61,63,65,66,67,80,81,82), // Regen, Regenschauer
        listOf(71,73,75,77,85,86),          // Schnee, Schneeschauer
        listOf(95,96,99)                    // Gewitter
    )

    private fun weatherCodeScore(forecasted: Int, actual: Int): Double {
        if (forecasted == actual) return 100.0
        val forecastedCategory = weatherCodeCategories.indexOfFirst { forecasted in it }
        println("WC-Prognosewert: $forecasted (ProgKategorie: $forecastedCategory)")
        val actualCategory = weatherCodeCategories.indexOfFirst { actual in it }
        println("WC-aktueller Wert: $actual (Kategorie: $actualCategory)")

        return when {
            forecastedCategory == -1 || actualCategory == -1 -> 0.0
            forecastedCategory == actualCategory -> 100.0
            abs(forecastedCategory - actualCategory) == 1 -> 98.0
            abs(forecastedCategory - actualCategory) in 2..3 -> 95.0
            abs(forecastedCategory - actualCategory) > 3 -> 90.0
            else -> 0.0
        }
    }

    override fun checkAccuracy(id: Int, currentWeather: Weather): Double {
        val scores = mutableListOf<Double>()
        val storedWeatherEntries = fileHandler.getEntriesForLocation(id)
        val startOfCurrentDay = LocalDate.now().atStartOfDay()      // 0 Uhr des aktuellen Tages
        val currentHour = LocalDateTime.now().hour
        println("aktuelle Stunde: ${currentHour}")
        for (past in storedWeatherEntries) {
            val pastTime = LocalDateTime.parse(past.timestamp).toLocalDate().atStartOfDay()
            println("past time: ${pastTime}")
            val hoursAhead = ChronoUnit.HOURS.between(pastTime, startOfCurrentDay)
            println("Stunden vor Messung: ${hoursAhead}")
            // Schutzbedingung: Prüfung ob Stunden in Range liegen. Die erste Messung muss mindestens 24 Stunden alt sein.
            if (hoursAhead !in 24..<past.hourlyForecasts.size) continue
            // Prognose-Wertepaare für diese Stunde holen
            val forecast = past.hourlyForecasts[currentHour]
            // Temperaturdifferenz ab 2º C => 0% Güte!
            val limit = 2.0
            val error = abs(forecast.wrapperTemperature - currentWeather.getTemperature())
            println("Temp-Prognose: $forecast")
            println("Temp-aktuell: ${currentWeather.getTemperature()}")
            // Prozentsatz der Temperaturgenauigkeit
            val temperatureScore = ((1-(error-limit)).coerceIn(0.0, 1.0) * 100)
            println("TempScore: ${temperatureScore}")
            val weatherCodesScore = weatherCodeScore(forecast.wrapperWeatherCode, currentWeather.getWeatherCode().code)
            println("WeatherScore: ${weatherCodesScore}")
            // Durchschnitt beider Prozentsätze
            scores.add((temperatureScore + weatherCodesScore) / 2)

        }
        return if (scores.isEmpty()) 0.0 else scores.average()
    }

    //    fun getCurrentWeather(location: Location): List<Any> {
//        fetchedWeather = apiHandler.fetchWeather(location)
//        return fetchedWeather!!.getCurrentWeatherDataAll()
//    }
    init {
        refreshFavorites()
    }

    fun refreshFavorites(){
        val savedFavorites = fileHandler.getAllFavorites()
        val freshFavorites = savedFavorites.map { fav ->
            val weather = apiHandler.fetchWeather(fav.location)
            if (weather != null) {
                fav.temperature = weather.getTemperature()
                fav.iconFileName = weather.getWeatherCode().iconName
            }
            fav
        }
        println("es wird die Favoritenliste aktualisiert nach dem neustart")
        favoritesList.setAll(freshFavorites)
    }

    override fun getLocations(searchText: String): MutableList<Location> {
        fetchedLocations = apiHandler.getLocations(searchText)
        return fetchedLocations
    }

    override fun getCurrentWeather(location: Location): Weather? {
        fetchedWeather = apiHandler.fetchWeather(location)
        if (fetchedWeather != null){

            fileHandler.getWeatherHistoryFromLocation(location.id)

            // prüfen ob Ort in Favoriten? Wenn ja: Wetterabfrage im Speicher speichern.
            fileHandler.storeWeatherData(fetchedWeather)
        }

        return fetchedWeather
    }

    override fun getFavoritesObservableList(): ObservableList<Favorite> = favoritesList

    override fun addFavorites(location: Location, weather: Weather): Boolean {

        if (favoritesList.size < 5 && !checkForFavorites(location)) {
            val favorite = Favorite(location, location.name).apply {
                this.temperature = weather.getTemperature()
                this.iconFileName = weather.getWeatherCode().iconName
            }
            favoritesList.add(0,favorite)
            fileHandler.storeFavorites(favorite)
            updateFavoriteFile()

            return true
        }
        return false
    }

    override fun removeFavorites(location: Location): Boolean {
        val removeFavorite = favoritesList.removeIf { it.location.id == location.id }
        if (removeFavorite) {
            updateFavoriteFile()
            println("Favorit wurde entfernt und XMl Datei aktualisiert")
        }
        return removeFavorite
    }

    override fun updateFavoriteFile() {
        val userHome = System.getProperty("user.home")
        val file = File(userHome,".Weather2b/storage/favorites/favoritesList.xml")
        if (file.exists()) {
            file.delete()
            favoritesList.forEach { fileHandler.storeFavorites(it) }
        }
    }

    override fun checkForFavorites(location: Location): Boolean {
        return favoritesList.any {
            it.location.id == location.id
        }
    }
}
