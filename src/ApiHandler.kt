import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import org.json.JSONObject
import java.time.LocalDateTime

class ApiHandler() {

    fun getCurrentWeatherData(location: Location, time: LocalDateTime) : CurrentWeather? {
        var result: CurrentWeather? = null
        val latitude = location.getLatitude()
        val longitude = location.getLongitude()
        val apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&current=temperature_2m,relative_humidity_2m,weather_code&timezone=Europe%2FBerlin"

// Internetquelle für JSON-Parser: https://gist.github.com/Da9el00/a29b4acca9dec698e18f88fca2eb8c96
        try {

            val url : URL = URI.create(apiUrl).toURL()
            val connection : HttpURLConnection = url.openConnection() as HttpURLConnection

            //Request method: GET
            connection.requestMethod = "GET"

            // Response code
            val responseCode: Int = connection.responseCode     // 200 = HTTP_OK: Abruf funktioniert

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response data
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                val response = StringBuilder()

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
// Mit Google-KI Anpassung am Code:
                val responseText = JSONObject(response.toString())
                println("responseText-Var: $responseText")
                if (responseText.has("current")) {
                    // 1. geschachtelte Objekt "current" holen
                    val currentObj = responseText.getJSONObject("current")

                    // 2. Werte aus "currentObj" extrahieren
                    val temperature = currentObj.getDouble("temperature_2m")
                    val humidity = currentObj.getInt("relative_humidity_2m")
                    val weatherCode = currentObj.getInt("weather_code")

                    result = CurrentWeather(location, time, temperature, humidity, weatherCode)
                }

            } else {
                println("Error: Unable to fetch data from the API")
            }
            // Close the connection
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    fun getLocations(searchText: String) : MutableList<Location> {
        val results = mutableListOf<Location>()
        val text = URLEncoder.encode(searchText, "UTF-8")
        val apiUrl = "https://geocoding-api.open-meteo.com/v1/search?name=$text&count=10&language=de&format=json&countryCode=CH"

        try {

            val url : URL = URI.create(apiUrl).toURL()
            val connection : HttpURLConnection = url.openConnection() as HttpURLConnection

            //Request method: GET
            connection.requestMethod = "GET"

            // Response code
            val responseCode: Int = connection.responseCode     // 200 = HTTP_OK: Abruf funktioniert

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read and print the response data
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                val response = StringBuilder()

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                reader.close()
// Mit Google-KI Anpassung am Code:
                val responseText = JSONObject(response.toString())

                if (responseText.has("results")) {
                    val resultsArray = responseText.getJSONArray("results")

                    for (i in 0 until resultsArray.length()) {
                        val item = resultsArray.getJSONObject(i)

                        // 1. Merkmale extrahieren
                        val latitude = item.getDouble("latitude")
                        val longitude = item.getDouble("longitude")
                        val name = item.getString("name")
                        val kanton = item.getString("admin1")
                        val bezirk = item.getString("admin2")
                        val gemeinde = item.getString("admin3")
                        val elevation = item.getDouble("elevation")
                        val id = item.getInt("id")

                        results.add(Location(latitude, longitude, name, kanton, bezirk, gemeinde, elevation, id.toUInt()))
                    }
                }

            } else {
                println("Error: Unable to fetch data from the API")
            }
            // Close the connection
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        println(results)
        return results
    }
}