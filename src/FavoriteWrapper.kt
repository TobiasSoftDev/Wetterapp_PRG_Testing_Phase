import java.io.Serializable

class FavoriteWrapper : Serializable{
    var favorites: MutableList<Favorite> = mutableListOf()
}
