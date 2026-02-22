import javafx.scene.image.Image
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class DailyWeather(
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
    fun getSunset(): LocalTime? = sunset.toLocalTime()
    fun getSunrise(): LocalTime? = sunrise.toLocalTime()
    fun getSunsetLocalDateTime(): LocalDateTime = sunset
    fun getSunriseLocalDateTime(): LocalDateTime = sunrise
    fun getWeatherCode(): WeatherCodes = weatherCode
}
