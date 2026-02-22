import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.effect.InnerShadow
import javafx.scene.layout.Border
import javafx.scene.layout.BorderStroke
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.layout.BorderWidths
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.paint.Color

object searchbar {


    val tflSucheingabe = TextField().apply {
        isEditable = true
        promptText = "Ort oder PLZ eingeben..."
        maxWidth = Double.MAX_VALUE
    }

    val btnSuche = Button("Suchen").apply {
        alignment = Pos.CENTER_LEFT
        padding = Insets(6.0, 18.0, 6.0, 18.0)
        cursor = Cursor.HAND
        val shadow = InnerShadow().apply {
            color = appStyle.ACCENT_COLOR_LIGHT
        }
        setOnMouseEntered {
            effect = shadow
        }
        setOnMouseExited {
            effect = null
        }
    }
    val hBoxSearchbar = HBox().apply {
        alignment = Pos.CENTER_LEFT
        padding = Insets(0.0, 0.0, 0.0, 0.0)
        spacing = 8.0
        HBox.setHgrow(tflSucheingabe, Priority.ALWAYS)
        HBox.setHgrow(btnSuche, Priority.ALWAYS)
        children.addAll(tflSucheingabe, btnSuche)
    }

    fun getView(): HBox = hBoxSearchbar

}