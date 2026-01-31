interface Logic {
    fun getLocations(searchText: String): MutableList<Location>
    fun getCurrentWeather(from: Location): Weather?
}