import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object dayView {
    lateinit var favorites: guiFavorites

    val btnAddFavorite by lazy {
        favorites.createAddButton(
            activeLocation = { Gui.selectedLocation },
            activeWeather = { Gui.selectedLocationWeather }
        )

    }

    fun addFavoriteButtonToBox() {
        if (!locationHbox.children.contains(btnAddFavorite)) {
            locationHbox.children.add(btnAddFavorite)
        }
    }

    val lblUpdateTime = Label("Für welchen Ort möchtest du das Wetter abfragen?").apply {
        alignment = Pos.CENTER_LEFT
        font = Font.font("Outfit", 16.0)
        textFill = appStyle.ACCENT_FONT_COLOR
    }

    val lblCurrentDay = Label().apply {
        text = LocalDate.now().format(DateTimeFormatter.ofPattern("d. MMMM yyyy"))
        alignment = Pos.CENTER_LEFT
        font = Font.font("Outfit", 20.0)
    }
    val vBoxDayTime = VBox().apply {
        alignment = Pos.CENTER_LEFT
        children.addAll(lblCurrentDay, lblUpdateTime)
    }

    val lblLocation = Label().apply {
        alignment = Pos.CENTER_LEFT
        font = Font.font("Outfit-Regular", FontWeight.SEMI_BOLD, 36.0)
    }

    private val locationHbox = HBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 8.0
        children.addAll(lblLocation)
        maxHeight = 250.0
    }

    val lblWeatherCode = Label().apply {
        alignment = Pos.CENTER_LEFT
        font = appStyle.FONT_16
    }

    val lblTemperature = Label().apply {
        alignment = Pos.CENTER_LEFT
        font = appStyle.FONT_64_BOLD
        padding = Insets(0.0, 20.0, 0.0, 0.0)
    }

    private val lblMaximum = Label("max").apply {
        prefWidth = 30.0
        minWidth = Label.USE_PREF_SIZE
        alignment = Pos.CENTER_RIGHT

    }
    private val lblMinimum = Label("min").apply {
        prefWidth = 30.0
        minWidth = Label.USE_PREF_SIZE
        alignment = Pos.CENTER_RIGHT
    }

    val lblMaxTemperature = Label("-").apply {
        prefWidth = 40.0
        alignment = Pos.CENTER_RIGHT
        textFill = appStyle.RED
        minWidth = Label.USE_PREF_SIZE
        font = appStyle.FONT_16
    }
    private val hBoxTempMax = HBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 5.0
        children.addAll(lblMaximum, lblMaxTemperature)
    }
    val lblMinTemperature = Label("-").apply {
        prefWidth = 40.0
        alignment = Pos.CENTER_RIGHT
        textFill = appStyle.BLUE
        minWidth = Label.USE_PREF_SIZE
        font = appStyle.FONT_16
    }
    private val hBoxTempMin = HBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 5.0
        children.addAll(lblMinimum, lblMinTemperature)
    }
    private val vBoxMaxMin = VBox().apply {
        alignment = Pos.CENTER
        spacing = 6.0
        children.addAll(hBoxTempMax, hBoxTempMin)
    }

    private val hBoxTemperatures = HBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 8.0
        children.addAll(lblTemperature, vBoxMaxMin)
    }

    private val vBoxCurrentLocationTemp = VBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 16.0
        children.addAll(locationHbox, lblWeatherCode, hBoxTemperatures)
    }

    private val vBoxDayView = VBox().apply {
        alignment = Pos.TOP_LEFT
        spacing = 27.0
        maxWidth = Double.MAX_VALUE
        HBox.setHgrow(this, Priority.ALWAYS)
        children.addAll(vBoxDayTime, vBoxCurrentLocationTemp)
    }


    val mapImage = Image("/pictures/Schweiz.png", 1052.09, 668.54, true, false)

    val mapView = ImageView(mapImage).apply {
        fitWidth = 360.0
        isPreserveRatio = true
    }

    val pinImage = Image("/icons/masterPin.png")
    val pinView = ImageView(pinImage).apply {
        fitWidth = 34.0
        isPreserveRatio = true
        isVisible = false
    }

    val imageViewContainer = StackPane(mapView).apply {
        children.addAll(pinView)
    }

    val hBoxDayView = HBox().apply {
        alignment = Pos.CENTER_LEFT
        children.addAll(vBoxDayView, imageViewContainer)
    }

    fun pinPosition(coordinates: Pair<Double, Double>) {
        pinView.translateX = coordinates.first - (mapView.boundsInLocal.width / 2)
        pinView.translateY = coordinates.second - (mapView.boundsInLocal.height / 2) - (pinView.fitWidth / 1.9)
        pinView.isVisible = true
    }

    fun calculatePosition(latitude: Double, longitude: Double): Pair<Double, Double> {
        val latitudeNorth = 47.90           // oberer Bildrand
        val latitudeSouth = 45.70           // unterer Bildrand
        val longitudeWest = 5.80            // linker Bildrand
        val longitudeEast = 10.60           // rechter Bildrand

        val pictureWidth = mapView.boundsInLocal.width
        val pictureHeight = mapView.boundsInLocal.height

        val x = (longitude - longitudeWest) / (longitudeEast - longitudeWest) * pictureWidth
        val y = (latitudeNorth - latitude)  / (latitudeNorth - latitudeSouth) * pictureHeight

        return Pair(x, y)
    }

    fun getView(): HBox {
        return hBoxDayView
    }


}