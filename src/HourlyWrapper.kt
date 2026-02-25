/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        P.Koch

  Beschreibung: Hier werden die stündlichen Wetterdaten für das Speichern in den Files in Listen gebuendelt und abgepackt.
 */

data class HourlyWrapper(
    var wrapperTemperature: Double = 0.0,
    var wrapperWeatherCode: Int = 0)
