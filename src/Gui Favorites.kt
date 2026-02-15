import javafx.application.Application
import javafx.collections.ListChangeListener
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.Background
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.SVGPath
import javafx.scene.shape.StrokeLineCap
import javafx.scene.shape.StrokeLineJoin
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.stage.Stage
import kotlin.apply
import kotlin.collections.forEach

class GuiFavorites (private val manager:Logic) {
    private val favoriteConstruct = VBox(5.0).apply {
        alignment = Pos.TOP_CENTER
        isFillWidth = true
        maxHeight = 250.0

    }

    val favoriteIcon = SVGPath().apply {
        content =
            "M21.2302 12.0054L17.6428 4.22806C17.3897 3.67919 16.6096 3.67919 16.3564 4.22806L12.7691 12.0054C12.6659 12.2291 12.4539 12.3832 12.2093 12.4122L3.70398 13.4206C3.10374 13.4918 2.86269 14.2337 3.30646 14.644L9.59463 20.4592C9.77549 20.6264 9.85646 20.8756 9.80845 21.1173L8.13926 29.5179C8.02146 30.1107 8.65256 30.5693 9.17999 30.274L16.6536 26.0906C16.8686 25.9703 17.1306 25.9703 17.3456 26.0906L24.8193 30.274C25.3467 30.5693 25.9778 30.1107 25.86 29.5179L24.1908 21.1173C24.1428 20.8756 24.2238 20.6264 24.4046 20.4592L30.6928 14.644C31.1366 14.2337 30.8955 13.4918 30.2953 13.4206L21.79 12.4122C21.5454 12.3832 21.3334 12.2291 21.2302 12.0054Z"
        stroke = AppStyle.FAVORITE_YELLOW
        fill = AppStyle.TRANSPARENT
        strokeWidth = 2.0
        strokeLineCap = StrokeLineCap.ROUND
        strokeLineJoin = StrokeLineJoin.ROUND
    }


    private val lblCount = Label().apply {
        font = Font.font("Outfit", 12.0)
        style = "-fx-text-fill: gray;"
        padding = Insets(10.0, 0.0, 0.0, 0.0)
    }

    fun createFavoriteBox(manager: Logic, onHomeClick: (Location) -> Unit): VBox {

        val favoriteBox = VBox(5.0).apply {
            padding = Insets(10.0)
            prefWidth = 350.0
            alignment = Pos.TOP_CENTER

            val ttlFavorites = Label("Meine Orte ⭐ ").apply {
                font = Font.font("Outfit", FontWeight.BOLD, 28.0)
                padding = Insets(0.0, 0.0, 5.0, 0.0)

            }
            VBox.setVgrow(favoriteConstruct, Priority.NEVER)

            children.addAll(ttlFavorites, favoriteConstruct, lblCount)
        }
        manager.getFavoritesObservableList().addListener(ListChangeListener {
            updateFavoritesList(onHomeClick)
        })
        updateFavoritesList(onHomeClick)

        return favoriteBox

    }

    fun createFavoriteList(favorite: Favorite, onHomeClick: (Location) -> Unit): HBox {
        val boxList = HBox(5.0).apply {
            padding = Insets(0.0, 5.0, 0.0, 5.0)
            alignment = Pos.TOP_RIGHT
            style = "-fx-background-color: transparent; -fx-background-radius: 8;"


            val locationName = Label(favorite.name).apply {
                font = Font.font("Outfit", FontWeight.LIGHT, 16.0)
                minWidth = 120.0
                isWrapText = false
            }

            val lblTemperature = Label("  ${favorite.temperature}°").apply {
                font = Font.font("Outfit", FontWeight.LIGHT, 20.0)
                alignment = Pos.CENTER
                textFill = Color.RED
                minWidth = 45.0

            }

            val lblWeatherIcon = ImageView(favorite.icon).apply {
                fitWidth = 30.0
                isPreserveRatio = true
                isSmooth = true
                isCache = true
            }

            val spacer = Region().apply {
                HBox.setHgrow(this, Priority.ALWAYS)
            }

            val btnSetHome = Button("🏠").apply {
                style =
                    "-fx-background-color: transparent; -fx-text-fill: #ff6b6b; -fx-font-weight: bold; -fx-cursor: hand;"
                isVisible = false
                setOnAction { onHomeClick(favorite.location) }
            }

            val btnRemoveFavorite = Button("❎").apply {
                style =
                    "-fx-background-color: transparent;-fx-text-fill: #ff6b6b; -fx-font-weight: bold; -fx-cursor: hand;"
                isVisible = false
                setOnAction {
                    manager.removeFavorites(favorite.location)
                }
            }

            setOnMouseEntered {
                btnSetHome.isVisible = true
                btnRemoveFavorite.isVisible = true
            }
            setOnMouseExited {
                btnSetHome.isVisible = false
                btnRemoveFavorite.isVisible = false
            }


            children.addAll(locationName, lblTemperature, lblWeatherIcon, spacer,btnSetHome, btnRemoveFavorite)
        }
        return boxList

    }


    fun updateFavoritesList(onHomeClick: (Location) -> Unit) {
        favoriteConstruct.children.clear()
        val currentFavorites = manager.getFavoritesObservableList()

        currentFavorites.forEach { favorite ->
            favoriteConstruct.children.add(
                createFavoriteList(
                    favorite,
                    onHomeClick
                )
            )
        }

        if (currentFavorites.isEmpty()) {
            val lblNoneFavorites = Label(" Keine Favoriten vorhanden").apply {
                style = "-fx-text-fill: gray;"
                padding = Insets(10.0)
            }
            favoriteConstruct.children.add(lblNoneFavorites)
        }

    }


    fun createAddButton(activeLocation: () -> Location?, activeWeather: () -> Weather?): Button {

        val btnAddFavorite = Button().apply {
            graphic = favoriteIcon
            background = Background.EMPTY
            cursor = Cursor.HAND
            isVisible = false

            setOnAction {
                val location = activeLocation()
                val weather = activeWeather()

                if (location != null) {
                    if (manager.checkForFavorites(location)) {
                        manager.removeFavorites(location)
                    } else if (weather != null) {
                        val successCheck = manager.addFavorites(location, weather)
                        if (!successCheck && manager.getFavoritesObservableList().size >= 5) {
                            Alert(Alert.AlertType.WARNING).apply {
                                title = "Limit erreicht"
                                contentText = "Du kannst maximal 5 Favoriten speichern."
                            }.showAndWait()

                        }
                        updateStarColor(location)
                    }
                }
            }
            manager.getFavoritesObservableList().addListener(ListChangeListener {
                updateStarColor(activeLocation())
            })
        }
        return btnAddFavorite
    }

    fun updateStarColor(location: Location?) {
        if (location == null) {
            favoriteIcon.fill = Color.TRANSPARENT
            return
        }
        if (manager.checkForFavorites(location)) {
            favoriteIcon.fill = Color.YELLOW
        } else {
            favoriteIcon.fill = Color.TRANSPARENT
        }
    }
}














