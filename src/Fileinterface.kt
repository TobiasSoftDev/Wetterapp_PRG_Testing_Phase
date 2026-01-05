/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Interface Speichern Daten
*/

interface Fileinterface {
    fun startFileInterface()
    fun readStoredWeather()
    fun readStoredLocation()
    fun storeWeather(weather: WeatherData) {}
    fun storeLocation(location: LocationData) {}
    fun clearOldData()
    fun checkAccuracy()
}