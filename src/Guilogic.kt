import javafx.collections.ObservableList

/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        P.Koch P.Theiler und T.Graf

  Beschreibung: Interface um auf die Methoden der verschiedenen Ein-, Ausgabe zwischen dem Gui und dem manager zuzugreifen
 */

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