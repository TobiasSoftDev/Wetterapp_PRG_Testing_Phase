import javafx.application.Application
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.BorderPane
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import plotterLineChart.plot
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.round


class Gui : Application() {
    private val manager: Guilogic = Manager()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var locationsModel = FXCollections.observableArrayList<Location>()
    private val resultsList = ListView<Location>().apply {
        prefWidth = 400.0
        items = locationsModel
// Cell-Factory-Code online unter https://openjfx.io/javadoc/12/javafx.controls/javafx/scene/control/Cell.html
// Damit die Auswahlliste nicht die gesamten Objekt-Informationen anzeigt, sondern einen bestimmten, von uns festgelegten String.
        setCellFactory {
            object : ListCell<Location>() {
                init {
                    // Effekt beim Betreten mit der Maus
                    setOnMouseEntered {
                        if (!isEmpty) {
                            style = "-fx-background-color: #D6E8FF;"
                        }
                    }
                    // Effekt beim Verlassen mit der Maus
                    setOnMouseExited {
                        style = "" // Setzt den Style auf Standard (CSS) zurück
                    }
                }
                override fun updateItem(item: Location?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty || item == null) {
                        text = null
                    } else {
                        text = item.getLocationForSearchResult()
                        cursor = Cursor.HAND
                        hoverProperty()
                    }
                }
            }
        }
    }

    private val onHomeClick = { location: Location ->
        scope.launch {
            selectedLocation = location
            val weather = withContext(Dispatchers.IO) {
                manager.getCurrentWeather(location)
            }
            selectedLocationWeather = weather
            fillInLocationData(selectedLocation)
            fillInWeatherData(selectedLocationWeather)
            val favList = manager.getFavoritesObservableList()
            val activeLocation = favList.find { it.location.id == location.id }
            if (activeLocation != null) {
                favList.remove(activeLocation)
                favList.add(0, activeLocation)
                withContext(Dispatchers.IO) {
                    manager.updateFavoriteFile()
                }
            }
        }
    }

    private val hBoxBottom = HBox().apply {
        alignment = Pos.CENTER
        padding = Insets(30.0)
        detailsView.getView().prefWidthProperty().bind(this.widthProperty().multiply(0.4))
        plotterLineChart.getView().prefWidthProperty().bind(this.widthProperty().multiply(0.6))
        children.addAll(detailsView.getView(), plotterLineChart.getView())
    }

    private val hBoxSearchAccuracy = HBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 8.0
        padding = Insets(30.0)
        // Breite der Sucheingabe = Breite der Detailsansicht (Haarlinie)
        guiSearchbar.tflSucheingabe.prefWidthProperty().bind(detailsView.getView().widthProperty().subtract(45.0))
        accuracyBox.getView().prefWidthProperty().bind(this.widthProperty().multiply(0.5))
        accuracyBox.infoBtn.onAction = EventHandler {event ->
            val source = (event.source as Node).scene.window as Stage
            accuracyBox.showInfoPopup(source)
        }
        guiSearchbar.tflSucheingabe.minWidthProperty().bind(guiSearchbar.tflSucheingabe.prefWidthProperty())
        guiSearchbar.tflSucheingabe.maxWidthProperty().bind(guiSearchbar.tflSucheingabe.prefWidthProperty())

        search(guiSearchbar.btnSuche)
        searchTfl(guiSearchbar.tflSucheingabe)

        children.addAll(guiSearchbar.getView(), accuracyBox.getView())
    }

    private val hBoxDayViewFavorites by lazy {
        HBox().apply {
            alignment = Pos.TOP_LEFT
            padding = Insets(30.0)
            isFillHeight = false

            val spacer = Region().apply {
                HBox.setHgrow(this, Priority.ALWAYS)
            }

            val favBox = guiFavorites.createFavoriteBox(onHomeClick)
            favBox.apply {
                minHeight = Region.USE_PREF_SIZE
                maxHeight = Region.USE_PREF_SIZE
                style = """
                -fx-background-color: #fcfcfc; 
                -fx-background-radius: 20; 
                -fx-border-color: #e0e0e0; 
                -fx-border-radius: 20;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);
            """.trimIndent()
                VBox.setVgrow(this, Priority.NEVER)
            }
            //HBox.setHgrow(dayView.hBoxDayView, Priority.ALWAYS)
            //HBox.setHgrow(favBox, Priority.ALWAYS)
            dayView.hBoxDayView.maxWidthProperty().bind(this.widthProperty().multiply(0.6))
            //favBox.maxWidthProperty().bind(this.widthProperty().multiply(0.5))
           // HBox.setMargin(favBox, Insets(0.0, 0.0, 0.0, 200.0))
            children.addAll(dayView.hBoxDayView,spacer, favBox)
        }
    }

    override fun start(stage: Stage) {
        guiFavorites.manager = this.manager
        dayView.favorites = guiFavorites
        dayView.addFavoriteButtonToBox()
        guiFavorites.updateFavoritesList(onHomeClick)
        manager.getFavoritesObservableList().addListener(javafx.collections.ListChangeListener{
        guiFavorites.updateFavoritesList(onHomeClick)})

        val root = BorderPane().apply {
            top = hBoxSearchAccuracy
            bottom = hBoxBottom
            center = hBoxDayViewFavorites
            background = Background(BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets(0.0, 0.0, 0.0, 0.0)))
            isFocusTraversable = true   // Nimmt den Cursor aus dem Textfeld. Textfeld will Aufmerksamkeit haben...
        }
        //dayView.getView().prefWidthProperty().bind(root.widthProperty().multiply(0.50))
        dayView.getView().minWidth = 200.0
        hBoxBottom.prefHeightProperty().bind(root.heightProperty().multiply(0.45))
        hBoxBottom.minHeight = 100.0

        with(stage) {
            scene = Scene(root)
            title = "Weather2B"
            isMaximized = true
            setOnCloseRequest { exit() }
            show()
            root.requestFocus()     // mit Tab-Taste krallt sich Textfeld wieder an die Aufmerksamkeit -> Cursor...
        }
        val currentFavorites = manager.getFavoritesObservableList()
        if (currentFavorites.isNotEmpty()) {
            val topFavorite = currentFavorites[0]
            onHomeClick(topFavorite.location)
        }
    }

    override fun stop() {
        scope.cancel()
        super.stop()
    }

    private fun fillInLocationData(location: Location?) {
            dayView.lblLocation.text = selectedLocation?.name
            if (location != null) {
                dayView.pinPosition(dayView.calculatePosition(location.latitude, location.longitude))
                detailsView.lblDetailsTitle.text = "Details für ${location.name}"
            }
    }

    private fun fillInWeatherData(weather: Weather?) {
        if (weather != null) {
            fillAccuracyBox(weather)
            fillDayView(weather)
            fillDetailsView(weather)
            fillPlotterView(weather)
        }
        guiFavorites.updateStarColor(selectedLocation)
        dayView.btnAddFavorite.isVisible = true
    }

    private fun fillAccuracyBox(weather: Weather) {
        scope.launch {
            val score = withContext(Dispatchers.Default) {
                manager.checkAccuracy(weather.getLocationID(), weather)
            }
            if (score != null) {
                accuracyBox.percentLbl.text = "$score%"
                accuracyBox.descriptionLbl.text =
                    accuracyBox.fillAccuracyLabel(score)
            } else {
                accuracyBox.percentLbl.text = ""
                accuracyBox.descriptionLbl.text = "Es ist noch keine Berechnung erfolgt."
            }
        }
    }

    private fun fillDayView(weather: Weather) {
        dayView.lblWeatherCode.text = weather.getWeatherCode().description
        dayView.lblTemperature.text = "${weather.getTemperature().toInt()}º"
        dayView.lblMaxTemperature.text = "${round(weather.getDailyList()[0].getTemperatureMax()).toInt()}º"
        dayView.lblMinTemperature.text = "${round(weather.getDailyList()[0].getTemperatureMin()).toInt()}º"
        dayView.lblUpdateTime.text = "aktualisiert um: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))} Uhr"
    }

    private fun fillDetailsView(weather: Weather) {
        detailsView.lblHumidityValue.text = "${weather.getHumidity()}%"
        detailsView.lblPrecipitationValue.text = "${weather.getPrecipitation()} mm"
        detailsView.lblSunriseValue.text = "${weather.getDailyList()[0].getSunrise()} Uhr"
        detailsView.lblSunsetValue.text = "${weather.getDailyList()[0].getSunset()} Uhr"
        detailsView.lblWindSpeedValue.text = "${weather.getWindSpeed()} km/h"
        detailsView.updateWindDirection(weather.getWindDirection())
        detailsView.lblApparentTemperatureValue.text = "${round(weather.getApparentTemperature()).toInt()}º"

    }

    private fun fillPlotterView(weather: Weather) {
        // API-Daten neu laden für Plotter Line Chart und erste 7 Werte auslesen
        weather.getDailyWeatherDataAll().take(7).forEach { day ->
            plot("Max Temperatur", day.getTemperatureMax().toInt())
            plot("Min Temperatur", day.getTemperatureMin().toInt())
        }
    }

    private fun fillSearchResults(string: String) {
        scope.launch {
            val results = withContext(Dispatchers.IO) {
                manager.getLocations(string)
            }
            locationsModel.clear()
            results.forEach { locationsModel.add(it) }
        }
    }

    private fun useSearchbar(stage: Stage) {
        guiSearchbar.popupLogic(stage, resultsList) { selected ->
            selectedLocation = selected
            selectedLocationWeather = manager.getCurrentWeather(selected)
            fillInLocationData(selectedLocation)
            fillInWeatherData(selectedLocationWeather)
        }
    }

    private fun search(with: Button) {
        with(with) {
            onAction = EventHandler { event ->
                fillSearchResults(guiSearchbar.tflSucheingabe.text)
                val source = (event.source as Node).scene.window as Stage
                useSearchbar(source)
            }
        }
    }

    private fun searchTfl(field: TextField) {
        with(field) {
            onAction = EventHandler { event -> fillSearchResults(this.text)
                val source = (event.source as Node).scene.window as Stage
                useSearchbar(source)
            }
        }
    }

    fun exit() {
        with(Alert(Alert.AlertType.INFORMATION)) {
        contentText = "Wenn du die App schliessen willst, drücke auf «OK»."
        showAndWait()
    }
}

    companion object {
        var selectedLocation: Location? = null
        var selectedLocationWeather: Weather? = null
    }

}