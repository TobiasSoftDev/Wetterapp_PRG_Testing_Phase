import plotterLineChart.plot
import java.beans.XMLDecoder
import java.beans.XMLEncoder
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Arbeitsdaten werden hier gespeichert
  und die Guetepruefung durchgeführt
 */

data class WeatherData(
    var timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd 'T' HH:mm:ss")).toString(),
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var id: Int = 0,
    var temperature: Double = 0.0,
    //var temperatureMaxHistory: MutableList<Double> = mutableListOf(),
    //var temperatureMinHistory: MutableList<Double> = mutableListOf(),
    var weatherCode: Int = 0) : Storabledata {

    // supply Date and time on the first line in the data file
    val date = java.time.LocalDate.now()
        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    val time = java.time.LocalTime.now()
        .format(DateTimeFormatter.ofPattern("HH:mm:ss"))

    override fun storeWeatherDataDaily(weather: Weather?): List<HourlyWeather>? {
        // DataDaily --> 14 days weather forecast$
        val dailyForecast = weather?.getHourlyWeatherDataAll()
        println("Getting current weather data...$dailyForecast")

        //  Create new directory
        val dailyDirectory = File("resources/dailyData")
        dailyDirectory.mkdir()
        println("Directory dailyData exists...")
        println(dailyDirectory.exists())

        // Dateien älter als 14 Tage löschen (Datum aus Dateiname lesen)
        // diesen Teil der Funktion wurde mittels Claude AI entwickelt
        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val cutoffDate = java.time.LocalDate.now().minusDays(14)

        dailyDirectory.listFiles()?.forEach { file ->
            val match = Regex("""(\d{2}\.\d{2}\.\d{4})""").find(file.name)
            if (match != null) {
                val fileDate = java.time.LocalDate.parse(match.value, formatter)
                if (fileDate.isBefore(cutoffDate)) {
                    file.delete()
                    println("Gelöscht: ${file.name}")
                }
            }
        }

        // Write data in a .xml file
        val file = File("resources/dailyData/DailyWeatherData$date.xml")
        val formattedData = dailyForecast .toString()
            .replace("]", "]\n")  // nach jedem ] ein Zeilenumbruch

        file.writeText("--- Datum: $date | Uhrzeit: $time ---\n")  // erste Zeile mit Timestamp
        file.appendText(formattedData)

        return dailyForecast
    }

    override fun storeWeatherDataHourly(weather: Weather?): List<HourlyWeather>? {
        // DataHourly --> 24h weather of current day
        val hourlyData = weather?.getHourlyWeatherDataAll()
        println("Getting current weather data...${hourlyData}Data")

        //  Create new directory
        val hourlyDirectory = File("resources/hourlyData")
        hourlyDirectory.mkdir()
        println("Directory hourlyData exists...")
        println(hourlyDirectory.exists())

        // Write data in a .xml file
        val file = File("resources/hourlyData/HourlyWeatherData.xml")
        val formattedData = hourlyData.toString()
            .replace("]", "]\n")  // nach jedem ] ein Zeilenumbruch

        file.writeText("--- Datum: $date | Uhrzeit: $time ---\n")  // erste Zeile mit Timestamp
        file.appendText(formattedData)

        return hourlyData
    }

    override fun storeWeatherData(weather: Weather?): List<Any>? {
        // WeatherData --> current weather, is being rewritten with every search request
        val currentData = weather?.getCurrentWeatherDataAll()
        println("Getting current weather data...$currentData")

        //  Create new directory
        val currentDirectory = File("resources/currentData")
        currentDirectory.mkdir()
        println("Directory currentData exists...")
        println(currentDirectory.exists())

        // Write data in a .xml file
        val file = File("resources/currentData/CurrentWeatherData.xml")
        val formattedData = currentData.toString()
            .replace("]", "]\n")  // nach jedem ] ein Zeilenumbruch

        file.writeText("--- Datum: $date | Uhrzeit: $time ---\n")  // erste Zeile mit Timestamp
        file.appendText(formattedData)

        return currentData
    }

    override fun storeData(weather: Weather?) {

        if (weather != null) {
            val dataset = WeatherData(
                LocalDateTime.now().toString(),
                weather.latitude,
                weather.longitude,
                weather.locationID.toInt(),
                weather.getTemperature(),

                weather.getWeatherCode().code
            )
            val file = getStorageFile()
            val history = if (file.exists()) {
                loadHistory(file)
            } else {
                FileWrapper()
            }
            history.dataList.add(dataset)
            val encoder = XMLEncoder(               // Stream bereitstellen
                BufferedOutputStream(
                    FileOutputStream(file)
                )
            )
            encoder.writeObject(history)            // Objekt speichern
            encoder.close()                         // Stream schliessen
        }
    }

    private fun getStorageFile(): File {
        // Holt den Pfad des globalen Benutzerordners (bspw. Mac: /users/peterkoch)
        val userHome = System.getProperty("user.home")
        // erstellt einen Ordner im Dateiensystem des Nutzers. Der Punkt steht für einen versteckten Ordner "XmlTest".
        val storageDirectory = Paths.get(userHome, ".Weather2b", "storage")

        if (!Files.exists(storageDirectory)) {
            Files.createDirectories(storageDirectory)
        }
        return storageDirectory.resolve("storageFile.xml").toFile()
    }

    private fun loadHistory(file: File): FileWrapper {
        try {
            val decoder = XMLDecoder(
                BufferedInputStream(
                    FileInputStream(file)))

            val storedObject = decoder.readObject() as FileWrapper
            return storedObject
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return FileWrapper()
    }

    override fun getHistoryForLocation(locationID: Int): List<WeatherData> {
        val file = getStorageFile()
        if (!file.exists()) { return emptyList() }

        val history = loadHistory(file)
        print("History des gesuchten Orts: ${history.dataList.filter { it.id == locationID }}")
        return history.dataList.filter { it.id == locationID }
    }

    override fun storeFavorites(favorites: Favorite): Favorite {
        val file = File("resources/favoriteLocationData/Favorites.txt")
        println("Storing favorite: ${favorites.location}")

        //  Create new directory
        val favoritesDirectory = File("resources/favoriteLocationData")
        favoritesDirectory.mkdir()
        println("Directory favoriteLocationData exists...")
        println(favoritesDirectory.exists())

        return favorites
    }

    override fun readWeatherDataDaily() {
        val file = File("resources/dailyData/DailyWeatherData$date.txt")

        val lines = file.readLines()
        for (line in lines) {
            print(line)
        }
    }

    override fun readWeatherDataHourly() {
        val file = File("resources/hourlyData/HourlyWeatherData.xml")

        val lines = file.readLines()
        for (line in lines) {
            print (line)
        }
    }

    override fun readWeatherData() {
        val file = File("resources/currentData/CurrentWeatherData.xml")

        val lines = file.readLines()
        for (line in lines) {
            print (line)
        }
    }

    override fun readFavorites() {
        val file = File("resources/favoriteLocationData/Favorites.xml")

        val lines = file.readLines()
        for (line in lines) {
            print (line)
        }
    }

    override fun checkAccuracy() {
        TODO("Not yet implemented")
    }

}