import javafx.application.Platform.runLater
import javafx.collections.FXCollections
import javafx.geometry.Side
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import javafx.scene.control.Label
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Tag und Temperatur als LineChart anzeigen
 */

object plotterLineChart {
    private var heute: LocalDate = LocalDate.now()
    private var dayIndex = 0
    private var updateCount = 0
    private var initialized = false
    private val MAX_SHOWN_VALUES = 7
    private val weekDays = mutableListOf<String>()
    private val series: MutableList<Series<String, Number>> = mutableListOf()
    private val yAxis = NumberAxis()
    private val weather = Gui.selectedLocationWeather?.getDailyWeatherDataAll()

    /*Die init Block Funktion um das aktuelle Datum und den Wochentag sowie diese
    auf der X-Achsenbeschreibung anzuzeigen, wurde mittels Unterstützung von Claude AI erstellt.*/

    init {

        // Startet bei heute und fügt 7 Tage hinzu noch ohne bereits Daten der Serien anzeigen
            for (i in 0..6) {
                val tag = heute.plusDays(i.toLong())
                val weekDayName = tag.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.GERMAN)
                val datum = tag.format(DateTimeFormatter.ofPattern("dd.MM"))
                weekDays.add("$weekDayName\n$datum\n")
            }
        }

    private val xAxis = CategoryAxis(FXCollections.observableArrayList(weekDays))
    private var chart = LineChart(xAxis, yAxis)


    fun getView(): LineChart<String?, Number?> {
        xAxis.label = "7 Tage Wettervorhersage [day/date]"
        yAxis.label = "Temperatur [°C]"

        // Bei X- und Y-Achse Labels die gewuenschte Schriftgroesse von appStyle verwenden
        runLater {
            (xAxis.lookup(".axis-label") as? Label)?.let {appStyle.layoutLabelBottomRight(it) }
            (yAxis.lookup(".axis-label") as? Label)?.let {appStyle.layoutLabelBottomRight(it) }
        }

        with(chart) {
            maxHeight = 350.0
            legendSide = Side.LEFT
            createSymbols = false
            animated = false

        }

        // um die Legende zu einmalig zu erstellen, damit doppelte Beschriftungen nicht mehr auftreten
        if (!initialized) {
            createAllSeries()
            initialized = true
        }

        // Y-Achse anpassen
        if (weather != null) {
            val maxTemp = weather.maxOf { it.getTemperatureMax() }
            val minTemp = weather.minOf { it.getTemperatureMin() }

            yAxis.isAutoRanging = false
            yAxis.upperBound = maxTemp + 5.0
            yAxis.lowerBound = minTemp - 5.0
        }

        return chart
    }

    fun plot(name: String, value: Int) {
                when (name) {
            "Max Temperatur"   -> updateSerie(series[0], value)
            "Min Temperatur"   -> updateSerie(series[1], value)
            else -> println("WARNUNG Plotter: unbekannte Datenserie: $name")
        }

    }
    private fun createAllSeries() {
        createSerie("Maximal\nTemperatur")
        createSerie("Minimal\nTemperatur")
    }

    private fun createSerie(name: String) {
        val serie = Series<String, Number>()
        serie.name = name
        chart.data.add(serie)
        series += serie
    }

    private fun updateSerie(serie: Series<String, Number>, value: Int) {
        runLater {

            val currentDay = weekDays[dayIndex % 7]
            val data = XYChart.Data<String, Number>(currentDay, value)

            // Ergaenzung um Temperatur-Label als Node zu setzen (Erstellung wurde mittels Claude AI unterstützt)
            // von hier
            val label = Label("${value}°C")
            // Beschriftung wird direkt im Objekt definiert, da es zusätzliche grafische Formatierungsmerkmale gebraucht hat. Nicht nur klassisches Label
            label.style = "-fx-font-size: 11px; -fx-font-weight: bold; -fx-opacity: 0.65;-fx-font-family: 'Helvetica'; -fx-text-fill: ${if (serie == series[0]) "brown" else "black"}; -fx-border-width: 0; -fx-background-color: white;"
            data.node = label
            // bis hier

            serie.data.add(data)
            if (serie.data.size > MAX_SHOWN_VALUES) {
                serie.data.removeAt(0)
            }

            data.node?.let { node ->
                node.translateY = 0.0    // 7px nach oben verschieben
                node.translateX = 5.0    // 5px nach rechts verschieben
            }

            // Nur nach dem Update beider Serien zum nächsten Tag
            updateCount++
            if (updateCount >= series.size) {
                dayIndex++
                updateCount = 0
            }
        }
    }
}
