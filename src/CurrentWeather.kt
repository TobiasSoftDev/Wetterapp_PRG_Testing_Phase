import java.time.LocalDateTime

data class CurrentWeather(
    private val location: Location,
    private val time: LocalDateTime,
    private val temperature2M: Double,
    private val relativeHumidity2M: Int,
    private val weatherCode: Int
    )
