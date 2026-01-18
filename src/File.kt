import kotlin.math.E

/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Arbeitsdaten werden hier gspeichert
  und die Guetepruefung durchgeführt
 */


/* Nur als info, später wieder löschen
class FileHandler {
    private var number = 1
    private val dataFile = File("File", number)

    fun work()  {
        dataFile.startFileInterface()

        if (dataFile.running) {
            LocationList.add("Thun")
            WeatherList.add("Sonnenschein")
            dataFile.readStoredWeather()
            dataFile.readStoredLocation()
        }
        else {
            println("File already exists")
        }
    }
}
 */

annotation class LocationData
annotation class WeatherData

data class File(
    private val name: String,
    private val nr: Int) : Fileinterface {
    var running = false

    override fun startFileInterface() {
        running = true
    }

    override fun readStoredWeather() {
        if (running) {
            println("Wetter ist: $WeatherList")
            println("Name der Liste ist: $name$nr")

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

    override fun readFileList(): MutableList<File> {
        TODO("Not yet implemented")
    }

    override fun storeWeather(weather: WeatherData) {
        WeatherList.add("Regen")
        println("Wetter ist: $WeatherList")
    }

    override fun storeLocation(location: LocationData) {
        LocationList.add("Zürich")
        println("Ort ist: $LocationList")
    }

    override fun storeFileList(): MutableList<File> {
        TODO("Not yet implemented")
    }

    override fun clearOldData() {
        LocationList.removeAt(1)
        WeatherList.removeAt(1)
    }

    override fun checkAccuracy() {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "$name$nr"
    }
}


