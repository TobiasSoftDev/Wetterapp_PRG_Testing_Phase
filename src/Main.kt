/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Main
*/


val LocationList = mutableListOf<Any>()
val WeatherList = mutableListOf<Any>()

fun main(){
    val myOrt = Location(47.37095, 8.5529785, "Zürich", "Zürich", "Zürich", "Zürich", 429.0, 2657896u)
    val myZeit = LocalDateTime.of(2026, 1, 9, 22, 0)

    val myTemperaturen: MutableList<Double> = mutableListOf()
    myTemperaturen.add(3.5)
    myTemperaturen.add(4.5)
    myTemperaturen.add(5.2)
    myTemperaturen.add(5.8)

    val myFeuchtigkeit: MutableList<Int> = mutableListOf()
    myFeuchtigkeit.add(90)
    myFeuchtigkeit.add(86)
    myFeuchtigkeit.add(79)
    myFeuchtigkeit.add(78)

    val myWeatherCode: MutableList<Int> = mutableListOf()
    myWeatherCode.add(53)
    myWeatherCode.add(3)
    myWeatherCode.add(3)
    myWeatherCode.add(3)

    val myHourlyWeatherZuerich = HourlyWeather(myOrt, myZeit, myTemperaturen, myFeuchtigkeit, myWeatherCode)
    println(myHourlyWeatherZuerich.getDataLastHour())
//    val myOrt = ApiHandler()
//    myOrt.getLocations("Wil")
//
//    val dataFile = File()
//    dataFile.startFileInterface()
//
//    LocationList.add("Thun")
//    LocationList.add(46.7590)
//    LocationList.add(7.6300)
//    dataFile.readStoredLocation()
//
//    WeatherList.add("Sonnenschein")
//    WeatherList.add("teilweise bewölkt")
//    WeatherList.add(3)
//    WeatherList.add(5.0)
//    dataFile.readStoredWeather()
//    println(WeatherCode.BEWOELKT)

}