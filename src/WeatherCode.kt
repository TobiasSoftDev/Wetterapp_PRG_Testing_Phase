import javafx.scene.image.Image

private fun loadIcon(fileName : String): Image {
    val path = "/icons/weatherIcons/$fileName"
    val resource = WeatherCodes::class.java.getResourceAsStream(path)

    return if (resource!= null) {
        Image(resource)
    } else {
        println("Iconpfad nicht gefunden!")
        Image("/icons/weatherIcons/umbrella.png")// Image("https://via.placeholder.com/64")
    }
}

enum class WeatherCodes(
    val code: Int,
    val description: String,
    val icon: Image) {

    SONNIG(0, "Sonne", loadIcon("sunny.png")),
    LEICHT_BEWOELKT(1, "leicht bewölkt", loadIcon("partly_cloudy.png")),
    BEWOELKT(3, "Bewölkt", loadIcon("cloudy.png")),
    NEBEL(40, "Nebel", loadIcon("fog.png")),
    LEICHTER_REGEN(50, "leichter Regen", loadIcon("light_rain.png")),
    REGEN(60, "Regen", loadIcon("rain.png")),
    SCHNEE(70, "Schnee", loadIcon("snow.png")),
    GEWITTER(90, "Gewitter", loadIcon("thunderstorm.png")),
    UNBEKANNT(-1, "Unbekannt", loadIcon("unknown.png"));


    companion object {

        fun fromCode(code: Int): WeatherCodes {
            // Zuerst exakte Übereinstimmung suchen (z.B. 45, 51, 61)
            entries.find { it.code == code }?.let { return it }

            // Dann Range-Checks für Bereiche
            return when (code) {
                in 1..2 -> LEICHT_BEWOELKT
                in 3..39 -> BEWOELKT
                in 40..49 -> NEBEL
                in 50..59 -> LEICHTER_REGEN
                in 60..69 -> REGEN
                in 70..79 -> SCHNEE
                in 90..99 -> GEWITTER
                else -> UNBEKANNT
            }
        }

    }
}