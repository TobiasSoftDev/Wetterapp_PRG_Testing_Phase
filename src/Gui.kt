import javafx.application.Application
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
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
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.TextAlignment
import javafx.stage.Modality
import javafx.stage.Stage
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

    var isFavorite = SimpleBooleanProperty(false)

   private var selectedLocation: Location? = null
    private var selectedLocationWeather: Weather? = null

    private val onHomeClick = { location: Location ->
        Gui.selectedLocation = location
        Gui.selectedLocationWeather = manager.getCurrentWeather(location)
        fillInLocationData(Gui.selectedLocation)
        fillInWeatherData(Gui.selectedLocationWeather)
        searchbar.tflSucheingabe.text = location.getLocationName()
    }

    private val lblProzent = Label("98%").apply {
        alignment = Pos.CENTER
        font = AppStyle.FONT_16
        //background = Background(BackgroundFill(Color.BLUE, null, null))
    }

    private val hBoxBottom = HBox().apply {
        alignment = Pos.CENTER
        padding = Insets(30.0)

        detailsView.getView().prefWidthProperty().bind(this.widthProperty().multiply(0.4))
        plotterLineChart.getView().prefWidthProperty().bind(this.widthProperty().multiply(0.6))
        children.addAll(detailsView.getView(), plotterLineChart.getView())

    }

    private val lblGuete = Label("Güte der Vorhersage").apply {
        font = AppStyle.FONT_18
    }

    private val hBoxGuete = HBox().apply {
        alignment = Pos.CENTER_RIGHT
        spacing = 8.0
        children.addAll(lblProzent, lblGuete)
    }

    private val hBoxSucheGuete = HBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 8.0
        padding = Insets(30.0)
