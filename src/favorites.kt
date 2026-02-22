import javafx.scene.image.Image
import java.io.Serializable

class Favorite: Serializable {
    var location: Location = Location()
    var name: String = ""
    var temperature: Double = 0.0
    var iconFileName: String = ""

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





