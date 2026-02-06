import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.stage.Stage

class GuiFavorites (manager:Logic) {

    fun createFavoriteBox(manager: Logic): VBox {


             val headerBox = HBox(10.0).apply {
                alignment = Pos.CENTER_LEFT
            }
            val title = Label("Meine Orte ⭐ ").apply {
                font = Font.font("Outfit", FontWeight.BOLD, 25.0)
            }

            headerBox.children.addAll(title)

            val favoriteConstruct = VBox()

            val allFavorites = manager.getFavoritList()


            allFavorites.forEach { favorite ->
                val line = createFavoriteList(favorite)
                favoriteConstruct.children.add(line)
            }

         /*   if (allFavorites.isEmpty()) {
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Favoritenliste"
                alert.contentText = "Es wurde noch kein Favorit hinzugefügt"
                alert.showAndWait()

            }*/

        val favoriteBox  = VBox().apply {
            alignment = Pos.CENTER_LEFT
            spacing = 30.0
            padding = Insets(20.0)
            HBox.setHgrow(headerBox, Priority.ALWAYS)
            HBox.setHgrow(favoriteConstruct, Priority.ALWAYS)
        }



        favoriteBox.children.addAll(headerBox,favoriteConstruct)





        return favoriteBox

        }

        fun createFavoriteList(favorite: Favorite): HBox {

            val boxList = HBox().apply {
                padding = Insets(0.0,10.0,10.0,0.0)
                alignment = Pos.CENTER_LEFT
                style = "-fx-background-color: #E3F2FD; -fx-background-radius: 8;"

            }
            val locationName = Label(favorite.name).apply {
                font = Font.font("Outfit", FontWeight.LIGHT, 15.0)
                prefWidth = 120.0
            }

            val lblTemperature = Label("  ${favorite.temperature}").apply {
                font = Font.font("Outfit", FontWeight.LIGHT, 20.0)
                alignment = Pos.CENTER_LEFT
                textFill = Color.RED
                padding = Insets(0.0,5.0,0.0,5.0)

            }

            val lblWeatherIcon = Label(favorite.icon).apply {
                font = Font.font(30.0)
                alignment = Pos.CENTER
                padding = Insets(0.0,5.0,0.0,5.0)
            }

            val btnFavoritEntfernen = Button("❎").apply {
                padding = Insets(2.0, 10.0, 2.0, 10.0)
                style = "-fx-background-color: gray; -fx-text-fill: white; -fx-background-radius: 5;"
            }


            boxList.children.addAll(locationName,lblTemperature,lblWeatherIcon,btnFavoritEntfernen)

            return boxList

        }

    }










