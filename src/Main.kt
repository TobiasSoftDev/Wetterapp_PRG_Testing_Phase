/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Main
*/


val LocationList = mutableListOf<Any>()
val WeatherList = mutableListOf<Any>()

fun main(){
    val myTest = Manager()

    val myOrts = myTest.fetchLocations("Wil")
    val myOrt = myOrts[0]

    myTest.fetchCurrentWeather(myOrt)

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