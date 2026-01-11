import java.time.LocalDateTime

class Manager() {
    private val apiHandler = ApiHandler()
    private val weatherCodes = WeatherCode.entries


    fun fetchCurrentWeather(location: Location) : CurrentWeather {
        val time = LocalDateTime.now()
        val result = apiHandler.getCurrentWeatherData(location, time)
        println(result)
        // store?
        return result!!
    }


    fun fetchHourlyWeather(location: Location) {
        // store?
    }

    fun fetchDailyWeather(location: Location) {
        // store?
    }

    fun fetchLocations(searchedLocation: String) : MutableList<Location> {
        val result = apiHandler.getLocations(searchedLocation)
        println(result)
        return result
    }

    private fun storeWeather() {

    }

    private fun storeLocation() {

    }

//    fun getCurrentWeather(): CurrentWeather {
//
//        return CurrentWeather()
//    }

//    fun getHourlyWeather(): HourlyWeather {
//
//        return HourlyWeather()
//    }
//
//    fun getDailyWeather(): DailyWeather {
//
//        return DailyWeather()
//    }
//
//    fun getLocation(): Location {
//
//        return Location()
    }
