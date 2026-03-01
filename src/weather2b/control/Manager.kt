/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        P.Koch, P.Theiler und T.Graf

  Beschreibung: Zentrale Klasse welche alle anderen miteinander verbindet
 */

package weather2b.control

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import weather2b.data.sourcedata.Location
import weather2b.data.sourcedata.Weather
import weather2b.data.api.Api
import weather2b.data.api.ApiHandler
import weather2b.data.Storabledata
import weather2b.data.WeatherData
import weather2b.gui.Guilogic
import weather2b.data.favorites.Favorite
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

class Manager() : Guilogic {
    private val apiHandler: Api = ApiHandler()
    private var fetchedWeather: Weather? = null
    private var fetchedLocations: MutableList<Location> = mutableListOf()
    private val favoritesList: ObservableList<Favorite> = FXCollections.observableArrayList()
    private val fileHandler: Storabledata = WeatherData()

    init {
        refreshFavorites()
    }

    private val weatherCodeCategories = listOf(
        listOf(0, 100), // klar
        (1..3).toList() + (101..103).toList(), // bewölkt
        (4..39).toList() + (104..139).toList(), // windig, diverse Wetterphänomene
        (40..49).toList() + (140..149).toList(), // nebelig
        (50..59).toList()  + (150..159).toList(), // leichter Regen
        (60..69).toList() + (160..169).toList() + (70..79).toList() + (80..82).toList() + (83..90).toList() + (170..179).toList() + (180..182).toList() + (183..190).toList(), // leichter Regenschauer, Regen oder Schnee
        (91..99).toList() + (191..199).toList() // Gewitter
    )

    private fun weatherCodeScore(forecasted: Int, actual: Int): Double {
        if (forecasted == actual) return 100.0
        val forecastedCategory = weatherCodeCategories.indexOfFirst { forecasted in it }
//        /* Hier kann man es auf Wunsch aktivieren um den weather2b.data.sourcedata.Weather Code Forecast auf der Console auszugeben */
//        println("WC-Prognosewert: $forecasted (ProgKategorie: $forecastedCategory)")
        val actualCategory = weatherCodeCategories.indexOfFirst { actual in it }
//        /* Hier kann man es auf Wunsch aktivieren um den aktuellen weather2b.data.sourcedata.Weather Code auf der Console auszugeben */
//        println("WC-aktueller Wert: $actual (Kategorie: $actualCategory)")

        return when {
            forecastedCategory == -1 || actualCategory == -1 -> 0.0
            forecastedCategory == actualCategory -> 100.0
            abs(forecastedCategory - actualCategory) == 1 -> 95.0
            abs(forecastedCategory - actualCategory) in 2..3 -> 90.0
            abs(forecastedCategory - actualCategory) == 4 -> 80.0
            abs(forecastedCategory - actualCategory) == 5 -> 40.0
            abs(forecastedCategory - actualCategory) == 6 -> 5.0
            else -> 0.0     // in Kategorien-Berechnung ist etwas falsch gelaufen.
        }
    }

    override fun checkAccuracy(id: Int, currentWeather: Weather): Double? {
            val scores = mutableListOf<Double>()
            val storedWeatherEntries = fileHandler.getEntriesForLocation(id)
            val startOfCurrentDay = LocalDate.now().atStartOfDay()
            val currentHour = LocalDateTime.now().hour
            if (checkForFavorites(id)) {
                for (past in storedWeatherEntries) {
                    val pastTime = LocalDateTime.parse(past.timestamp).toLocalDate().atStartOfDay()

                    val hoursAhead = ChronoUnit.HOURS.between(pastTime, startOfCurrentDay)

                    // Schutzbedingung: Prüfung ob Stunden in Range liegen. Die erste Messung muss mindestens 24 Stunden alt sein.
                    if (hoursAhead !in 24..<past.hourlyForecasts.size) continue
                    // Prognose-Wertepaare für diese Stunde holen
                    val forecast = past.hourlyForecasts[currentHour]
                    // Temperaturdifferenz ab 10º C => 0% Güte!
                    val limit = 10.0
                    val error = abs(forecast.wrapperTemperature - currentWeather.getTemperature())
//                    /* Hier kann man es auf Wunsch aktivieren zu Diagnosezwecken */
//                    println("Temp-Prognose: $forecast, an Stelle: $currentHour in ${past.id} -> $error")
//                    println("Temp-aktuell: ${currentWeather.getTemperature()}")
                    // Prozentsatz der Temperaturgenauigkeit
                    val temperatureScore = ((1-(error/limit)).coerceIn(0.0, 1.0) * 100)
//                    /* Hier kann man es auf Wunsch aktivieren zu Diagnosezwecken */
//                    println("TempScore: ${temperatureScore}")
                    val weatherCodesScore = weatherCodeScore(forecast.wrapperWeatherCode, currentWeather.getWeatherCode().code)
//                    /* Hier kann man es auf Wunsch aktivieren zu Diagnosezwecken */
//                    println("WeatherScore: ${weatherCodesScore}")
                    // Durchschnitt beider Prozentsätze
                    scores.add((temperatureScore + weatherCodesScore) / 2)
//                    /* Hier kann man es auf Wunsch aktivieren zu Diagnosezwecken */
//                    println("Güte: ${scores.average()}")

                    println("Zeit seit der Letzten Abspeicherung: ${pastTime}")
                    println("Stunden vor Messung: ${hoursAhead}")
                }
            }
            return if (scores.isEmpty()) null else "%.2f".format(scores.average()).toDouble()
    }

