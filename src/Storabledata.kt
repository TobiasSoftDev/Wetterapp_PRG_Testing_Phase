/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Interface für das Speichern der Daten
*/

interface Storabledata {
    // DataDaily --> 14 days weather forecast
    fun storeWeatherDataDaily(weather: Weather?): List<HourlyWeather>?
    // DataHourly --> 24h weather of current day
    fun storeWeatherDataHourly(weather: Weather?): List<HourlyWeather>?
    // WeatherData --> current weather, is being rewritten with every search request
    fun storeWeatherData(weather: Weather?): List<Any>?
    fun storeFavorites(favorite: Favorite): Favorite
    fun getAllFavorites(): List<Favorite>
    fun readWeatherDataDaily()
    fun readWeatherDataHourly()
    fun readWeatherData()
    fun readFavorites()
    fun checkAccuracy()

    fun storeData(weather: Weather?)
    fun getHistoryForLocation(locationID: Int): List<WeatherData>
}