//        HBox.setHgrow(searchbar.getView(), Priority.ALWAYS)
//        HBox.setHgrow(hBoxGuete, Priority.ALWAYS)
        hBoxGuete.prefWidthProperty().bind(this.widthProperty().multiply(0.5))
        with(searchbar.btnSuche) {
            onAction = EventHandler { event ->
                fillSearchResults(searchbar.tflSucheingabe.text)
                val source = (event.source as Node).scene.window as Stage
                popupLogic(source)
            }
        }
        with(searchbar.tflSucheingabe) {
            setOnAction { event -> fillSearchResults(this.text)
                val source = (event.source as Node).scene.window as Stage
                popupLogic(source)
            }
        }
        children.addAll(searchbar.getView(), hBoxGuete)
    }

    private val vBoxFavorites = VBox().apply {
        alignment = Pos.TOP_RIGHT
        border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
    }

    /*  private val hBoxDayViewFavorites = HBox().apply {
          alignment = Pos.TOP_LEFT
          padding = Insets(30.0)
          vBoxFavorites.prefWidthProperty().bind(this.widthProperty().multiply(0.5))
          dayView.getView().prefWidthProperty().bind(this.widthProperty().multiply(0.50))
          children.addAll(dayView.getView(), vBoxFavorites)
      } */
    /*private val hBoxDayViewFavorites = HBox().apply {
        alignment = Pos.TOP_LEFT
        padding = Insets(30.0)
        // vBoxFavorites.prefWidthProperty().bind(this.widthProperty().multiply(0.5))
        //hBoxDayView.prefWidthProperty().bind(this.widthProperty().multiply(0.50))
        //children.addAll(hBoxDayView,favorites.createFavoriteBox(manager, onHomeClick))
        val favBox = favorites.createFavoriteBox(manager, onHomeClick)
        favBox.maxHeight = 100.0
        HBox.setMargin(favBox, Insets(0.0,0.0,0.0,100.0))
        children.addAll(dayView.hBoxDayView,favBox)
    } */
    private val hBoxDayViewFavorites by lazy {
        HBox().apply {
            alignment = Pos.TOP_LEFT
            padding = Insets(30.0)
            isFillHeight = false
            val favBox = guiFavorites.createFavoriteBox(onHomeClick)
            favBox.apply {
                maxWidth = 350.0
                maxHeight = 100.0
                maxHeightProperty().bind(dayView.hBoxDayView.heightProperty())
                alignment = Pos.TOP_CENTER
                VBox.setVgrow(this, Priority.NEVER)
            }
            HBox.setHgrow(dayView.hBoxDayView, Priority.ALWAYS)

            HBox.setMargin(favBox, Insets(0.0, 0.0, 0.0, 100.0))
            children.addAll(dayView.hBoxDayView, favBox)
        }
    }

    override fun start(stage: Stage) {
        guiFavorites.manager = this.manager
        dayView.favorites = guiFavorites
        dayView.addFavoriteButtonToBox()

        val storage: Storabledata = WeatherData()
        val loadedFavorites = storage.getAllFavorites()
        manager.getFavoritesObservableList().setAll(loadedFavorites)

        guiFavorites.updateFavoritesList(onHomeClick)
        manager.getFavoritesObservableList().addListener(javafx.collections.ListChangeListener{
            guiFavorites.updateFavoritesList(onHomeClick)
        })


        val root = BorderPane().apply {
            top = hBoxSucheGuete
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
    }

    fun popupLogic(ownerStage: Stage) {
        val popupStage = Stage().apply {
            title = "Welchen Ort suchst du genau?"
            initModality(Modality.APPLICATION_MODAL)
            initOwner(ownerStage)
            isResizable = false
        }
        resultsList.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
            if (newValue != null) {
                Gui.selectedLocation = newValue
                Gui.selectedLocationWeather = manager.getCurrentWeather(newValue)
                fillInLocationData(Gui.selectedLocation)
                fillInWeatherData(Gui.selectedLocationWeather)
                // Create and refresh the current weather file in "currentData"
                val storage: Storabledata = WeatherData()
                println("Current: ${storage.storeWeatherData(Gui.selectedLocationWeather)}")

//                // Create and refresh the current weather file in "currentData"
//                val storage: Storabledata = WeatherData()
//                println("Current: ${storage.storeWeatherData(selectedLocationWeather)}")
//
//                // For Test purposes only safe the hourly and daily weather as well
//                println("Daily: ${storage.storeWeatherDataDaily(selectedLocationWeather)}")
//                println("Hourly: ${storage.storeWeatherDataHourly(selectedLocationWeather)}")

                popupStage.close()
            }
        }

        val hintLbl = Label("Solltest du deinen gewünschten Ort nicht finden,\nversuche es mit einem in der Nähe.").apply {
            textAlignment = TextAlignment.CENTER
            isWrapText = true
            padding = Insets(10.0, 0.0, 0.0, 0.0)
            font = AppStyle.FONT_14
            textFill = AppStyle.MAIN_FONT_COLOR
        }
        val resultsBox = VBox().apply {
            alignment = Pos.CENTER
            padding = Insets(10.0)
            children.addAll(resultsList, hintLbl)
        }
        popupStage.scene = Scene(resultsBox, 400.0, 500.0)
        popupStage.showAndWait()
    }

    private fun fillInLocationData(location: Location?) {
        dayView.lblLocation.text = Gui.selectedLocation?.getLocationName()
        if (location != null) {
            dayView.pinPosition(dayView.calculatePosition(location.getLatitude(), location.getLongitude()))
            detailsView.lblDetailsTitle.text = "Details für ${location.getLocationName()}"
        }
    }

    private fun fillInWeatherData(weather: Weather?) {
        if (weather != null) {
            dayView.lblWeatherCode.text = weather.getWeatherCode().description
            dayView.lblTemperature.text = "${weather.getTemperature().toInt()}º"
            dayView.lblMaxTemperature.text = "${weather.getDailyList()[0].getTemperatureMax()}"
            dayView.lblMinTemperature.text = "${weather.getDailyList()[0].getTemperatureMin()}"

            detailsView.lblHumidityValue.text = "${weather.getHumidity()}%"
            detailsView.lblPrecipitationValue.text = "${weather.getPrecipitation()} mm"
            detailsView.lblSunriseValue.text = weather.getDailyList().get(0).getSunrise()
            detailsView.lblSunsetValue.text = weather.getDailyList().get(0).getSunset()
            detailsView.lblWindSpeedValue.text = "${weather.getWindSpeed()} km/h"
            detailsView.updateWindDirection(weather.getWindDirection())
            detailsView.lblApparentTemperatureValue.text = "${round(weather.getApparentTemperature()).toInt()}º"

            dayView.lblUpdateTime.text = "aktualisiert um: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))} Uhr"
        }
        guiFavorites.updateStarColor(Gui.selectedLocation)
        dayView.btnAddFavorite.isVisible = true

        // API-Daten neu laden für Plotter Line Chart und erste 7 Werte auslesen
        weather?.getDailyWeatherDataAll()?.take(7)?.forEach { day ->
            plot("Max Temperatur", day.getTemperatureMax().toInt())
            plot("Min Temperatur", day.getTemperatureMin().toInt())
        }
    }

    fun fillSearchResults(string: String) {
        val search = manager.getLocations(string)
        locationsModel.clear()
        for (result in search) {
            locationsModel.add(result)
        }
    }
    companion object {
        var selectedLocation: Location? = null
        var selectedLocationWeather: Weather? = null
    }


}