data class Location(
    private val latitude: Double,
    private val longitude: Double,
    private val name: String,
    private val kanton: String,
    private val bezirk: String,
    private val gemeinde: String,
    private val elevation: Double,
    private val id: UInt) {



    fun getLocationDataAll(): List<Any> = listOf(latitude, longitude, name, kanton, bezirk, gemeinde, elevation)
    fun getLocationForSearchResult() = "$name ($kanton), Gemeinde: $gemeinde"
    fun getLocationName() = name
    fun getLongitude(): Double = longitude
    fun getLatitude(): Double = latitude
    fun getName(): String = name
    fun getLocationID(): UInt = id

    private var isFavorite = false
    fun toggleFavorite() { isFavorite = !isFavorite }




}