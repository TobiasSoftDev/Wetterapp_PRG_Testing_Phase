import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.URLEncoder

class ApiHandler() {

    fun getWeatherData(location: Location) { }

    fun getLocations(searchText: String) {

        val text = URLEncoder.encode(searchText, "UTF-8")
        val apiUrl = "https://geocoding-api.open-meteo.com/v1/search?name=$text&count=10&language=de&format=json&countryCode=CH"

        try {
            val url : URL = URI.create(apiUrl).toURL()
            val connection : HttpURLConnection = url.openConnection() as HttpURLConnection

            //Request method: GET
            connection.requestMethod = "GET"

            // Response code
            val responseCode: Int = connection.responseCode     // 200 = HTTP_OK: Abruf funktioniert
            println("Response Code: $responseCode")

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read and print the response data
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                val response = StringBuilder()

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                println("Response Data: $response")

            } else {
                println("Error: Unable to fetch data from the API")
            }

            // Close the connection
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}