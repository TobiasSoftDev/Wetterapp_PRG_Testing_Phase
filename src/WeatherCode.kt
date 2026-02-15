import javafx.scene.image.Image
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    SONNIG(0, "sonnig", loadIcon("Sonnig.png")),
    LEICHT_BEWOELKT(1, "leicht bewölkt", loadIcon("leichtbewölkt.png")),
    BEWOELKT(3, "bewölkt", loadIcon("bewölkt.png")),
    NEBEL(40, "Nebel", loadIcon("Nebel.png")),
    LEICHTER_REGEN(50, "leichter Regen", loadIcon("leichterRegen.png")),
    REGEN(60, "Regen", loadIcon("Regen.png")),
    SCHNEE(70, "Schnee", loadIcon("Schnee.png")),
    GEWITTER(91, "Gewitter", loadIcon("Gewitter.png")),

    KLAR(100, "klar", loadIcon("klarNacht.png")),
    LEICHT_BEWOELKT_NACHT(101, "leicht bewölkt", loadIcon("leichtbewölktNacht.png")),
    BEWOELKT_NACHT(103, "bewölkt", loadIcon("bewölkt.png")),
    NEBEL_NACHT(140, "Nebel", loadIcon("Nebel.png")),
    LEICHTER_REGEN_NACHT(150, "leichter Regen", loadIcon("leichterRegen.png")),
    REGEN_NACHT(160, "Regen", loadIcon("Regen.png")),
    SCHNEE_NACHT(170, "Schnee", loadIcon("Schnee.png")),
    GEWITTER_NACHT(191, "Gewitter", loadIcon("Gewitter.png")),

    UNBEKANNT(-1, "Unbekannt", loadIcon("umbrella.png"));





    companion object {
        fun fromCode(code: Int, weather: DailyWeather?): WeatherCodes {
            // Zuerst exakte Übereinstimmung suchen (z.B. 45, 51, 61)
            entries.find { it.code == code }?.let { return it }

            var weatherCode = code

            if (weather != null){

                val currentTime = LocalDateTime.now() //.format(DateTimeFormatter.ofPattern("HH:mm"))
                val sunrise = weather.getSunriseLocalDateTime()
                val sunset = weather.getSunsetLocalDateTime()
                if (currentTime.isBefore(sunrise) || currentTime.isAfter(sunset)) {
                    weatherCode += 100
                }
            }
                // Dann Range-Checks für Bereiche
                return when (weatherCode) {
                    0 -> SONNIG
                    in 1..2 -> LEICHT_BEWOELKT
                    in 3..39 -> BEWOELKT
                    in 40..49 -> NEBEL
                    in 50..59 -> LEICHTER_REGEN
                    in 60..69 -> REGEN
                    in 70..79 -> SCHNEE
                    in 80..82 -> LEICHTER_REGEN
                    in 83..90 -> SCHNEE
                    in 91..99 -> GEWITTER
                    100 -> KLAR
                    in 101..102 -> LEICHT_BEWOELKT_NACHT
                    in 103..139 -> BEWOELKT_NACHT
                    in 140..149 -> NEBEL_NACHT
                    in 150..159 -> LEICHTER_REGEN_NACHT
                    in 160..169 -> REGEN_NACHT
                    in 170..179 -> SCHNEE_NACHT
                    in 180..182 -> LEICHTER_REGEN_NACHT
                    in 183..190 -> SCHNEE_NACHT
                    in 191..199 -> GEWITTER_NACHT


                    else -> UNBEKANNT
                }
            }
        }
    }
