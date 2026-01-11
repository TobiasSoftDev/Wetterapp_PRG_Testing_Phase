import java.time.LocalDateTime

data class CurrentDayWeather (
    private val location: Location,
    private val time: LocalDateTime,
    private val temperature2MMin: Double,
    private val temperature2MMax: Double,
    private val weatherCode: Int)
