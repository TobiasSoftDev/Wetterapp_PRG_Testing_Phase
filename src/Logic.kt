interface Logic {
    fun getLocations(searchText: String): MutableList<Location>
    fun getCurrentWeather(from: Location): Weather?
    fun getFavoritList(): MutableList<Favorite>
    fun addFavorites(location: Location, weather: Weather): Boolean
    fun removeFavorites(location: Location,weather: Weather): Boolean
    fun checkForFavorites(location: Location): Boolean

}