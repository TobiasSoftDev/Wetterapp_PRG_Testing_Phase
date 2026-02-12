import javafx.application.Application.*
import javafx.scene.image.Image
import javax.lang.model.type.NullType
import kotlin.collections.MutableList


/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Main
*/

fun main(){

    val weather = Weather(
        location = Location(0.0, 0.0, "default", "default", "default", "default", 0.0, 0U),
        temperature = 0.0,
        humidity = 0,
        weatherCode = WeatherCodes.UNBEKANNT,
        precipitation = 0,
        windSpeed = 0,
        windDirection = 0,
        apparentTemperature = 0.0,
        hourlyList = listOf<HourlyData>(),
        dailyList = listOf<DailyData>()
    )
//    val favorites = Favorite(
//        location = Location(0.0, 0.0, "default", "default", "default", "default", 0.0, 0U),
//        name = "Default",
//        temperature = 0.0,
//        icon =  Image("")
//        )

    val storage: Storabledata = WeatherData()
    val hourlyData = weather.getHourlyList()
    val dailyData = weather.getDailyList()

// Zu Testzwecken Direckausgabe auf Konsole
    println("Daily: ${storage.storeWeatherDataDaily(weather)}")
    println("Hourly: ${storage.storeWeatherDataHourly(weather)}")
    println("Current: ${storage.storeWeatherData(weather)}")
//    println("Favorite: ${storage.storeFavorites(favorites)}")

    launch(Gui::class.java)

//    val myApiHandler = ApiHandler()
//    val myTest = Manager(myApiHandler)
//    myTest.fetchLocations("Wil")
//    val loc = myTest.pickLocation("Wil", 1)
//    val test = myTest.getCurrentWeather(loc)
//    println("$test")
    
    




}


