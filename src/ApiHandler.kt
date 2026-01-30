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
                val temperature = currentObj.getDouble("temperature_2m")
                val humidity = currentObj.getInt("relative_humidity_2m")
                //val weatherCode = currentObj.getInt("weather_code")
                val weatherCodeInt = currentObj.getInt("weather_code")
                val weatherCode = WeatherCodes.fromCode(weatherCodeInt)
                val precipitation = currentObj.getInt("precipitation")
                val windSpeed = currentObj.getInt("wind_speed_10m")
                val windDirection = currentObj.getInt("wind_direction_10m")
                val apparentTemperature = currentObj.getDouble("apparent_temperature")


// Werte aus "hourly"-Objekt extrahieren:
                val hourlyTimes = hourlyObj.getJSONArray("time")
                val hourlyTemps = hourlyObj.getJSONArray("temperature_2m")
                val hourlyHumidity = hourlyObj.getJSONArray("relative_humidity_2m")
                val hourlyApparentTemperature = hourlyObj.getJSONArray("apparent_temperature")
                val hourlyPrecipitation = hourlyObj.getJSONArray("precipitation")
                val hourlyWindSpeed = hourlyObj.getJSONArray("wind_speed_10m")
                val hourlyWindDirection = hourlyObj.getJSONArray("wind_direction_10m")
                val hourlyWeatherCode = hourlyObj.getJSONArray("weather_code")
                val hourlyFreezingLevelHeight = hourlyObj.getJSONArray("freezing_level_height")
                //val hourlySnowfallHeight = hourlyObj.getJSONArray("snowfall_height")

// HourlyData-Objekte mit "hourly"-Daten befüllen:
                val hourlyList = mutableListOf<HourlyData>()
                for (i in 0 until hourlyTimes.length()-1) {
                    hourlyList.add(HourlyData(
                        times = LocalDateTime.parse(hourlyTimes.getString(i)),
                        temperature2M = hourlyTemps.optDouble(i),
                        relativeHumidity2M = hourlyHumidity.optInt(i),
                        apparentTemperature2M = hourlyApparentTemperature.optDouble(i),
                        precipitation = hourlyPrecipitation.optDouble(i),
                        windSpeed = hourlyWindSpeed.optDouble(i),
                        windDirection = hourlyWindDirection.optInt(i),
                        //weatherCode = hourlyWeatherCode.optInt(i),
                        weatherCode = WeatherCodes.fromCode(hourlyWeatherCode.optInt(i)),
                        freezingLevel = hourlyFreezingLevelHeight.optDouble(i),
                        //snowfallLevel = hourlySnowfallHeight.optDouble(i)

                    ))
                }

// Werte aus "daily"-Objekt extrahieren:
                val dailyTimes = dailyObj.getJSONArray("time")
                val dailyWeatherCodes = dailyObj.getJSONArray("weather_code")
                val dailyTemperatureMin = dailyObj.getJSONArray("temperature_2m_min")
                val dailyTemperatureMax = dailyObj.getJSONArray("temperature_2m_max")
                val dailyApparentTemperatureMin = dailyObj.getJSONArray("apparent_temperature_min")
                val dailyApparentTemperatureMax = dailyObj.getJSONArray("apparent_temperature_max")
                val dailySunset = dailyObj.getJSONArray("sunset")
                val dailySunrise = dailyObj.getJSONArray("sunrise")

                // DailyData-Objekte mit "hourly"-Daten befüllen:
                val dailyList = mutableListOf<DailyData>()
                for (i in 0 until dailyTimes.length()-1) {
                    dailyList.add(DailyData(
                        time = LocalDate.parse(dailyTimes.getString(i)),
                        temperatureMin = dailyTemperatureMin.optDouble(i),
                        temperatureMax = dailyTemperatureMax.optDouble(i),
                        apparentTemperatureMin = dailyApparentTemperatureMin.optDouble(i),
                        apparentTemperatureMax = dailyApparentTemperatureMax.optDouble(i),
                        sunset = LocalDateTime.parse(dailySunset.getString(i)),
                        sunrise = LocalDateTime.parse(dailySunrise.getString(i)),
//                        weatherCode = dailyWeatherCodes.optInt(i),
                        weatherCode = WeatherCodes.fromCode(dailyWeatherCodes.optInt(i))

                    ))

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