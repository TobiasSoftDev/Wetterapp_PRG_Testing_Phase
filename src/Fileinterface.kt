/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Interface Speichern Daten
*/

interface Fileinterface {
    /*current weather*/
    fun storeWeatherDataDaily()
    fun storeWeatherDataHourly(weather: Weather)
    fun storeWeatherData()
    fun storeFavorites()
    fun readWeatherDataDaily()
    /*current weather*/
    fun readWeatherDataHourly()
    fun readWeatherData()
    fun readFavorites()
    fun clearOldData()
    fun checkAccuracy()
}