import kotlin.Double

interface Api {
    fun getWeatherData(location: Location) { }
    fun getLocations(searchText: String) { }

}