    fun refreshFavorites(){
        val savedFavorites = fileHandler.getAllFavorites()
        val freshFavorites = savedFavorites.take(5).map { fav ->
            val weather = apiHandler.fetchWeather(fav.location)
            if (weather != null) {
                fav.temperature = weather.getTemperature()
                fav.iconFileName = weather.getWeatherCode().iconName
            }
            fav
        }
        favoritesList.setAll(freshFavorites)

        if (savedFavorites.size >5){
            updateFavoriteFile()
        }
    }

    override fun getLocations(searchText: String): MutableList<Location> {
        try {
            fetchedLocations = apiHandler.getLocations(searchText)
            return fetchedLocations
        } catch (e: NullPointerException) {
            System.err.println("Die Ortsabfrage aus dem weather2b.Manager hat nicht funktioniert: ${e.message}")
            e.printStackTrace()
            return mutableListOf()
        }

    }

    override fun getCurrentWeather(location: Location): Weather? {
        return try {
            fetchedWeather = apiHandler.fetchWeather(location)
            val weather = fetchedWeather ?: return null
            fileHandler.storeWeatherData(weather)
            return weather
        } catch (e: NullPointerException) {
            e.printStackTrace()
            System.err.println("Die Wetterabfrage aus dem weather2b.Manager hat nicht funktioniert: ${e.message}")
            null
        }
    }

    override fun getFavoritesObservableList(): ObservableList<Favorite> = favoritesList

    override fun addFavorites(location: Location, weather: Weather): Boolean {

        if (favoritesList.size < 5 && !checkForFavorites(location.id)) {
            val favorite = Favorite(location, location.name).apply {
                this.temperature = weather.getTemperature()
                this.iconFileName = weather.getWeatherCode().iconName
            }
            favoritesList.add(0,favorite)
            fileHandler.storeFavorites(favorite)
            fileHandler.storeWeatherData(weather)
            updateFavoriteFile()
            return true
        }
        return false
    }

    override fun removeFavorites(location: Location): Boolean {
        val removeFavorite = favoritesList.removeIf { it.location.id == location.id }
        if (removeFavorite) {
            updateFavoriteFile()
            fileHandler.removeStoredWeatherData(location)
            println("Favorit wurde entfernt und XMl Datei aktualisiert")
        }
        return removeFavorite
    }

    override fun updateFavoriteFile() {
        val userHome = System.getProperty("user.home")
        val file = File(userHome, ".Weather2b/storage/favoritesList.xml")
        if (file.exists()) {
            file.delete()
            favoritesList.forEach { fileHandler.storeFavorites(it) }
        }
    }

    override fun checkForFavorites(location: Int): Boolean {
        return favoritesList.any {
            it.location.id == location
        }
    }

    override fun updateALLFavorites() {
        favoritesList.forEachIndexed { index, fav ->
            val freshWeather = apiHandler.fetchWeather(fav.location)
            if (freshWeather != null){
                fav.temperature = freshWeather.getTemperature()
                fav.iconFileName = freshWeather.getWeatherCode().iconName
                favoritesList[index] = fav
            }
        }
    }
}