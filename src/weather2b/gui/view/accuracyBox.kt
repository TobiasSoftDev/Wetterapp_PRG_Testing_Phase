/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        P.Koch

  Beschreibung: Das Informationsfeld der Guetepruefung wird hier definiert und mit
  aktuellen Daten befuellt.
 */

package weather2b.gui.view

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.scene.text.TextFlow
import javafx.stage.Modality
import javafx.stage.Stage
import weather2b.gui.design.appStyle

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

    fun showInfoPopup(ownerStage: Stage) {
        val popupStage = Stage().apply {
            title = "Prognosequalität"
            initModality(Modality.APPLICATION_MODAL)
            initOwner(ownerStage)
            isResizable = false
        }

        val titleLbl = Label("Was ist die Prognosequalität und wie wird sie berechnet?").apply {
            textAlignment = TextAlignment.LEFT
            isWrapText = true
            padding = Insets(20.0, 0.0, 8.0, 0.0)
            font = appStyle.FONT_14_BOLD
        }

        val intro = Text("Sobald du einen Favoriten hinzugefügt hast, werden dessen Wetterprognosen pro Abruf (einmal pro Tag) gespeichert. Um die Genauigkeit einer Prognose zu messen, wird sie mit dem tatsächlich eingetroffenen Wetter verglichen — frühestens 24 Stunden nach der Vorhersage.\n\nFür jede gespeicherte Prognose werden zwei Werte verglichen:").apply {
            wrappingWidth = 345.0
            lineSpacing = 1.0
            font = appStyle.FONT_12
        }

        val temperatureTitle = Label("Temperatur 🌡️").apply{
            padding = Insets(16.0, 0.0, 0.0, 0.0)
            font = appStyle.FONT_14_BOLD
        }

        val temperatureText = Text("Weicht die vorhergesagte Temperatur um mehr als 10°C vom tatsächlichen Wert ab, beträgt die Genauigkeit 0%. Je kleiner die Abweichung, desto höher die Güte der Temperaturmessung.").apply{
            wrappingWidth = 345.0
            lineSpacing = 1.0
            font = appStyle.FONT_12
        }

        val weatherTitle = Label("Wetterzustand 🌤️").apply {
            padding = Insets(16.0, 0.0, 0.0, 0.0)
            font = appStyle.FONT_14_BOLD
        }

        val weatherText = Text("Der vorhergesagte Wetterzustand (z.B. bewölkt, Regen, etc.) wird als Wettercode gespeichert und mit dem aktuellen verglichen. Die Übereinstimmung fliesst als Prozentwert in die Gesamtbewertung ein.").apply {
            wrappingWidth = 345.0
            lineSpacing = 1.0
            font = appStyle.FONT_12
        }

        val scoreTitle = Label("Gesamtwert (Prognosequalität) 📊").apply {
            padding = Insets(16.0, 0.0, 0.0, 0.0)
            font = appStyle.FONT_14_BOLD
        }

        val scoreText = Text("Der angezeigte Prozentwert ist der Durchschnitt dieser beiden Werte über alle verfügbaren Vergleichsdaten.").apply {
            wrappingWidth = 345.0
            lineSpacing = 1.0
            font = appStyle.FONT_12
        }

        val exampleTitle = Label("Rechenbeispiel 🔎").apply {
            padding = Insets(16.0, 0.0, 0.0, 0.0)
            font = appStyle.FONT_14_BOLD
        }

        val exampleText = Text("Vor 24 Stunden wurden 20°C und 'klarer Himmel' vorhergesagt.\n\nTemperatur: Tatsächlich sind es 22°C.\n"+
                "Die Abweichung beträgt 2°C. Dies ergibt eine Genauigkeit von 80% (1℃ Abweichung ≙ 10% Abzug).\n\n"+
                "Wetterzustand: Tatsächlich ist es 'Leicht bewölkt'.\nDa beide Zustände zur selben Kategorie gehören, ergibt dies 100%.\n\n"+
                "Ergebnis: Der Durchschnitt aus 80% und 100% ergibt eine Prognosegüte von 90% für diesen Messpunkt.").apply {
            wrappingWidth = 345.0
            lineSpacing = 1.0
            font = appStyle.FONT_12
        }

        val contentBox = VBox().apply {
            alignment = Pos.TOP_LEFT
            spacing = 5.0
            minHeight = Region.USE_PREF_SIZE
            maxHeight = Region.USE_PREF_SIZE
            padding = Insets(20.0)
            children.addAll(titleLbl, intro, temperatureTitle, temperatureText, weatherTitle, weatherText, scoreTitle, scoreText, exampleTitle, exampleText)
        }

        popupStage.scene = Scene(contentBox)
        popupStage.showAndWait()

    }

    fun fillAccuracyLabel(score: Double?): String {
        score ?:  return "Die Prognosequalität kann nicht angezeigt werden."
        return when (score) {
            in 99.00..100.0 -> "exzellent"
            in 95.00..99.49  -> "sehr gut"
            in 93.50..94.99 -> "gut"
            in 60.00..93.49 -> "genügend"
            in 20.00..59.99 -> "verbesserungswürdig"
            in 0.00..19.99 -> "Ist etwas schief gelaufen?"
            else -> "Wert ungültig"
        }
    }

    fun getView(): VBox = accuracyVbox
}