import java.time.LocalDateTime

data class DailyWeather(
    private val location: Location,
    private val time: LocalDateTime,
    private val temperature2MMin: MutableList<Double>,
    private val temperature2MMax: MutableList<Double>,
    private val weatherCode: MutableList<Int>) {

}
