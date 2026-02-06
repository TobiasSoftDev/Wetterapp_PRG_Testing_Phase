import AppStyle.GOLDEN_RATIO
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.effect.InnerShadow
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.SVGPath
import javafx.scene.shape.StrokeLineCap
import javafx.scene.shape.StrokeLineJoin
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import javafx.stage.Modality
import javafx.stage.Stage
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.lang.model.element.ModuleElement

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

    private var isFavorite: Boolean = false
    private val favoriteIcon = SVGPath().apply {
        content = "M21.2302 12.0054L17.6428 4.22806C17.3897 3.67919 16.6096 3.67919 16.3564 4.22806L12.7691 12.0054C12.6659 12.2291 12.4539 12.3832 12.2093 12.4122L3.70398 13.4206C3.10374 13.4918 2.86269 14.2337 3.30646 14.644L9.59463 20.4592C9.77549 20.6264 9.85646 20.8756 9.80845 21.1173L8.13926 29.5179C8.02146 30.1107 8.65256 30.5693 9.17999 30.274L16.6536 26.0906C16.8686 25.9703 17.1306 25.9703 17.3456 26.0906L24.8193 30.274C25.3467 30.5693 25.9778 30.1107 25.86 29.5179L24.1908 21.1173C24.1428 20.8756 24.2238 20.6264 24.4046 20.4592L30.6928 14.644C31.1366 14.2337 30.8955 13.4918 30.2953 13.4206L21.79 12.4122C21.5454 12.3832 21.3334 12.2291 21.2302 12.0054Z"
        stroke = AppStyle.FAVORITE_YELLOW
        fill = AppStyle.TRANSPARENT
        strokeWidth = 2.0
        strokeLineCap = StrokeLineCap.ROUND
        strokeLineJoin = StrokeLineJoin.ROUND
    }
