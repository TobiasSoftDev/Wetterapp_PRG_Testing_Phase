import javafx.scene.image.Image
import java.io.Serializable

/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        T.Theiler

  Beschreibung: Hier wird die Favoriten Klasse definiert.
 */

class Favorite: Serializable {
    var location: Location = Location()
    var name: String = ""
    var temperature: Double = 0.0
    var iconFileName: String = ""

    // Dieser Konstruktor muss einmalig leer aufgerufen werden, um den Speichervorgang abschliessen zu koennen
    constructor()

    constructor(location: Location, name: String) {
        this.location = location
        this.name = name
    }

    val icon: Image
        get() {
            return if (iconFileName.isEmpty()) {
                loadIcon("umbrella.png")
            } else {
                loadIcon(iconFileName)
            }
        }
}





