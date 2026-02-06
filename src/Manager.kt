class Manager() : Logic {
    private val apiHandler: Api = ApiHandler()
    private var fetchedWeather: Weather? = null
    private var fetchedLocations: MutableList<Location> = mutableListOf()
    private var favoritesList: MutableList<Favorite> = mutableListOf()

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


    override fun getFavoritList(): MutableList<Favorite> = favoritesList


    override fun addFavorites(location: Location, weather: Weather): Boolean {
        if (!checkForFavorites(location)) {
            val favorite = Favorite(location, location.getName(), weather.getTemperature(), weather.weatherCodeIcon)
            favoritesList.add(favorite)
            println("Ort gespeichert!")
            return true
        }
        return false
    }

    override fun removeFavorites(location: Location, weather: Weather): Boolean {
        val favorite = Favorite(location, location.getName(), weather.getTemperature(), weather.weatherCodeIcon)
        favoritesList.removeAll {it == favorite}
        println("Favorit wurde aus Liste entfernt")
        return true
    }


    override fun checkForFavorites(location: Location): Boolean {
        for (fav in favoritesList) {
            if ((location.getLocationName() == fav.location.getLocationName()) and (favoritesList.size <5) ) {
                return true
            }
        }
        return false
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
