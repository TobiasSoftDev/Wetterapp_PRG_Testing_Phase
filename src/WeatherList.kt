/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Arbeitsdaten werden hier gespeichert
  und die Guetepruefung durchgeführt
 */

abstract class WeatherList() : Fileinterface {

    override fun storeWeatherDataDaily() {
        TODO("Not yet implemented")
    }

    override fun storeWeatherDataHourly(weather: Weather) {
        TODO("Not yet implemented")
    }

    override fun storeWeatherData() {
        TODO("Not yet implemented")
    }

    override fun readWeatherDataDaily() {
        TODO("Not yet implemented")
    }

//    override fun readWeatherDataHourly(): Weather  {
//        TODO("Not yet implemented")
//        return Weather
//    }

    override fun readWeatherData(){
        TODO("Not yet implemented")
    }

    override fun clearOldData() {
        TODO("Not yet implemented")
    }

    override fun checkAccuracy() {
        TODO("Not yet implemented")
    }

}