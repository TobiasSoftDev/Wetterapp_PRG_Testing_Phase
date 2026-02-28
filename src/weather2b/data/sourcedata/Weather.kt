/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        P.Koch, P.Theiler und T.Graf

  Beschreibung: Wetter Klasse Daten zusammenfuehren um daraus Wetter Objekte zu bilden
 */

package weather2b.data.sourcedata

import weather2b.data.wrapper.HourlyWrapper

data class Weather(
    private val location: Location,
    private val temperature: Double,
    private val humidity: Int,
    private val weatherCode: WeatherCodes,
    private val precipitation: Int,
    private val windSpeed: Int,
    private val windDirection: Int,
    private val apparentTemperature: Double,
    private val hourlyList: List<HourlyWeather>,
    private val dailyList: List<DailyWeather>) {

    fun getTemperature(): Double = temperature
    fun getHumidity(): Int = humidity
    fun getWeatherCode(): WeatherCodes = weatherCode
    fun getPrecipitation(): Int = precipitation
    fun getWindSpeed(): Int = windSpeed
    fun getWindDirection(): Int = windDirection
    fun getApparentTemperature(): Double = apparentTemperature
    fun getDailyList(): List<DailyWeather> = dailyList
    fun getLocationID(): Int = location.id
    fun getLongitude(): Double = location.longitude
    fun getLatitude(): Double = location.latitude

    fun getHourlyForecasts(): MutableList<HourlyWrapper> {
        val myList = mutableListOf<HourlyWrapper>()

        for (code in hourlyList) {
            val wrapper = HourlyWrapper(code.getTemperature2M(), code.getWeatherCodes().code)
            myList.add(wrapper)
        }
        return myList
    }

    fun getDailyWeatherDataAll() = dailyList

}