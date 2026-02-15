import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.image.Image
import kotlin.String

class Manager() : Logic {
    private val apiHandler: Api = ApiHandler()
    private var fetchedWeather: Weather? = null
    private var fetchedLocations: MutableList<Location> = mutableListOf()
    private val favoritesList: ObservableList<Favorite> = FXCollections.observableArrayList()


    //    fun getCurrentWeather(location: Location): List<Any> {
//        fetchedWeather = apiHandler.fetchWeather(location)
//        return fetchedWeather!!.getCurrentWeatherDataAll()
//    }
    override fun getLocations(searchText: String): MutableList<Location> {
        fetchedLocations = apiHandler.getLocations(searchText)
        return fetchedLocations
    }

    override fun getCurrentWeather(from: Location): Weather? {
        fetchedWeather = apiHandler.fetchWeather(from)
        return fetchedWeather
    }


    override fun getFavoritesObservableList(): ObservableList<Favorite> = favoritesList


    override fun addFavorites(location: Location, weather: Weather): Boolean {
        if (favoritesList.size < 5 && !checkForFavorites(location)) {
            val favorite =
                Favorite(location, location.getName(), weather.getTemperature(), weather.getWeatherCode().icon)
            println("Ort gespeichert!")
            return favoritesList.add(favorite)
        }
        return false
    }

    override fun removeFavorites(location: Location): Boolean {
        val removeFavorites = favoritesList.removeIf { it.location.getLocationID() == location.getLocationID() }
        if (removeFavorites) {
            println("Favorit wurde entfernt")
        }
        return removeFavorites
    }


    override fun checkForFavorites(location: Location): Boolean {
        return favoritesList.any {
            it.location.getLocationID() == location.getLocationID()
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
