import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.effect.InnerShadow
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.text.TextAlignment
import javafx.stage.Modality
import javafx.stage.Stage

/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        P.Koch

  Beschreibung: Suchfeld oben links platzieren und Ein-, Ausgabe darüber triggern
 */

object guiSearchbar {

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

    fun popupLogic(ownerStage: Stage,
                   results: ListView<Location>,

                   onSelectedLocation: (Location) -> Unit) {
        val popupStage = Stage().apply {
            title = "Welchen Ort suchst du genau?"
            initModality(Modality.APPLICATION_MODAL)
            initOwner(ownerStage)
            isResizable = false
        }
        results.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                onSelectedLocation(newValue)
                popupStage.close()
            }
        }

        val hintLbl = Label("Solltest du deinen gewünschten Ort nicht finden,\nversuche es mit einem in der Nähe.").apply {
            textAlignment = TextAlignment.CENTER
            isWrapText = true
            padding = Insets(10.0, 0.0, 0.0, 0.0)
            font = appStyle.FONT_14
            textFill = appStyle.MAIN_FONT_COLOR
        }
        val resultsBox = VBox().apply {
            alignment = Pos.CENTER
            padding = Insets(10.0)
            children.addAll(results, hintLbl)
        }
        popupStage.scene = Scene(resultsBox, 400.0, 500.0)
        popupStage.showAndWait()
    }

    fun getView(): HBox = hBoxSearchbar

}