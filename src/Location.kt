class Location(
    private val latitude: Double,
    private val longitude: Double,
    private val name: String,
    private val kanton: String,
    private val bezirk: String,
    private val gemeinde: String,
    private val elevation: Double,
    private val id: UInt
) {
    private var isFavorite = false
    fun toggleFavorite() { isFavorite = !isFavorite }

    fun getLongitude(): Double = longitude
    fun getLatitude(): Double = latitude

    override fun toString(): String {
        return "$name: $kanton, $bezirk, $gemeinde |"
    }

}