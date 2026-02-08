import javafx.application.Platform.runLater
import javafx.collections.FXCollections
import javafx.geometry.Side
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


/* Tag und Temperatur als LineChart anzeigen */

object plotterLineChart {
    private var heute: LocalDate = LocalDate.now()
    private var wochentag: DayOfWeek = heute.getDayOfWeek()
    private var dayIndex = 0
    private var updateCount = 0
    private var initialized = false
    private val MAX_SHOWN_VALUES = 7
    private val weekDays = mutableListOf<String>()
    private val series: MutableList<Series<String, Number>> = mutableListOf()
    private val yAxis = NumberAxis()

    /*Die init Block Funktion um das aktuelle Datum und den Wochentag
    auf der X-Achsenbeschreibung anzuzeigen, wurde mittels Claude AI erstellt.*/

    init {
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")

        // Starte bei heute und füge 7 Tage hinzu
        for (i in 0..6) {
            val tag = heute.plusDays(i.toLong())
            val weekDayName = tag.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMAN)
            val datum = tag.format(DateTimeFormatter.ofPattern("dd.MM"))

            weekDays.add("$weekDayName\n$datum")
        }
    }

    private val xAxis = CategoryAxis(FXCollections.observableArrayList(weekDays))
    private var chart = LineChart(xAxis, yAxis)


    fun getView(): LineChart<String?, Number?> {
        xAxis.label = "Wochentag"
        yAxis.label = "Temperatur [C]"

        with(chart) {
            maxHeight = 200.0
            legendSide = Side.LEFT
            createSymbols = false
            animated = false

        }
        // um die Legende zu einmalig zu erstellen, damit doppelte Beschriftungen nicht mehr auftreten
        if (!initialized) {
            createAllSeries()
            initialized = true
        }

        return chart
    }

    fun plot(name: String, value: Int) {
        when (name) {
            "Min Temperatur"   -> updateSerie(series[0], value)
            "Max Temperatur"   -> updateSerie(series[1], value)
            else -> println("WARNUNG Plotter: unbekannte Datenserie: $name")
        }
    }
    private fun createAllSeries() {
        createSerie("Höchste\n Temperatur")
        createSerie("Tiefste\nTemperatur")
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
            serie.data.add(data)
            if (serie.data.size > MAX_SHOWN_VALUES) {
                serie.data.removeAt(0)
            }

            // Nur nach dem Update beider Serien zum nächsten Tag
            updateCount++
            if (updateCount >= series.size) {
                dayIndex++
                updateCount = 0
            }
        }
    }

    private fun doNothing() {
    }
}
