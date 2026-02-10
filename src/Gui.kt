import AppStyle
import dayView
import plotterLineChart
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
import javafx.scene.control.TextField
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Border
import javafx.scene.layout.BorderPane
import javafx.scene.layout.BorderStroke
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.layout.BorderWidths
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.FlowPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import javafx.stage.Modality
import javafx.stage.Stage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.get
import kotlin.text.toInt

//fun exit() {
//    with(Alert(Alert.AlertType.INFORMATION)) {
//        contentText = "Wenn du die App schliessen willst, drücke auf «OK»."
//        showAndWait()
//    }
//}
class Gui : Application() {
    private val manager: Logic = Manager()
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

    private val lblProzent = Label("98%").apply {
        alignment = Pos.CENTER
        font = Font.font("Outfit", 16.0)
        //background = Background(BackgroundFill(Color.BLUE, null, null))
    }


    private val lblPrecipitation = Label("Niederschlag: ").apply {
        alignment = Pos.CENTER
        font = Font.font("Outfit", 16.0)
        padding = Insets(8.0, 8.0, 8.0, 8.0)
    }

    private val lblHumidity = Label("Luftfeuchtigkeit: ").apply {
        alignment = Pos.CENTER
        font = Font.font("Outfit", 16.0)
        padding = Insets(8.0, 8.0, 8.0, 8.0)
    }

    private val lblSunrise = Label("Sonnenaufgang: ").apply {
        alignment = Pos.CENTER
        font = Font.font("Outfit", 16.0)
        padding = Insets(8.0, 8.0, 8.0, 8.0)
    }

    private val lblSunset = Label("Sonnenuntergang: ").apply {
        alignment = Pos.CENTER
        font = Font.font("Outfit", 16.0)
        padding = Insets(8.0, 8.0, 8.0, 8.0)
    }

    private val fpDetailsDayView = FlowPane().apply {
        alignment = Pos.TOP_LEFT
        padding = Insets(8.0, 0.0, 8.0, 0.0)
        border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        children.addAll(lblHumidity, lblPrecipitation, lblSunrise, lblSunset)
    }

    private val graphView = HBox().apply {
        alignment = Pos.CENTER
        border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
    }

    private val hBoxBottom = HBox().apply {
        alignment = Pos.CENTER
        padding = Insets(30.0)
        border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        fpDetailsDayView.prefWidthProperty().bind(this.widthProperty().multiply(0.33))
        plotterLineChart.getView().prefWidthProperty().bind(this.widthProperty().multiply(0.66))
        children.addAll(fpDetailsDayView, plotterLineChart.getView())
    }

    private val lblGuete = Label("Güte der Vorhersage").apply {
        font = Font.font("Outfit", 18.0)
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

    private val hBoxDayViewFavorites = HBox().apply {
        alignment = Pos.TOP_LEFT
        padding = Insets(30.0)
        vBoxFavorites.prefWidthProperty().bind(this.widthProperty().multiply(0.5))
        dayView.getView().prefWidthProperty().bind(this.widthProperty().multiply(0.50))
        children.addAll(dayView.getView(), vBoxFavorites)
    }

    override fun start(stage: Stage) {
        val root = BorderPane().apply {
            top = hBoxSucheGuete
            bottom = hBoxBottom
            center = hBoxDayViewFavorites
            background = Background(BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets(0.0, 0.0, 0.0, 0.0)))
            isFocusTraversable = true   // Nimmt den Cursor aus dem Textfeld. Textfeld will Aufmerksamkeit haben...
        }
        dayView.getView().prefWidthProperty().bind(root.widthProperty().multiply(0.50))
        dayView.getView().minWidth = 200.0
        hBoxBottom.prefHeightProperty().bind(root.heightProperty().multiply(0.40))
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
                selectedLocation = newValue
                selectedLocationWeather = manager.getCurrentWeather(newValue)
                fillInLocationData(selectedLocation)
                fillInWeatherData(selectedLocationWeather)
                popupStage.close()
            }
        }

        val hintLbl = Label("Solltest du deinen gewünschten Ort nicht finden,\nversuche es mit einem in der Nähe.").apply {
            textAlignment = TextAlignment.CENTER
            isWrapText = true
            padding = Insets(10.0, 0.0, 0.0, 0.0)
            font = Font.font("Outfit", FontWeight.LIGHT, FontPosture.ITALIC, 14.0)
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
        dayView.lblLocation.text = selectedLocation?.getLocationName()
        if (location != null) {
            dayView.pinPosition(dayView.calculatePosition(location.getLatitude(), location.getLongitude()))
        }
    }

    private fun fillInWeatherData(weather: Weather?) {
        if (weather != null) {
            dayView.lblWeatherCode.text = weather.getWeatherCode().description
            dayView.lblTemperature.text = "${weather.getTemperature().toInt()}º"
            dayView.lblMaxTemperature.text = "${weather.getDailyList()[0].getTemperatureMax()}"
            dayView.lblMinTemperature.text = "${weather.getDailyList()[0].getTemperatureMin()}"
            lblHumidity.text = "Luftfeuchtigkeit: ${weather.getHumidity()}%"
            lblPrecipitation.text = "Niederschlag: ${weather.getPrecipitation()}%"
            lblSunrise.text = "Sonnenaufgang: ${
                weather.getDailyList().get(0).getSunrise()}"
            lblSunset.text = "Sonnenuntergang: ${
                weather.getDailyList().get(0).getSunset()}"
            dayView.lblUpdateTime.text = "aktualisiert um: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))} Uhr"
        }
        dayView.btnSetFavorite.isVisible = true
    }

    fun fillSearchResults(string: String) {
        val search = manager.getLocations(string)
        locationsModel.clear()
        for (result in search) {
            locationsModel.add(result)
        }
    }

}