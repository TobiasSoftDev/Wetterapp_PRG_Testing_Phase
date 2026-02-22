data class Location(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var name: String = "",
    var kanton: String = "",
    var bezirk: String = "",
    var gemeinde: String = "",
    var elevation: Double = 0.0,
    var id: Int = 0){

    //fun getLocationDataAll(): List<Any> = listOf(latitude, longitude, name, kanton, bezirk, gemeinde, elevation)
   fun getLocationForSearchResult() = "$name ($kanton), Gemeinde: $gemeinde"
    /*fun getLocationName() = name
    fun getLongitude(): Double = longitude
    fun getLatitude(): Double = latitude
    fun getName(): String = name
    fun getLocationID(): Int = id */

  //  private var isFavorite = false
   // fun toggleFavorite() { isFavorite = !isFavorite }




}