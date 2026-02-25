/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        P.Theiler

  Beschreibung: Funktion um Location Angaben aus der Suchfunktion auszulesen und im Code bereitzustellen.
 */

data class Location(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var name: String = "",
    var kanton: String = "",
    var bezirk: String = "",
    var gemeinde: String = "",
    var elevation: Double = 0.0,
    var id: Int = 0){

   fun getLocationForSearchResult() = "$name ($kanton), Gemeinde: $gemeinde"





}