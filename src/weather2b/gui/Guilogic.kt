/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        P.Koch P.Theiler und T.Graf

  Beschreibung: Interface um auf die Methoden der verschiedenen Ein-, Ausgabe zwischen dem Gui und dem manager zuzugreifen
 */

package weather2b.gui

import javafx.collections.ObservableList
import weather2b.data.sourcedata.Location
import weather2b.data.sourcedata.Weather
import weather2b.data.favorites.Favorite

interface Guilogic {
    fun getLocations(searchText: String): MutableList<Location>
    fun getCurrentWeather(location: Location): Weather?
    fun addFavorites(location: Location, weather: Weather): Boolean
    fun removeFavorites(location: Location): Boolean
    fun checkForFavorites(location: Int): Boolean
    fun getFavoritesObservableList(): ObservableList<Favorite>
    fun checkAccuracy(id: Int, currentWeather: Weather): Double?
    fun updateFavoriteFile()
}