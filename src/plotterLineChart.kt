import javafx.application.Platform.runLater
import javafx.collections.FXCollections
import javafx.geometry.Side
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series

/* Tag und Temperatur als LineChart anzeigen */

object plotterLineChart {
    private val weekDays = listOf("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag")
    private val xAxis = CategoryAxis(FXCollections.observableArrayList(weekDays))
    private val yAxis = NumberAxis()
    private var chart = LineChart(xAxis, yAxis)
    private val series: MutableList<Series<String, Number>> = mutableListOf()
    private var dayIndex = 0
    private val MAX_SHOWN_VALUES = 7
    private var updateCount = 0
    private var initialized = false

    fun getView(): LineChart<String?, Number?> {
        xAxis.label = "Wochentag"
        yAxis.label = "Temperatur [C]"

        with(chart) {
            maxHeight = 200.0
            legendSide = Side.LEFT
            createSymbols = false
            animated = false

        }

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
        createSerie("Tiefste\nTemperatur")
        createSerie("Höchste\n Temperatur")
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
