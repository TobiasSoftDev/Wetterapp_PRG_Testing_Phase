/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Arbeitsdaten werden hier gspeichert
  und die Guetepruefung durchgeführt
 */

annotation class LocationData
annotation class WeatherData

class File : Fileinterface {
    private var running = false

    override fun startFileInterface() {
        running = true
    }

    override fun readStoredWeather() {
        if (running) {
            println("Wetter ist: $WeatherList")
        }
        else {
            println("keine neuen Wetterdaten")
        }
    }

    override fun readStoredLocation(){
        if (running) {
            println("Ort ist: $LocationList")
        }
        else {
            println("keine neuen Ortsangagben")
        }
    }

    override fun storeWeather(weather: WeatherData) {
        WeatherList.add("Regen")
        println("Wetter ist: $WeatherList")
    }

    override fun storeLocation(location: LocationData) {
        LocationList.add("Zürich")
        println("Ort ist: $LocationList")
    }

    override fun clearOldData() {
        LocationList.removeAt(1)
        WeatherList.removeAt(1)
    }

    override fun checkAccuracy() {
        TODO("Not yet implemented")
    }
}
