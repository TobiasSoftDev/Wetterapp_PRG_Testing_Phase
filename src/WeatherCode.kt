import javax.swing.plaf.nimbus.State

enum class WeatherCode(val code: Int, val beschreibung: String, val icon: String)  {
    SONNIG(0, "Sonne", "☀️"),
    LEICHT_BEWOELKT(1-3, "leicht bewölkt", "⛅"),
    BEWOELKT(3, "Bewölkt", "☁️"),
    NEBEL(code = 40-49, "Nebel", "🌫️"),
    LEICHTER_REGEN(50-59+80-90, "leichter Regen", "🌦️"),
    REGEN(60-69, "Regen", "🌧️"),
    SCHNEE(71, "Schnee", "🌨️"),
    GEWITTER(91-99, "Gewitter", "⛈️");

    override fun toString() = "$code,$beschreibung,$icon"
}