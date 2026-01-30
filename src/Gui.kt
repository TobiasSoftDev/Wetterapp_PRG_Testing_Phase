import javafx.application.Application
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.TextField
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
import javafx.scene.text.FontWeight
import javafx.stage.Modality
import javafx.stage.Stage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
            override fun updateItem(item: Location?, empty: Boolean) {
                super.updateItem(item, empty)
                if (empty || item == null) {
                    text = null
                } else {
                    text = item.getLocationForSearchResult()
                }
            }
        }
        }
    }

    private var selectedLocation: Location? = null
    private var selectedLocationWeather: Weather? = null



    private val lblProzent = Label("98%").apply {
        alignment = Pos.CENTER
        font = Font.font("Outfit", 16.0)
        //background = Background(BackgroundFill(Color.BLUE, null, null))
    }

    private val tflSucheingabe = TextField().apply {
        isEditable = true
        promptText = "Ort oder PLZ eingeben..."
    }

    private val btnSuche = Button("Suchen").apply {
        alignment = Pos.CENTER_LEFT
        padding = Insets(6.0, 18.0, 6.0, 18.0)
        setOnAction { event -> fillResults(tflSucheingabe.text)
            val source = (event.source as Node).scene.window as Stage
            popupLogic(source)
        }
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
        alignment = Pos.CENTER_LEFT
        padding = Insets(8.0, 0.0, 8.0, 0.0)
        border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        children.addAll(lblHumidity, lblPrecipitation, lblSunrise, lblSunset)
    }

    private val graphView = HBox().apply {
        alignment = Pos.CENTER
        padding = Insets(8.0, 0.0, 8.0, 0.0)
        border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
    }

    private val hBoxBottom = HBox().apply {
        alignment = Pos.CENTER
        padding = Insets(20.0)
        border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        HBox.setHgrow(fpDetailsDayView, Priority.ALWAYS)
        HBox.setHgrow(graphView, Priority.ALWAYS)
        children.addAll(fpDetailsDayView, graphView)
    }

    private val hBoxSuche = HBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 8.0
        padding = Insets(0.0, 0.0, 2.0, 0.0)
        children.addAll(tflSucheingabe, btnSuche)
    }

    private val lblGuete = Label("Güte der Vorhersage").apply {
        font = Font.font("Outfit", 18.0)
    }

    private val hBoxGuete = HBox().apply {
        alignment = Pos.CENTER_RIGHT
        spacing = 8.0
        padding = Insets(20.0)
        //background = Background(BackgroundFill(Color.BLUE, null, null))
        children.addAll(lblProzent, lblGuete)
    }

    private val hBoxSucheGuete = HBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 8.0
        padding = Insets(20.0)
        HBox.setHgrow(hBoxGuete, Priority.ALWAYS)
        HBox.setHgrow(hBoxSuche, Priority.ALWAYS)
        children.addAll(hBoxSuche, hBoxGuete)
    }

    private val lblUpdateTime = Label("aktualisiert um...").apply {
        alignment = Pos.CENTER_LEFT
        font = Font.font("Outfit", 16.0)
        textFill = Color.web("#3D90FD")
    }

    private val vBoxDayTime = VBox().apply {
        val lblCurrentDay = Label(LocalDateTime.now().dayOfWeek.toString()).apply {
            alignment = Pos.CENTER_LEFT
            font = Font.font("Outfit", 24.0)
        }

        alignment = Pos.CENTER_LEFT
        children.addAll(lblCurrentDay, lblUpdateTime)
    }

    private val lblLocation = Label().apply {
        alignment = Pos.CENTER_LEFT
        font = Font.font("Outfit", 20.0)
    }

    private val lblWeatherCode = Label().apply {
        alignment = Pos.CENTER_LEFT
        font = Font.font("Outfit", FontWeight.LIGHT, 16.0)
    }

    private val lblTemperature = Label().apply {
        alignment = Pos.CENTER_LEFT
        border = Border(BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        font = Font.font("Outfit", FontWeight.BOLD, 28.0)
    }
    private val lblTempMaxMin = Label().apply {
        alignment = Pos.CENTER_LEFT
        border = Border(BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        font = Font.font("Outfit", FontWeight.LIGHT, 14.0)
    }

    private val hBoxTemperatures = HBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 8.0
        border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        children.addAll(lblTemperature, lblTempMaxMin)
    }

    private val vBoxCurrentLocationTemp = VBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 16.0
        border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        children.addAll(lblLocation, lblWeatherCode, hBoxTemperatures)
    }

    private val vBoxDayView = VBox().apply {
        alignment = Pos.TOP_LEFT
        spacing = 27.0
        padding = Insets(20.0)
        border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        HBox.setHgrow(vBoxDayTime, Priority.ALWAYS)
        HBox.setHgrow(vBoxCurrentLocationTemp, Priority.ALWAYS)
        children.addAll(vBoxDayTime, vBoxCurrentLocationTemp)
    }
    private val vBoxFavorites = VBox().apply {
        alignment = Pos.TOP_RIGHT
        spacing = 0.0
        padding = Insets(20.0)
        border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
    }

    private val hBoxDayViewFavorites = HBox().apply {
        alignment = Pos.CENTER
        spacing = 15.0
        padding = Insets(20.0)
        HBox.setHgrow(vBoxDayView, Priority.ALWAYS)
        HBox.setHgrow(vBoxFavorites, Priority.ALWAYS)
        children.addAll(vBoxDayView, vBoxFavorites)
    }

    override fun start(stage: Stage) {
        val root = BorderPane().apply {
            top = hBoxSucheGuete
            bottom = hBoxBottom
            center = hBoxDayViewFavorites

            isFocusTraversable = true   // Nimmt den Cursor aus dem Textfeld. Textfeld will Aufmerksamkeit haben...
        }

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

                lblLocation.text = selectedLocation?.getLocationName()
                lblWeatherCode.text = selectedLocationWeather?.getWeatherCode().toString()
                lblTemperature.text = "${selectedLocationWeather?.getTemperature()} ℃"
                lblTempMaxMin.text = "${selectedLocationWeather?.getDailyList()?.get(0)?.getTemperatureMax()} / ${
                    selectedLocationWeather?.getDailyList()?.get(0)?.getTemperatureMin()}"
                lblHumidity.text = "Luftfeuchtigkeit: ${selectedLocationWeather?.getHumidity().toString()}%"
                lblPrecipitation.text = "Niederschlag: ${selectedLocationWeather?.getPrecipitation().toString()}%"
                lblSunrise.text = "Sonnenaufgang: ${
                    selectedLocationWeather?.getDailyList()?.get(0)?.getSunrise()}"
                lblSunset.text = "Sonnenuntergang: ${
                    selectedLocationWeather?.getDailyList()?.get(0)?.getSunset()}"
                // println(selectedLocationWeather)
                lblUpdateTime.text = "aktualisiert am: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))}"
                popupStage.close()
            }
        }

        val resultsBox = VBox(resultsList).apply {
            alignment = Pos.CENTER
            padding = Insets(10.0)
        }
        popupStage.scene = Scene(resultsBox, 400.0, 500.0)
        popupStage.showAndWait()
    }


    fun fillResults(string: String) {
        val search = manager.getLocations(string)
        locationsModel.clear()
        for (result in search) {
            locationsModel.add(result)
        }
    }
}



//fun exit() {
//    with(Alert(Alert.AlertType.INFORMATION)) {
//        contentText = "Wenn du die App schliessen willst, drücke auf «OK»."
//        showAndWait()
//    }
//}