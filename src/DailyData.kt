import javafx.scene.image.Image
import java.time.LocalDate
import java.time.LocalDateTime

data class DailyData(
    private val time: LocalDate,
    private val temperatureMin: Double,
    private val temperatureMax: Double,
    private val apparentTemperatureMin: Double,
    private val apparentTemperatureMax: Double,
    private val sunset: LocalDateTime,
    private val sunrise: LocalDateTime,
    private val weatherCode: WeatherCodes ) {

    fun getTime(): LocalDate = time
    fun getTemperatureMin(): Double = temperatureMin
    fun getTemperatureMax(): Double = temperatureMax
    fun getApparentTemperatureMin(): Double = apparentTemperatureMin
    fun getApparentTemperatureMax(): Double = apparentTemperatureMax
    fun getSunset(): String = "${sunset.hour}:${sunset.minute} Uhr"
    fun getSunrise(): String = "${sunrise.hour}:${sunrise.minute} Uhr"

    val weatherCodeNumber: Int
        get() = weatherCode.code

    val weatherCodeDescription: String
        get() = weatherCode.description

    val weatherCodeIcon: Image
        get() = weatherCode.icon




    fun get(): List<Any> {
        return listOf(time, temperatureMin, temperatureMax, apparentTemperatureMin, apparentTemperatureMax, sunset, sunrise,  "${weatherCodeNumber}${weatherCodeDescription}${weatherCodeIcon}")
    }
}
