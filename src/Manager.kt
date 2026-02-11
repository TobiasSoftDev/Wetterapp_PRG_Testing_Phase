import javafx.scene.image.Image
import kotlin.String

class Manager() : Logic {
    private val apiHandler: Api = ApiHandler()
    private var fetchedWeather: Weather? = null
    private var fetchedLocations: MutableList<Location> = mutableListOf()
//    private val weatherCodes: WeatherCode = WeatherCode.SONNIG
    private var hourlyWeather: MutableList<WeatherData> = mutableListOf()
    private var favoritesList: MutableList<Favorite> = mutableListOf()

//    fun getCurrentWeather(location: Location): List<Any> {
//        fetchedWeather = apiHandler.fetchWeather(location)
//        return fetchedWeather!!.getCurrentWeatherDataAll()
//    }

    fun addFavorites(location: Location, weather: Weather): Boolean {
        if (!checkForFavorites(location)) {
            val favorite = Favorite(location, location.getName(), weather.getTemperature(), Image(""))
            favoritesList.add(favorite)
            println("Ort gespeichert!")
            return true
        }
        println("Ort bereits vorhanden!")
        return false
    }

    private fun checkForFavorites(location: Location): Boolean {
        for (fav in favoritesList){
            if (location.getLocationID() == fav.location.getLocationID()) {
                return true
            }
        }
        return false
    }

    override fun getLocations(searchText: String): MutableList<Location> {
        fetchedLocations = apiHandler.getLocations(searchText)
        return fetchedLocations
    }

    override fun getCurrentWeather(from: Location): Weather? {
        fetchedWeather = apiHandler.fetchWeather(from)
        return fetchedWeather
    }

//    fun storeWeatherData(weather: Weather) : MutableList<Location> {
//        hourlyWeather = fetchedWeather.getHourlyWeatherDataAll()
//        return hourlyWeather
//    }

    private fun storeWeather() {
    }

    private fun storeLocation() {
    }

    }
