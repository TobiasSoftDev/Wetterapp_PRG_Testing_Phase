/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        P.Koch

  Beschreibung: API Interface zwischen API Handler und Manager
 */

interface Api {
    fun fetchWeather(location: Location): Weather?
    fun getLocations(searchText: String): MutableList<Location>

}