//    private val chevronUp = SVGPath().apply {
//        content = "M9.375 13.5415L12.5 10.4165L15.625 13.5415"
//        stroke = Color.web("#232F48")
//        strokeWidth = 2.0
//        strokeLineCap = StrokeLineCap.ROUND
//        strokeLineJoin = StrokeLineJoin.ROUND
//    }
//    private val chevronDown = SVGPath().apply {
//        content = "M15.625 11.4585L12.5 14.5835L9.375 11.4585"
//        stroke = Color.web("#232F48")
//        strokeWidth = 2.0
//        strokeLineCap = StrokeLineCap.ROUND
//        strokeLineJoin = StrokeLineJoin.ROUND
//    }
//    private val scaledChevronUp = Group(chevronUp).apply {
//        scaleX = 1.5
//        scaleY = 1.5
//    }
//
//    private val scaledChevronDown = Group(chevronDown).apply {
//        scaleX = 1.5
//        scaleY = 1.5
//    }
    private val chevronUp = Image("/icons/chevronUp.png")
    private val imageViewChevronUp = ImageView(chevronUp).apply {
        fitWidth = 25.0
        isPreserveRatio = true
    }

    private val chevronDown = Image("/icons/chevronDown.png")
    private val imageViewChevronDown = ImageView(chevronDown).apply {
        fitWidth = 25.0
        isPreserveRatio = true
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
        onAction = EventHandler {
                event -> fillSearchResults(this.text)
            val source = (event.source as Node).scene.window as Stage
            popupLogic(source)
        }

    }

    private val btnSuche = Button("Suchen").apply {
        alignment = Pos.CENTER_LEFT
        padding = Insets(6.0, 18.0, 6.0, 18.0)
        cursor = Cursor.HAND
        val shadow = InnerShadow().apply {
            color = AppStyle.ACCENT_COLOR_LIGHT
        }
        setOnMouseEntered {
            effect = shadow
        }
        setOnMouseExited {
            effect = null
        }
        setOnAction { event -> fillSearchResults(tflSucheingabe.text)
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

    private val hBoxSuche = HBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 8.0
        tflSucheingabe.prefWidthProperty().bind(this.widthProperty().divide(1 + GOLDEN_RATIO).multiply(GOLDEN_RATIO))
        children.addAll(tflSucheingabe)
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
        hBoxSuche.prefWidthProperty().bind(this.widthProperty().multiply(0.5))
        hBoxGuete.prefWidthProperty().bind(this.widthProperty().multiply(0.5))
        children.addAll(hBoxSuche, hBoxGuete)
    }

    private val lblUpdateTime = Label("Für welchen Ort möchtest du das Wetter abfragen?").apply {
        alignment = Pos.CENTER_LEFT
        font = Font.font("Outfit", 16.0)
        textFill = AppStyle.ACCENT_FONT_COLOR
    }

    private val lblCurrentDay = Label().apply {
        text = LocalDate.now().format(DateTimeFormatter.ofPattern("d. MMMM yyyy"))
        alignment = Pos.CENTER_LEFT
        font = Font.font("Outfit", 20.0)
    }
    private val vBoxDayTime = VBox().apply {
        alignment = Pos.CENTER_LEFT
        children.addAll(lblCurrentDay, lblUpdateTime)
    }

    private val lblLocation = Label().apply {
        alignment = Pos.CENTER_LEFT
        font = Font.font("Outfit-Regular", FontWeight.SEMI_BOLD, 36.0)
    }

    fun setFavoriteIcon() {
        if (isFavorite) {
            favoriteIcon.fill = AppStyle.FAVORITE_YELLOW
        } else {
            favoriteIcon.fill = AppStyle.TRANSPARENT
        }
    }

    private val btnSetFavorite = Button().apply {
        graphic = favoriteIcon
        background = Background.EMPTY
        cursor = Cursor.HAND
        isVisible = false
        setOnAction { event -> println("Favoriten-Action: addFavorite()..")
            isFavorite = !isFavorite
            setFavoriteIcon()
        }
    }

    private val locationHbox = HBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 8.0
        children.addAll(lblLocation, btnSetFavorite)
    }
    private val lblWeatherCode = Label().apply {
        alignment = Pos.CENTER_LEFT
        font = Font.font("Outfit", FontWeight.LIGHT, 16.0)
    }

    private val lblTemperature = Label().apply {
        alignment = Pos.CENTER_LEFT
        //border = Border(BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        font = Font.font("Helvetica", FontWeight.BOLD, 64.0)
    }

    private val lblTempMax = Label().apply {
        alignment = Pos.CENTER_LEFT
        textFill = AppStyle.RED
        font = Font.font("Outfit", FontWeight.LIGHT, 16.0)
    }
    private val hBoxTempMax = HBox().apply {
        alignment = Pos.CENTER_LEFT
        children.addAll(imageViewChevronUp, lblTempMax)
    }
    private val lblTempMin = Label().apply {
        alignment = Pos.CENTER
        textFill = AppStyle.BLUE
        font = Font.font("Outfit", FontWeight.LIGHT, 16.0)
    }
    private val hBoxTempMin = HBox().apply {
        alignment = Pos.CENTER_LEFT
        children.addAll(imageViewChevronDown, lblTempMin)
    }
    private val vBoxMaxMin = VBox().apply {
        alignment = Pos.CENTER
        spacing = 6.0
        children.addAll(hBoxTempMax, hBoxTempMin)
    }

    private val hBoxTemperatures = HBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 8.0
        //border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        children.addAll(lblTemperature, vBoxMaxMin)
    }

    private val vBoxCurrentLocationTemp = VBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 16.0
        //border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        children.addAll(locationHbox, lblWeatherCode, hBoxTemperatures)
    }

    private val vBoxDayView = VBox().apply {
        alignment = Pos.TOP_LEFT
        spacing = 27.0
        //border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        maxWidth = Double.MAX_VALUE
        HBox.setHgrow(this, Priority.ALWAYS)
        children.addAll(vBoxDayTime, vBoxCurrentLocationTemp)
    }

    private val mapImage = Image("/pictures/Schweiz.png", 1052.09, 668.54, true, false)

    private val mapView = ImageView(mapImage).apply {
        fitWidth = 300.0
        isPreserveRatio = true
    }

    val pinImage = Image("/icons/masterPin.png")
    val pinView = ImageView(pinImage).apply {
        fitWidth = 34.0
        isPreserveRatio = true
        isVisible = false
    }

    private val imageViewContainer = StackPane(mapView).apply {
        //padding = Insets(20.0)
        //border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        children.addAll(pinView)
    }

    private val hBoxDayView = HBox().apply {
        alignment = Pos.CENTER_LEFT
        children.addAll(vBoxDayView, imageViewContainer)
    }

    private val vBoxFavorites = VBox().apply {
        alignment = Pos.TOP_RIGHT
        border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
    }

    private val hBoxDayViewFavorites = HBox().apply {
        alignment = Pos.TOP_LEFT
        padding = Insets(30.0)
        vBoxFavorites.prefWidthProperty().bind(this.widthProperty().multiply(0.5))
        hBoxDayView.prefWidthProperty().bind(this.widthProperty().multiply(0.50))
        children.addAll(hBoxDayView, vBoxFavorites)
    }

    override fun start(stage: Stage) {
        val root = BorderPane().apply {
            top = hBoxSucheGuete
            bottom = hBoxBottom
            center = hBoxDayViewFavorites
            background = Background(BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets(0.0, 0.0, 0.0, 0.0)))
            isFocusTraversable = true   // Nimmt den Cursor aus dem Textfeld. Textfeld will Aufmerksamkeit haben...
        }
        hBoxDayView.prefWidthProperty().bind(root.widthProperty().multiply(0.50))
        hBoxDayView.minWidth = 200.0
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
        lblLocation.text = selectedLocation?.getLocationName()
        if (location != null) {
            pinPosition(calculatePosition(location.getLatitude(), location.getLongitude()))
        }
    }

    private fun fillInWeatherData(weather: Weather?) {
        if (weather != null) {
            lblWeatherCode.text = weather.getWeatherCode().description
            lblTemperature.text = "${weather.getTemperature().toInt()}º"
            lblTempMax.text = "${weather.getDailyList()[0].getTemperatureMax()}"
            lblTempMin.text = "${weather.getDailyList()[0].getTemperatureMin()}"
            lblHumidity.text = "Luftfeuchtigkeit: ${weather.getHumidity()}%"
            lblPrecipitation.text = "Niederschlag: ${weather.getPrecipitation()}%"
            lblSunrise.text = "Sonnenaufgang: ${
                weather.getDailyList().get(0).getSunrise()}"
            lblSunset.text = "Sonnenuntergang: ${
                weather.getDailyList().get(0).getSunset()}"
            lblUpdateTime.text = "aktualisiert um: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))} Uhr"
        }
        btnSetFavorite.isVisible = true
    }

    fun fillSearchResults(string: String) {
        val search = manager.getLocations(string)
        locationsModel.clear()
        for (result in search) {
            locationsModel.add(result)
        }
    }

    fun pinPosition(coordinates: Pair<Double, Double>) {
        pinView.translateX = coordinates.first - (mapView.boundsInLocal.width / 2)
        pinView.translateY = coordinates.second - (mapView.boundsInLocal.height / 2) - (pinView.fitWidth / 1.9)
        pinView.isVisible = true
    }
    fun calculatePosition(latitude: Double, longitude: Double): Pair<Double, Double> {
        val latitudeNorth = 47.90       // oberer Bildrand
        val latitudeSouth = 45.70       // unterer Bildrand
        val longitudeWest = 5.80            // linker Bildrand
        val longitudeEast = 10.60          // rechter Bildrand

        val pictureWidth = mapView.boundsInLocal.width
        val pictureHeight = mapView.boundsInLocal.height

        val x = (longitude - longitudeWest) / (longitudeEast - longitudeWest) * pictureWidth
        val y = (latitudeNorth - latitude)  / (latitudeNorth - latitudeSouth) * pictureHeight

        return Pair(x, y)
    }
}







//fun exit() {
//    with(Alert(Alert.AlertType.INFORMATION)) {
//        contentText = "Wenn du die App schliessen willst, drücke auf «OK»."
//        showAndWait()
//    }
//}