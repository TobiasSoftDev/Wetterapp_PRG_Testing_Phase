import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.effect.InnerShadow
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Background
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.shape.SVGPath
import javafx.scene.shape.StrokeLineCap
import javafx.scene.shape.StrokeLineJoin
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object dayView {

    //private val favList = FXCollections.observableArrayList<Favorite>()
    private val lblMaximum = Label("max").apply {
        padding = Insets(5.0, 10.0, 5.0, 10.0)
        alignment = Pos.BOTTOM_CENTER
    }
    private val lblMinimum = Label("min").apply {
        padding = Insets(5.0, 10.0, 5.0, 10.0)
        alignment = Pos.BOTTOM_CENTER
    }


    val lblUpdateTime = Label("Für welchen Ort möchtest du das Wetter abfragen?").apply {
        alignment = Pos.CENTER_LEFT
        font = Font.font("Outfit", 16.0)
        textFill = AppStyle.ACCENT_FONT_COLOR
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

    private val favoriteIcon = SVGPath().apply {
        content = "M21.2302 12.0054L17.6428 4.22806C17.3897 3.67919 16.6096 3.67919 16.3564 4.22806L12.7691 12.0054C12.6659 12.2291 12.4539 12.3832 12.2093 12.4122L3.70398 13.4206C3.10374 13.4918 2.86269 14.2337 3.30646 14.644L9.59463 20.4592C9.77549 20.6264 9.85646 20.8756 9.80845 21.1173L8.13926 29.5179C8.02146 30.1107 8.65256 30.5693 9.17999 30.274L16.6536 26.0906C16.8686 25.9703 17.1306 25.9703 17.3456 26.0906L24.8193 30.274C25.3467 30.5693 25.9778 30.1107 25.86 29.5179L24.1908 21.1173C24.1428 20.8756 24.2238 20.6264 24.4046 20.4592L30.6928 14.644C31.1366 14.2337 30.8955 13.4918 30.2953 13.4206L21.79 12.4122C21.5454 12.3832 21.3334 12.2291 21.2302 12.0054Z"
        stroke = AppStyle.FAVORITE_YELLOW
        fill = AppStyle.TRANSPARENT
        strokeWidth = 2.0
        strokeLineCap = StrokeLineCap.ROUND
        strokeLineJoin = StrokeLineJoin.ROUND
    }

//    private fun setFavoriteIcon() {
//        if (favList.isNotEmpty()) {
//            favoriteIcon.fill = AppStyle.FAVORITE_YELLOW
//        } else {
//            favoriteIcon.fill = AppStyle.TRANSPARENT
//        }
//    }

    val btnSetFavorite = Button().apply {
        graphic = favoriteIcon
        background = Background.EMPTY
        cursor = Cursor.HAND
        isVisible = false
        setOnAction { event -> println("Favoriten-Action: addFavorite()..")
            //isFavorite = !isFavorite
            //setFavoriteIcon()
        }
    }

    private val locationHbox = HBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 8.0
        children.addAll(lblLocation, btnSetFavorite)
    }
    val lblWeatherCode = Label().apply {
        alignment = Pos.CENTER_LEFT
        font = Font.font("Outfit", FontWeight.LIGHT, 16.0)
    }

    val lblTemperature = Label().apply {
        alignment = Pos.CENTER_LEFT
        //border = Border(BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
        font = Font.font("Helvetica", FontWeight.BOLD, 64.0)
    }

    val lblMaxTemperature = Label().apply {
        alignment = Pos.CENTER_RIGHT
        textFill = AppStyle.RED
        prefWidth = 30.0
        font = Font.font("Outfit", FontWeight.LIGHT, 16.0)
    }
    private val hBoxTempMax = HBox().apply {
        alignment = Pos.CENTER_LEFT
        children.addAll(lblMaximum, lblMaxTemperature)
    }
    val lblMinTemperature = Label().apply {
        alignment = Pos.CENTER_RIGHT
        textFill = AppStyle.BLUE
        prefWidth = 30.0
        font = Font.font("Outfit", FontWeight.LIGHT, 16.0)
    }
    private val hBoxTempMin = HBox().apply {
        alignment = Pos.CENTER_LEFT
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
        //padding = Insets(20.0)
        //border = Border(BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0)))
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