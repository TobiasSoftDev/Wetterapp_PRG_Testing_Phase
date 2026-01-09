import java.time.LocalDateTime

data class HourlyWeather(
    private val location: Location,
    private val time: LocalDateTime,
    private val temperature2M: MutableList<Double>,
    private val relativeHumidity2M: MutableList<Int>,
    private val weatherCode: MutableList<Int>) {

    fun getDataLastHour() : CurrentWeather {
        // Aktuelle Stunde: Hilfsvariable damit später über die ersten 24 Items iteriert werden kann.
        val currentHour = LocalDateTime.now().hour
        // Funktion um die Temperatur der letzten vollen Stunde zu bekommen:
        var temperatureAt = temperature2M[0]         // Für Übungszwecke 0, sonst: [currentTime.hour]
        var relativeHumidityAt  = relativeHumidity2M[0]         // siehe oben
        var weatherCodeAt = weatherCode[0]                      // siehe oben
        return CurrentWeather(location, time, temperatureAt, relativeHumidityAt, weatherCodeAt)
    }

}