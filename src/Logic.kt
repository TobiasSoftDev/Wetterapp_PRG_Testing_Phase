import javafx.collections.ObservableList

interface Logic {
    fun getLocations(searchText: String): MutableList<Location>
    fun getCurrentWeather(from: Location): Weather?
    fun addFavorites(location: Location, weather: Weather): Boolean
    fun removeFavorites(location: Location): Boolean
    fun checkForFavorites(location: Location): Boolean
    fun getFavoritesObservableList(): ObservableList<Favorite>
    fun checkAccuracy(id: Int, currentWeather: Weather): Double
}