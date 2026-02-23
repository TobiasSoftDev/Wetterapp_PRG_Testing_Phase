/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Interface für das Speichern der Daten
*/

interface Storabledata {
    fun storeFavorites(favorite: Favorite): Favorite
    fun readFavorites()
    fun getAllFavorites(): List<Favorite>
    fun storeWeatherData(weather: Weather?)
    fun removeStoredWeatherData(from: Location)
    fun getEntriesForLocation(locationID: Int): List<WeatherData>


}