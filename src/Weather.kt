data class Weather(
    private val location: Location,
    private val temperature: Double,
    private val humidity: Int,
    private val weatherCode: WeatherCodes,
    private val precipitation: Int,
    private val windSpeed: Int,
    private val windDirection: Int,
    private val apparentTemperature: Double,
    private val hourlyList: List<HourlyData>,
    private val dailyList: List<DailyData>) {

    fun getTemperature(): Double = temperature
    fun getHumidity(): Int = humidity
    fun getWeatherCode(): WeatherCodes = weatherCode
    fun getPrecipitation(): Int = precipitation
    fun getWindSpeed(): Int = windSpeed
    fun getWindDirection(): Int = windDirection
    fun getApparentTemperature(): Double = apparentTemperature
    fun getHourlyList(): List<HourlyData> = hourlyList
    fun getDailyList(): List<DailyData> = dailyList

    fun getCurrentWeatherDataAll() : List<Any> = listOf(temperature, apparentTemperature, humidity, precipitation, windSpeed, windDirection, "${weatherCodeNumber} ${weatherCodeDescription} ${weatherCodeIcon}")

    val weatherCodeNumber: Int
       get() = weatherCode.code

    val weatherCodeDescription: String
        get() = weatherCode.description

    val weatherCodeIcon: String
        get() = weatherCode.icon

    val weatherTemperature: Double
        get() = temperature

    val locationName: String
        get() = location.getName()

    val latitude: Double
        get() = location.getLatitude()

    val longitude: Double
        get() = location.getLongitude()

    val locationID: UInt
        get() = location.getLocationID()

    val weatherList : List<Any>
        get() = getCurrentWeatherDataAll()


        //println("$temperature\n$apparentTemperature\n$humidity\n$precipitation\n$windSpeed\n$windDirection\n$weatherCode")
    fun getHourlyWeatherDataAll() = hourlyList
    fun getDailyWeatherDataAll() = dailyList


}