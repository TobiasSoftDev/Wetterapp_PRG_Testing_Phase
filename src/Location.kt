class Location(
    private val latitude: Double,
    private val longitude: Double,
    private val name: String,
    private val kanton: String,
    private val kreis: String,
    private val gemeinde: String,
    private val elevation: Double,
    private val id: UInt
) {
    private var isFavorite = false
    fun toggleFavorite() { isFavorite = !isFavorite }

}