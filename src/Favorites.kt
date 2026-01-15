object Favorites {
    private val favorites = mutableListOf<String>()

    fun addFavorites(location: String){
        //kontrollieren, ob Ort schon vorhanden und Liste nicht grösser als 5, wenn nicht an freier Stelle hinzufügen
        if (favorites.contains(location)){
            println("Ort bereits vorhanden!")
        }
        if (favorites.size > 5){
            println("entferne ein Favoriten um einen Neuen zu erstellen")
        }
        else favorites.add(location)

    }

    fun removeFavorites(){
        if (true){
            favorites.removeAt(4)
            println("Favorit wurde aus Liste entfernt")
        }
        else println("es konnte kein Favorit entfernt werden")


    }

    fun showFavorites(){
       val sortedFavorites = favorites.sorted()
        println(sortedFavorites)

    }

}