/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        T.Graf

  Beschreibung: Interface für das Speichern der Daten
*/

package weather2b.data

import weather2b.data.sourcedata.Location
import weather2b.data.sourcedata.Weather
import weather2b.data.favorites.Favorite

interface Storabledata {
    fun storeFavorites(favorite: Favorite): Favorite
    fun readFavorites()
    fun getAllFavorites(): List<Favorite>
    fun storeWeatherData(weather: Weather?)
    fun removeStoredWeatherData(from: Location)
    fun getEntriesForLocation(locationID: Int): List<WeatherData>


}