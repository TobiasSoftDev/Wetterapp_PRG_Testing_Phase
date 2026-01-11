import java.time.LocalDateTime

data class DailyWeather(
    private val location: Location,
    private val time: LocalDateTime,
    private val temperature2MMin: MutableList<Double>,
    private val temperature2MMax: MutableList<Double>,
    private val weatherCode: MutableList<Int>) {

    fun getToday() : CurrentDayWeather {
        // Werte des aktuellen Tages entsprechen immer erstem Listeneintrag.
        return CurrentDayWeather(location, time, temperature2MMin[0], temperature2MMax[0], weatherCode[0])
    }

}
