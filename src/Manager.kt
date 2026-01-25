import java.time.LocalDateTime

class Manager(
    private val apiHandler: Api) {
    private var fetchedWeather: Weather? = null
    private var fetchedLocations: MutableList<Location> = mutableListOf()
//    private val weatherCodes: WeatherCode = WeatherCode.SONNIG
    private var hourlyWeather: MutableList<WeatherList> = mutableListOf()


    fun getCurrentWeather(location: Location): List<Any> {
        fetchedWeather = apiHandler.fetchWeather(location)
        return fetchedWeather!!.getCurrentWeatherDataAll()
    }

    fun getHourlyWeather(location: Location): List<Any> {
        fetchedWeather = apiHandler.fetchWeather(location)
        return fetchedWeather!!.getHourlyWeatherDataAll()
    }

    fun getDailyWeather(location: Location): List<Any> {
        fetchedWeather = apiHandler.fetchWeather(location)
        return fetchedWeather!!.getDailyWeatherDataAll()
    }

    fun fetchLocations(searchedLocation: String) : MutableList<Location> {
        fetchedLocations = apiHandler.fetchLocations(searchedLocation)
        return fetchedLocations
    }

    fun pickLocation(search: String, choice: Int): Location {
        val locations = fetchLocations(search)
        println(locations[choice])
        return locations[choice]
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
