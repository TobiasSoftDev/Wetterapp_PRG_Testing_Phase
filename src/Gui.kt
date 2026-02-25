import javafx.application.Application
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Border
import javafx.scene.layout.BorderPane
import javafx.scene.layout.BorderStroke
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.layout.BorderWidths
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.stage.Modality
import javafx.stage.Stage
import kotlinx.coroutines.*
import plotterLineChart.plot
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.round

//fun exit() {
//    with(Alert(Alert.AlertType.INFORMATION)) {
//        contentText = "Wenn du die App schliessen willst, drücke auf «OK»."
//        showAndWait()
//    }
//}
class Gui : Application() {
    private val manager: Logic = Manager()

    //private val favorites: guiFavorites = GuiFavorites(manager)
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

            selectedLocation = location
            selectedLocationWeather = manager.getCurrentWeather(location)
            fillInLocationData(selectedLocation)
            fillInWeatherData(selectedLocationWeather)

            val favList = manager.getFavoritesObservableList()
            val activeLocation = favList.find { it.location.id == location.id }
            if (activeLocation != null) {
                favList.remove(activeLocation)
                favList.add(0, activeLocation)
                manager.updateFavoriteFile()
            }
    }


    private val lblProzent = Label("").apply {
        alignment = Pos.CENTER
        font = appStyle.FONT_16
        //background = Background(BackgroundFill(Color.BLUE, null, null))
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
        searchbar.tflSucheingabe.prefWidthProperty().bind(detailsView.getView().widthProperty().subtract(45.0))
        accuracyBox.getView().prefWidthProperty().bind(this.widthProperty().multiply(0.5))
        accuracyBox.infoBtn.onAction = EventHandler {event ->
            val source = (event.source as Node).scene.window as Stage
            accuracyBox.showInfoPopup(source)
        }
        searchbar.tflSucheingabe.minWidthProperty().bind(searchbar.tflSucheingabe.prefWidthProperty())
        searchbar.tflSucheingabe.maxWidthProperty().bind(searchbar.tflSucheingabe.prefWidthProperty())

        search(searchbar.btnSuche)
        searchTfl(searchbar.tflSucheingabe)

        children.addAll(searchbar.getView(), accuracyBox.getView())
    }

    private val hBoxDayViewFavorites by lazy {
        HBox().apply {
            alignment = Pos.TOP_LEFT
            padding = Insets(30.0)
            isFillHeight = false
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
                //padding = Insets(.0)
                //maxHeightProperty().bind(dayView.hBoxDayView.heightProperty())
                alignment = Pos.TOP_CENTER
                VBox.setVgrow(this, Priority.NEVER)
            }
            HBox.setHgrow(dayView.hBoxDayView, Priority.ALWAYS)
            //HBox.setHgrow(favBox, Priority.ALWAYS)
            dayView.hBoxDayView.maxWidthProperty().bind(this.widthProperty().multiply(0.5))
            //favBox.maxWidthProperty().bind(this.widthProperty().multiply(0.5))
            HBox.setMargin(favBox, Insets(0.0, 0.0, 0.0, 100.0))
            children.addAll(dayView.hBoxDayView, favBox)
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
        dayView.getView().prefWidthProperty().bind(root.widthProperty().multiply(0.50))
        dayView.getView().minWidth = 200.0
        hBoxBottom.prefHeightProperty().bind(root.heightProperty().multiply(0.45))
        hBoxBottom.minHeight = 100.0

        with(stage) {
            scene = Scene(root)
            title = "Weather2B"
            isMaximized = true
            //setOnCloseRequest { exit() }
            show()
            root.requestFocus()     // mit Tab-Taste krallt sich Textfeld wieder an die Aufmerksamkeit -> Cursor...
        }
        val currentFavorites = manager.getFavoritesObservableList()
        if (currentFavorites.isNotEmpty()) {
            val topFavorite = currentFavorites[0]
            onHomeClick(topFavorite.location)
        }
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
        val score = manager.checkAccuracy(weather.getLocationID(), weather)
            if (score != null) {
                accuracyBox.percentLbl.text = "$score%"
                accuracyBox.descriptionLbl.text = accuracyBox.fillAccuracyLabel(score)
            } else {
                accuracyBox.percentLbl.text = ""
                accuracyBox.descriptionLbl.text = "Es ist noch keine Berechnung erfolgt."
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
            val search = manager.getLocations(string)
            locationsModel.clear()
            for (result in search) {
                locationsModel.add(result)
        }
    }

    private fun useSearchbar(stage: Stage) {
        searchbar.popupLogic(stage, resultsList) { selected ->
            selectedLocation = selected
            selectedLocationWeather = manager.getCurrentWeather(selected)
            fillInLocationData(selectedLocation)
            fillInWeatherData(selectedLocationWeather)
        }
    }

    private fun search(with: Button) {
        with(with) {
            onAction = EventHandler { event ->
                fillSearchResults(searchbar.tflSucheingabe.text)
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

    companion object {
        var selectedLocation: Location? = null
        var selectedLocationWeather: Weather? = null
    }

}