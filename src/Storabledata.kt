/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Interface Speichern Daten
*/

interface Storabledata {
    // DataDaily --> 14 days weather forecast
    fun storeWeatherDataDaily(weather: Weather?): List<HourlyData>?
    // DataHourly --> 24h weather of current day
    fun storeWeatherDataHourly(weather: Weather?): List<HourlyData>?
    // WeatherData --> current weather, is being rewritten with every search request
    fun storeWeatherData(weather: Weather?): List<Any>?
    fun storeFavorites(favorites: Favorite): Favorite
    fun readWeatherDataDaily()
    fun readWeatherDataHourly()
    fun readWeatherData()
    fun readFavorites()
    fun checkAccuracy()
    fun storeData(weather: Weather?)
}