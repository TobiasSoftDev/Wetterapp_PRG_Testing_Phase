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
    fun getLocationID(): Int = id.toInt()
    fun getLongitude(): Double = longitude
    fun getLatitude(): Double = latitude
    fun getName(): String = name

    private var isFavorite = false
    fun toggleFavorite() { isFavorite = !isFavorite }




}