/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Interface für das Speichern der Daten
*/

interface Storabledata {
    // DataDaily --> 14 days weather forecast
//    fun storeWeatherDataDaily(weather: Weather?): List<HourlyWeather>?
    // DataHourly --> 24h weather of current day
//    fun storeWeatherDataHourly(weather: Weather?): List<HourlyWeather>?
    // WeatherData --> current weather, is being rewritten with every search request
    fun storeFavorites(favorite: Favorite): Favorite
//    fun readWeatherDataDaily()
//    fun readWeatherDataHourly()
//    fun readWeatherData()
    fun readFavorites()
//    fun checkAccuracy()
    fun getAllFavorites(): List<Favorite>
//    fun readWeatherDataDaily()
//    fun readWeatherDataHourly()
//    fun readWeatherData()
//    fun readFavorites()
    //fun checkAccuracy(id: Int, currentWeather: Weather)

    fun getEntriesForLocation(locationID: Int): List<WeatherData>
    fun storeWeatherData(weather: Weather?)
    fun getWeatherHistoryFromLocation(locationID: Int): List<WeatherData>
}