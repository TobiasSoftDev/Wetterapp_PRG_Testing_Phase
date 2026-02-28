/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        P.Koch und T.Graf

  Beschreibung: In Daily Weather werden die Daten der kommenden 14 Tage verarbeitet
 */

package weather2b.data.sourcedata

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
    private val weatherCode: WeatherCodes
) {
    fun getTemperatureMin(): Double = temperatureMin
    fun getTemperatureMax(): Double = temperatureMax
    fun getSunset(): LocalTime? = sunset.toLocalTime()
    fun getSunrise(): LocalTime? = sunrise.toLocalTime()
    fun getSunsetLocalDateTime(): LocalDateTime = sunset
    fun getSunriseLocalDateTime(): LocalDateTime = sunrise
}
