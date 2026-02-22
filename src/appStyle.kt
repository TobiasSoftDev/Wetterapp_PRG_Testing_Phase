
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

object appStyle {
    val MAIN_FONT_COLOR = Color.web("#232F48")
    val ACCENT_FONT_COLOR = Color.web("#3D90FD")
    val ACCENT_COLOR_LIGHT = Color.web("#D6E8FF")
    val FAVORITE_YELLOW = Color.web("#FCC029")
    val TRANSPARENT = Color.TRANSPARENT
    val GOLDEN_RATIO = 1.618
    val RED = Color.RED
    val BLUE = Color.BLUE
    val FONT_12 = Font("Helvetica", 12.0)
    val FONT_12_BOLD = Font.font("Helvetica", FontWeight.BOLD, 12.0)
    val FONT_14 = Font("Helvetica", 14.0)
    val FONT_14_BOLD = Font.font("Helvetica", FontWeight.BOLD, 14.0)
    val FONT_16 = Font("Helvetica", 16.0)
    val FONT_16_BOLD = Font.font("Helvetica", FontWeight.BOLD, 16.0)
    val FONT_18 = Font.font("Helvetica", 18.0)
    val FONT_24 = Font("Helvetica", 24.0)
    val FONT_24_BOLD = Font.font("Helvetica", FontWeight.BOLD, 24.0)
    val FONT_64 = Font.font("Helvetica", 64.0)
    val FONT_64_BOLD = Font.font("Helvetica", FontWeight.BOLD, 64.0)

    fun layoutLabelLeft(label: Label) {
        with(label) {
            alignment = Pos.CENTER_LEFT
            font = FONT_14
            padding = Insets(5.0, 5.0, 5.0, 5.0)
            minWidth = Region.USE_PREF_SIZE
        }
    }

    fun layoutLabelRight(label: Label) {
        with(label) {
            alignment = Pos.CENTER_RIGHT
            font = FONT_14
            padding = Insets(5.0, 5.0, 5.0, 5.0)
            minWidth = Region.USE_PREF_SIZE
        }
    }

    fun layoutLabelHeaderLeft(label: Label) {
        with(label) {
            alignment = Pos.CENTER_LEFT
            font = FONT_14_BOLD
            padding = Insets(5.0, 5.0, 0.0, 5.0)
            minWidth = Region.USE_PREF_SIZE
        }
    }

    fun layoutLabelHeaderRight(label: Label) {
        with(label) {
            alignment = Pos.CENTER_RIGHT
            font = FONT_14_BOLD
            padding = Insets(5.0, 5.0, 0.0, 5.0)
            minWidth = Region.USE_PREF_SIZE
        }
    }

    fun layoutLabelHeaderTop(label: Label) {
        with(label) {
            alignment = Pos.TOP_LEFT
            font = FONT_18
            padding = Insets(0.0, 0.0, 8.0, 5.0)
            minWidth = Region.USE_PREF_SIZE
        }
    }

    fun layoutLabelBottomRight(label: Label) {
        with(label) {
            alignment = Pos.TOP_LEFT
            font = FONT_14_BOLD
            padding = Insets(0.0, 0.0, 8.0, 5.0)
        }
    }

}