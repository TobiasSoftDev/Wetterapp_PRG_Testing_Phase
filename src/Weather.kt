data class Weather(
    //val timeStamp: Date,
    val location: Location,
    val temperature2M: Double,
    val apparentTemperature: Double,
    val freezingLevelHeight: Double,
    val relativeHumidity2M: Int,
    val rain: Double,
    val windSpeed10m: Int,
    val windDirection: Int,
    val isDay: Boolean,
    //val weatherCode: Weathercode
)
