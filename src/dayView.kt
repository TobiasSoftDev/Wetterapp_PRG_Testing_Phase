import javafx.scene.image.Image
import javafx.scene.image.ImageView

object dayView {


    private val chevronUp = Image("/icons/chevronUp.png")
    private val imageViewChevronUp = ImageView(chevronUp).apply {
        fitWidth = 25.0
        isPreserveRatio = true
    }

    private val chevronDown = Image("/icons/chevronDown.png")
    private val imageViewChevronDown = ImageView(chevronDown).apply {
        fitWidth = 25.0
        isPreserveRatio = true
    }


}
