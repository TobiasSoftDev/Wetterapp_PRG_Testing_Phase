import javafx.scene.image.Image
import java.time.LocalDateTime

data class HourlyWeather(
    private val times: LocalDateTime,
    private val temperature2M: Double,
    private val apparentTemperature2M: Double,
    private val relativeHumidity2M: Int,
    private val precipitation: Double,
    private val windSpeed: Double,
    private val windDirection: Int,
    private val freezingLevel: Double,
    private val weatherCode: WeatherCodes) {

    val weatherCodeNumber: Int
        get() = weatherCode.code

    val weatherCodeDescription: String
        get() = weatherCode.description

    val weatherCodeIcon: Image
        get() = weatherCode.icon






    fun get(): List<Any> {
        return listOf(times, temperature2M, apparentTemperature2M, relativeHumidity2M, precipitation, windSpeed, windDirection, freezingLevel, "${weatherCodeNumber}${weatherCodeDescription}${weatherCodeIcon}")
    }

}

