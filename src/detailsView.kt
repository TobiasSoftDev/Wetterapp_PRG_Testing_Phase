import com.sun.org.apache.xalan.internal.lib.ExsltStrings.padding
import javafx.animation.Interpolator
import javafx.animation.ParallelTransition
import javafx.animation.RotateTransition
import javafx.animation.SequentialTransition
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.image.ImageView
import javafx.scene.layout.Background
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.scene.layout.VBox.setMargin
import javafx.scene.transform.Rotate
import javafx.util.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object detailsView {

    val lblPrecipitation = Label("Niederschlag").apply {
        appStyle.layoutLabelHeaderLeft(this)
    }

    val lblPrecipitationValue = Label("-").apply {
        appStyle.layoutLabelLeft(this)
    }

    val vBoxPrecipitation = VBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 5.0
        children.addAll(lblPrecipitation, lblPrecipitationValue)
    }

    val lblHumidity = Label("Luftfeuchtigkeit").apply {
        appStyle.layoutLabelHeaderRight(this)
    }

    val lblHumidityValue = Label("-").apply {
        appStyle.layoutLabelRight(this)
    }

    val vBoxHumidity = VBox().apply {
        alignment = Pos.CENTER_RIGHT
        spacing = 5.0
        children.addAll(lblHumidity, lblHumidityValue)
    }

    val lblSunrise = Label("Sonnenaufgang").apply {
        appStyle.layoutLabelHeaderLeft(this)
    }

    val lblSunriseValue = Label("-").apply {
        appStyle.layoutLabelLeft(this)
    }

    val vBoxSunrise = VBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 5.0
        children.addAll(lblSunrise, lblSunriseValue)
    }

    val lblSunset = Label("Sonnenuntergang").apply {
        appStyle.layoutLabelHeaderRight(this)
    }

    val lblSunsetValue = Label("-").apply {
        appStyle.layoutLabelRight(this)
    }

    val vBoxSunset = VBox().apply {
        alignment = Pos.CENTER_RIGHT
        spacing = 5.0
        children.addAll(lblSunset, lblSunsetValue)
    }

    val lblWindSpeed = Label("Windgeschwindigkeit").apply {
        appStyle.layoutLabelHeaderLeft(this)
    }

    val lblWindSpeedValue = Label("- km/h").apply {
        appStyle.layoutLabelLeft(this)
    }

    val vBoxWindSpeed = VBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 5.0
        children.addAll(lblWindSpeed, lblWindSpeedValue)
    }

    val lblWindDirection = Label("Windrichtung").apply {
        appStyle.layoutLabelHeaderRight(this)
    }

    private val windDirectionArrow = javafx.scene.image.Image("/icons/Arrow_Down_LG.png")

    val windImageView = ImageView().apply {
        image = windDirectionArrow
        fitHeight = 24.0
        fitWidth = 24.0
        isPreserveRatio = true
        isVisible = false
    }

    fun updateWindDirection(degrees: Int) {
        animateWindDirection(degrees, windImageView)
    }

    val vBoxWindDirection = VBox().apply {
        alignment = Pos.CENTER_RIGHT
        spacing = 5.0
        setMargin(windImageView, Insets(0.0, 5.0, 0.0, 0.0))
        children.addAll(lblWindDirection, windImageView)
    }

    val lblApparentTemperature = Label("Gefühlte Temperatur").apply {
        appStyle.layoutLabelHeaderLeft(this)
    }
    val lblApparentTemperatureValue = Label("-").apply {
        appStyle.layoutLabelLeft(this)
    }

    val vBoxApparentTemperature = VBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 5.0
        children.addAll(lblApparentTemperature, lblApparentTemperatureValue)
    }

    val lblDetailsTitle = Label().apply {
        text = "Details für..."
        appStyle.layoutLabelHeaderTop(this)
    }

    val gpDetailsView = GridPane().apply {
        hgap = 20.0
        vgap = 15.0
        padding = Insets(20.0, 0.0, 0.0, 0.0)
        val col1 = ColumnConstraints().apply { percentWidth = 50.0; halignment = HPos.CENTER }
        val col2 = ColumnConstraints().apply { percentWidth = 50.0; halignment = HPos.CENTER }
        columnConstraints.addAll(col1, col2)
        //Haarlinie oben
        style = "-fx-border-color: #D1D1D1; -fx-border-width: 1.0 0 0 0;"

        add(vBoxSunrise, 0, 0)
        add(vBoxSunset, 1, 0)
        add(vBoxWindSpeed, 0, 1)
        add(vBoxWindDirection, 1, 1)
        add(vBoxPrecipitation, 0, 2)
        add(vBoxHumidity, 1, 2)
        add(vBoxApparentTemperature, 0, 3)
    }

    val gridPaneBox = VBox().apply {
        alignment = Pos.CENTER_LEFT
        spacing = 5.0
        children.addAll(lblDetailsTitle, gpDetailsView)
    }

    val scrollPane = ScrollPane().apply {
        isFitToWidth = true
        content = gridPaneBox
        // Farbe des Viewports (fx-background), Farbe des SP-Containers (fx-background-color)
        style = "-fx-background: white; -fx-background-color: transparent; -fx-padding: 0 50 10 0;"

    }

    fun getView(): ScrollPane {
        return scrollPane
    }

    private fun animateWindDirection(degrees: Int, image: ImageView) {
        image.isVisible = true
        val TIME = 600.0
        val rotate = RotateTransition(Duration(TIME), image).apply {
            toAngle = degrees.toDouble()
            interpolator = Interpolator.EASE_BOTH
        }
        rotate.play()
    }
}