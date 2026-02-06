import javax.swing.plaf.nimbus.State

enum class WeatherCodes(
                        val code: Int,
                        val description: String,
                        val icon: String) {

    SONNIG(0,"Sonne", "☀️"),
    LEICHT_BEWOELKT(1, "leicht bewölkt", "⛅"),
    BEWOELKT(3, "Bewölkt", "☁️"),
    NEBEL(40, "Nebel", "🌫️"),
    LEICHTER_REGEN(50, "leichter Regen", "🌦️"),
    REGEN(60, "Regen", "🌧️"),
    SCHNEE(70, "Schnee", "🌨️"),
    GEWITTER(90, "Gewitter", "⛈️"),
    UNBEKANNT(-1, "Unbekannt", "?");

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