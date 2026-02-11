/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Arbeitsdaten werden hier gespeichert
  und die Guetepruefung durchgeführt
 */

class WeatherData() : Storabledata {

    override fun storeWeatherDataDaily(weather: Weather): List<HourlyData> {
        // DataDaily --> 14 days weather forecast$
        val dailyForecast = weather.getHourlyWeatherDataAll()
        println("Getting current weather data...$dailyForecast")

        return dailyForecast
    }

    override fun storeWeatherDataHourly(weather: Weather): List<Any> {
        // DataHourly --> 24h weather of current day
        val hourlyData = weather.getHourlyWeatherDataAll()
        println("Getting current weather data...${hourlyData}Data")

        return hourlyData
    }

    override fun storeWeatherData(weather: Weather): List<Any> {
        // WeatherData --> current weather, is being rewritten with every search request
        val currentData = weather.getCurrentWeatherDataAll()
        println("Getting current weather data...$currentData")

        return currentData
    }

    override fun storeFavorites(favorites: Favorite): Favorite {
        println("Storing favorite: ${favorites.location}")

        return favorites
    }

    override fun readWeatherDataDaily() {
        TODO("Not yet implemented")
    }

    override fun readWeatherDataHourly() {
        TODO("Not yet implemented")
    }

    override fun readWeatherData() {
        TODO("Not yet implemented")
    }

    override fun readFavorites() {
        TODO("Not yet implemented")
    }

    override fun clearOldData() {
        TODO("Not yet implemented")
    }

    override fun checkAccuracy() {
        TODO("Not yet implemented")
    }

}