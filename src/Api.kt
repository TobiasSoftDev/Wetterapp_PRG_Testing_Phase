interface Api {
    fun fetchWeather(location: Location): Weather?
    fun getLocations(searchText: String): MutableList<Location>

}