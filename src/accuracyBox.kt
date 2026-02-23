import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment

object accuracyBox {

    val percentLbl = Label("").apply {
        alignment = Pos.CENTER
        font = appStyle.FONT_16
        //background = Background(BackgroundFill(Color.BLUE, null, null))
    }

    val accuracyLbl = Label("Prognosequalität").apply {
        font = appStyle.FONT_18
    }

    private val upperHbox = HBox().apply {
        alignment = Pos.CENTER_RIGHT
        spacing = 10.0
        children.addAll(percentLbl, accuracyLbl)
    }

    private val image = Image("/icons/infoButton.png")

    val infoBtn = Button().apply {
        alignment = Pos.CENTER
        graphic = ImageView(image).apply {
            fitWidth = 20.0
            fitHeight = 20.0
        }
        style = "-fx-background-color: transparent; -fx-padding: 1; -fx-background-radius: 500;"

        setOnMouseEntered {
            style = "-fx-background-color: #D6E8FF; -fx-padding: 1; -fx-background-radius: 500; -fx-opacity: 1.0; -fx-cursor: hand;"
        }
        setOnMouseExited {
            style = "-fx-background-color: transparent; -fx-padding: 1; -fx-opacity: 1.0;"
        }
    }

    val descriptionLbl = Label("").apply {
        font = appStyle.FONT_14
    }

    private val lowerHbox = HBox().apply {
        alignment = Pos.CENTER_RIGHT
        spacing = 10.0
        children.addAll(descriptionLbl, infoBtn)
    }

    private val accuracyVbox = VBox().apply {
        alignment = Pos.CENTER_RIGHT
        spacing = 8.0
        children.addAll(upperHbox, lowerHbox)
    }
    fun getView(): VBox = accuracyVbox
}