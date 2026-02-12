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

    override fun storeWeatherDataDaily(weather: Weather): List<HourlyData> {
        // DataDaily --> 14 days weather forecast$
        val dailyForecast = weather.getHourlyWeatherDataAll()
        println("Getting current weather data...$dailyForecast")

        //  Create new directory
        val dailyDirectory = File("resources/dailyData")
        dailyDirectory.mkdir()
        println("Directory dailyData exists...")
        println(dailyDirectory.exists())

        // Write data in a .txt file
        val file = File("resources/dailyData/DailyWeatherData$date.txt")
        val formattedData = dailyForecast .toString()
            .replace("]", "]\n")  // nach jedem ] ein Zeilenumbruch

        file.writeText("--- Datum: $date | Uhrzeit: $time ---\n")  // erste Zeile mit Timestamp
        file.appendText(formattedData)

        val lines = file.readLines()
        for (line in lines) {
            print (line)
        }

        return dailyForecast
    }

    override fun storeWeatherDataHourly(weather: Weather): List<Any> {
        // DataHourly --> 24h weather of current day
        val hourlyData = weather.getHourlyWeatherDataAll()
        println("Getting current weather data...${hourlyData}Data")

        //  Create new directory
        val hourlyDirectory = File("resources/hourlyData")
        hourlyDirectory.mkdir()
        println("Directory hourlyData exists...")
        println(hourlyDirectory.exists())

        // Write data in a .txt file
        val file = File("resources/hourlyData/HourlyWeatherData.txt")
        val formattedData = hourlyData.toString()
            .replace("]", "]\n")  // nach jedem ] ein Zeilenumbruch

        file.writeText("--- Datum: $date | Uhrzeit: $time ---\n")  // erste Zeile mit Timestamp
        file.appendText(formattedData)

        val lines = file.readLines()
        for (line in lines) {
            print (line)
        }

        return hourlyData
    }

    override fun storeWeatherData(weather: Weather): List<Any> {
        // WeatherData --> current weather, is being rewritten with every search request
        val currentData = weather.getCurrentWeatherDataAll()
        println("Getting current weather data...$currentData")

        //  Create new directory
        val currentDirectory = File("resources/currentData")
        currentDirectory.mkdir()
        println("Directory currentData exists...")
        println(currentDirectory.exists())

        // Write data in a .txt file
        val file = File("resources/currentData/CurrentWeatherData.txt")
        val formattedData = currentData.toString()
            .replace("]", "]\n")  // nach jedem ] ein Zeilenumbruch

        file.writeText("--- Datum: $date | Uhrzeit: $time ---\n")  // erste Zeile mit Timestamp
        file.appendText(formattedData)

        val lines = file.readLines()
        for (line in lines) {
            print (line)
            }

        return currentData
    }

    override fun storeFavorites(favorites: Favorite): Favorite {
        println("Storing favorite: ${favorites.location}")

        //  Create new directory
        val favoritesDirectory = File("resources/favoriteLocationData")
        favoritesDirectory.mkdir()
        println("Directory favoriteLocationData exists...")
        println(favoritesDirectory.exists())


        return favorites
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