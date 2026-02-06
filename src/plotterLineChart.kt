import javafx.application.Platform.runLater
import javafx.geometry.Side
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series

/* Tag und Temperatur als LineChart anzeigen */

object plotterLineChart {
    private val xAxis = NumberAxis()
    private val yAxis = NumberAxis()
    private var chart = LineChart(xAxis, yAxis)
    private val series: MutableList<Series<Number, Number>> = mutableListOf()
    private var counter = 0.0
    private val MAX_SHOWN_VALUES = 30

    fun getView(): LineChart<Number, Number> {
        xAxis.label = "Wochentag"
        yAxis.label = "Temperatur [C]"

        with(chart) {
            maxHeight = 200.0
            legendSide = Side.LEFT
            createSymbols = false
            animated = false
        }
        createAllSeries()
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
        createSerie("Min Temperatur")
        createSerie("Max Temperatur")
    }

    private fun createSerie(name: String) {
        val serie = Series<Number, Number>()
        serie.name = name
        chart.data.add(serie)
        series += serie
    }

    private fun updateSerie(serie: Series<Number, Number>, value: Int) {
        runLater {
            val data = XYChart.Data<Number, Number>(counter.toInt(), value)
            serie.data.add(data)
            if (serie.data.size > MAX_SHOWN_VALUES) {
                serie.data.removeAt(0)
            }
            counter += 1.0 / series.size // Passt für 1 Sekunden Zyklus des Supervisors
        }
    }

    private fun doNothing() {
    }
}
