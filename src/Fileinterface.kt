/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Interface Speichern Daten
*/

interface Fileinterface {
    fun storeWeatherDataDaily()
    fun storeWeatherDataHourly(weather: Weather)
    fun storeWeatherData()            //current weather
    fun readWeatherDataDaily()
    fun readWeatherDataHourly()
    fun readWeatherData()
    fun clearOldData()
    fun checkAccuracy()
}