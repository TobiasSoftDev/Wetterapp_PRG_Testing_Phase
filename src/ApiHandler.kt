import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime

class ApiHandler() : Api {

    override fun fetchWeather(location: Location): Weather? {
        var result: Weather? = null
        val latitude = location.getLatitude()
        val longitude = location.getLongitude()
        val apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&daily=weather_code,temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset&hourly=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,wind_direction_10m,precipitation,freezing_level_height&current=temperature_2m,relative_humidity_2m,apparent_temperature,wind_speed_10m,wind_direction_10m,precipitation,weather_code&timezone=Europe%2FBerlin&forecast_days=14"

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
// Mit Google-KI Anpassung am Code damit Daten in Klassen übertragen werden können:
                val responseText = JSONObject(response.toString())

// Respons-Objekt in "current", "hourly" und "daily" in JSON-Objekte aufteilen:
                val currentObj = responseText.getJSONObject("current")
                val hourlyObj = responseText.getJSONObject("hourly")
                val dailyObj = responseText.getJSONObject("daily")

// Werte aus "current"-Objekt extrahieren:
                val temperature = currentObj.optDouble("temperature_2m", 0.0)
                val humidity = currentObj.optInt("relative_humidity_2m", 0)
                val weatherCodeInt = currentObj.optInt("weather_code", 0)
                val weatherCode = WeatherCodes.fromCode(weatherCodeInt,weather = null)
                val precipitation = currentObj.optInt("precipitation", 0)
                val windSpeed = currentObj.optInt("wind_speed_10m", 0)
                val windDirection = currentObj.optInt("wind_direction_10m", 0)
                val apparentTemperature = currentObj.optDouble("apparent_temperature", 0.0)

                val hourlyList = mutableListOf<HourlyWeather>()
                // Werte aus "hourly"-Objekt extrahieren:
                val hourlyTimes = hourlyObj.optJSONArray("time")
                if (hourlyTimes != null) {

                    for (i in 0 until hourlyTimes.length()) {
                        val timeString = hourlyTimes.optString(i, "")
                        val time = if (timeString.isNotEmpty()) {
                            try { LocalDateTime.parse(timeString) } catch (e: Exception) { LocalDateTime.now() }
                        } else {
                            LocalDateTime.now()
                        }
                        hourlyList.add(HourlyWeather(
                            times = time,
                            temperature2M = hourlyObj.optJSONArray("temperature_2m")?.optDouble(i) ?: 0.0,
                            relativeHumidity2M = hourlyObj.optJSONArray("relative_humidity_2m")?.optInt(i) ?: 0,
                            apparentTemperature2M = hourlyObj.optJSONArray("apparent_temperature")?.optDouble(i) ?: 0.0,
                            precipitation = hourlyObj.optJSONArray("precipitation")?.optDouble(i) ?: 0.0,
                            windSpeed = hourlyObj.optJSONArray("wind_speed_10m")?.optDouble(i) ?: 0.0,
                            windDirection = hourlyObj.optJSONArray("wind_direction_10m")?.optInt(i) ?: 0,
                            //weatherCode = hourlyWeatherCode.optInt(i),
                            weatherCode = WeatherCodes.fromCode(
                                hourlyObj.optJSONArray("weather_code")?.optInt(i) ?: 0,weather = null
                            ),
                            freezingLevel = hourlyObj.optJSONArray("freezing_level_height")?.optDouble(i) ?: 0.0))
                            //snowfallLevel = hourlySnowfallHeight.optDouble(i)
                    }
                }

                val dailyList = mutableListOf<DailyWeather>()
                // Werte aus "daily"-Objekt extrahieren:
                val dailyTimes = dailyObj.optJSONArray("time")
                if (dailyTimes != null) {

                    for (i in 0 until dailyTimes.length()) {
                        //Datum sicher parsen:
                        val dateString = dailyTimes.optString(i, "")
                        val date = if (dateString.isNotEmpty()) {
                            try { LocalDate.parse(dateString) } catch (e: Exception) { LocalDate.now() }
                        } else { LocalDate.now() }
                    dailyList.add(DailyWeather(
                        time = date,
                        temperatureMin = dailyObj.optJSONArray("temperature_2m_min")?.optDouble(i) ?: 0.0,
                        temperatureMax = dailyObj.optJSONArray("temperature_2m_max")?.optDouble(i) ?: 0.0,
                        apparentTemperatureMin = dailyObj.optJSONArray("apparent_temperature_min")?.optDouble(i) ?: 0.0,
                        apparentTemperatureMax = dailyObj.optJSONArray("apparent_temperature_max")?.optDouble(i) ?: 0.0,
                        sunrise = parseDateTimeSafely(dailyObj.optJSONArray("sunrise")?.optString(i)),
                        sunset = parseDateTimeSafely(dailyObj.optJSONArray("sunset")?.optString(i)),
                        weatherCode = WeatherCodes.fromCode(
                            dailyObj.optJSONArray("weather_code")?.optInt(i) ?: 0, weather = null
                        )
                    ))
                    }
                }

                result = Weather(location,temperature, humidity, weatherCode, precipitation, windSpeed, windDirection, apparentTemperature, hourlyList, dailyList)

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

    override fun getLocations(searchText: String) : MutableList<Location> {
        val results = mutableListOf<Location>()
        val text = URLEncoder.encode(searchText, "UTF-8")
        val apiUrl = "https://geocoding-api.open-meteo.com/v1/search?name=$text&count=20&language=de&format=json&countryCode=CH"

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

                if (responseText.has("results")) {
                    val resultsArray = responseText.getJSONArray("results")

                    for (i in 0 until resultsArray.length()) {
                        val item = resultsArray.getJSONObject(i)

                        // 1. Merkmale extrahieren
                        val latitude = item.optDouble("latitude", 0.0)
                        val longitude = item.optDouble("longitude", 0.0)
                        val name = item.optString("name", "unbekannter Ort")
                        val kanton = item.optString("admin1", "")
                        val bezirk = item.optString("admin2", "")
                        val gemeinde = item.optString("admin3", "")
                        val elevation = item.optDouble("elevation", 0.0)
                        val id = item.optLong("id", 0L).toInt()

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
        return results
    }

    private fun parseDateTimeSafely(dateTimeString: String?): LocalDateTime {
        return if (!dateTimeString.isNullOrEmpty()) {
            try {
                // Versucht den String zu parsen
                LocalDateTime.parse(dateTimeString)
            } catch (e: Exception) {
                // Falls das Format falsch ist: Aktuelle Zeit als Sicherheitsnetz
                LocalDateTime.now()
            }
        } else {
            // Falls der String null oder leer ist: Aktuelle Zeit
            LocalDateTime.now()
        }
    }
}