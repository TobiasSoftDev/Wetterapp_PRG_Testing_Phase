import javafx.collections.FXCollections
import javafx.collections.ObservableList

import java.io.File

import kotlin.String

class Manager() : Logic {
    private val apiHandler: Api = ApiHandler()
    private var fetchedWeather: Weather? = null
    private var fetchedLocations: MutableList<Location> = mutableListOf()
//    private val weatherCodes: WeatherCode = WeatherCode.SONNIG
    private var hourlyWeather: MutableList<WeatherData> = mutableListOf()
    //private var favoritesList: MutableList<Favorite> = mutableListOf()
    private val favoritesList: ObservableList<Favorite> = FXCollections.observableArrayList()
    private val fileHandler: Storabledata = WeatherData()


    //    fun getCurrentWeather(location: Location): List<Any> {
//        fetchedWeather = apiHandler.fetchWeather(location)
//        return fetchedWeather!!.getCurrentWeatherDataAll()
//    }
    /*init {
        val savedFavorites = fileHandler.getAllFavorites()
        favoritesList.addAll(savedFavorites)
        println("Favoriten aus XML-File geladen")
    } */
    init {
        val loadedFavorites = fileHandler.getAllFavorites()
        favoritesList.setAll(loadedFavorites)

        val currentFavorites = loadedFavorites.map { oldFav ->
           val freshFavorites = apiHandler.fetchWeather(oldFav.location)

               // val currentWeather = getCurrentWeather(oldFav.location)
                if (freshFavorites != null){
                    Favorite(
                        oldFav.location,
                        oldFav.name,
                        freshFavorites.getTemperature(),
                        freshFavorites.getWeatherCode().iconName,
                    )
            }else{
            oldFav
        }
        }
        updateFavoriteFile()
        println("es wird die Favoritenliste aktualisiert nach dem neustart")
        favoritesList.setAll(currentFavorites)
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
            val favorite = Favorite(
                location,
                location.name,
                weather.getTemperature(),
                weather.getWeatherCode().iconName)

            val success = favoritesList.add(favorite)

            if (success) {
                fileHandler.storeFavorites(favorite)
                println("Favorite persistent gespeichert")
            }
            return success
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

    private fun updateFavoriteFile() {
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



//    fun storeWeatherData(weather: Weather) : MutableList<Location> {
//        hourlyWeather = fetchedWeather.getHourlyWeatherDataAll()
//        return hourlyWeather
//    }

  //  private fun storeWeather() {
  //  }

   // private fun storeLocation() {
   // }

    // }
