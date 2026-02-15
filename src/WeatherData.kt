import java.io.File
import java.time.format.DateTimeFormatter

/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Arbeitsdaten werden hier gespeichert
  und die Guetepruefung durchgeführt
 */

class WeatherData() : Storabledata {

    // supply Date and time on the first line in the data file
    val date = java.time.LocalDate.now()
        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    val time = java.time.LocalTime.now()
        .format(DateTimeFormatter.ofPattern("HH:mm:ss"))

    override fun storeWeatherDataDaily(weather: Weather?): List<HourlyData>? {
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

    override fun storeWeatherDataHourly(weather: Weather?): List<HourlyData>? {
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

    override fun storeFavorites(favorites: Favorite): Favorite {
        val file = File("resources/favoriteLocationData/Favorites.xml")
        println("Storing favorite: ${favorites.location}")

        //  Create new directory
        val favoritesDirectory = File("resources/favoriteLocationData")
        favoritesDirectory.mkdir()
        println("Directory favoriteLocationData exists...")
        println(favoritesDirectory.exists())

        return favorites
    }

    override fun readWeatherDataDaily() {
        val file = File("resources/dailyData/DailyWeatherData$date.xml")

        val lines = file.readLines()
        for (line in lines) {
            print (line)
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