import javafx.collections.FXCollections
import javafx.collections.ObservableList

import java.io.File

import kotlin.String

class Manager() : Logic {
    private val apiHandler: Api = ApiHandler()
    private var fetchedWeather: Weather? = null
    private var fetchedLocations: MutableList<Location> = mutableListOf()
    private var hourlyWeather: MutableList<WeatherData> = mutableListOf()
    private val favoritesList: ObservableList<Favorite> = FXCollections.observableArrayList()
    private val fileHandler: Storabledata = WeatherData()

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

            //fileHandler.getHistoryForLocation(from.getLocationID().toInt())

            // prüfen ob Ort in Favoriten? Wenn ja: Wetterabfrage im Speicher speichern.
            fileHandler.storeData(fetchedWeather)
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
