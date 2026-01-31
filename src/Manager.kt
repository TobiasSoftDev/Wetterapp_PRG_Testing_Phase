class Manager() : Logic {
    private val apiHandler: Api = ApiHandler()
    private var fetchedWeather: Weather? = null
    private var fetchedLocations: MutableList<Location> = mutableListOf()
//    private val weatherCodes: WeatherCode = WeatherCode.SONNIG
    private var hourlyWeather: MutableList<WeatherList> = mutableListOf()

//
//    fun getCurrentWeather(location: Location): List<Any> {
//        fetchedWeather = apiHandler.fetchWeather(location)
//        return fetchedWeather!!.getCurrentWeatherDataAll()
//    }

    fun getHourlyWeather(location: Location): List<Any> {
        fetchedWeather = apiHandler.fetchWeather(location)
        return fetchedWeather!!.getHourlyWeatherDataAll()
    }

    fun getDailyWeather(location: Location): List<Any> {
        fetchedWeather = apiHandler.fetchWeather(location)
        return fetchedWeather!!.getDailyWeatherDataAll()
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
