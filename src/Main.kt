/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Main
*/


fun main(){
    val myApiHandler = ApiHandler()
    val myTest = Manager(myApiHandler)
    myTest.fetchLocations("Wil")
    myTest.pickLocation("Wil", 1)

}