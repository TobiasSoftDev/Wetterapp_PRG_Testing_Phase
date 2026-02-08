/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Arbeitsdaten werden hier gespeichert
  und die Guetepruefung durchgeführt
 */

abstract class WeatherList() : Fileinterface {

//    private val HourlyWeather: MutableList<HourlyWeatherList>)

    override fun storeWeatherDataDaily() {
        TODO("Not yet implemented")
    }

    override fun storeWeatherDataHourly(weather: Weather) {
        TODO("Not yet implemented")
    }

    override fun storeWeatherData() {
        TODO("Not yet implemented")
    }

    override fun storeFavorites() {
        TODO("Not yet implemented")
    }

    override fun readWeatherDataDaily() {
        TODO("Not yet implemented")
    }

    override fun readWeatherDataHourly() {
        TODO("Not yet implemented")
    }

    override fun readWeatherData() {
        TODO("Not yet implemented")
    }

    override fun readFavorites() {
        TODO("Not yet implemented")
    }

    override fun clearOldData() {
        TODO("Not yet implemented")
    }

    override fun checkAccuracy() {
        TODO("Not yet implemented")
    }

}