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
    fun getHourlyList(): List<HourlyWeather> = hourlyList
    fun getDailyList(): List<DailyWeather> = dailyList
    fun getLocationID(): Int = location.getLocationID().toInt()
    fun getLongitude(): Double = location.getLongitude()
    fun getLatitude(): Double = location.getLatitude()

    fun getHourlyWeatherCodes(): MutableList<Int> {
        val myList = mutableListOf<Int>()
        for (code in hourlyList) {
            myList.add(code.getWeatherCodes().code)
        }
        return myList
    }

    fun getHourlyForecasts(): MutableList<HourlyWrapper> {
        val myList = mutableListOf<HourlyWrapper>()

        for (code in hourlyList) {
            val wrapper = HourlyWrapper(code.getTemperature2M(), code.getWeatherCodes().code)
            myList.add(wrapper)
        }
        return myList
    }

    fun getDailyWeatherDataAll() = dailyList

    val weatherTemperature: Double
        get() = temperature

    val locationName: String
        get() = location.name

    val latitude: Double
        get() = location.latitude

    val longitude: Double
        get() = location.longitude

    val locationID: Int
        get() = location.id

    val weatherList : List<Any>
        get() = getCurrentWeatherDataAll()


}