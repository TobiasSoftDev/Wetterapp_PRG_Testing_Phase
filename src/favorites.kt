class Favorites {
    private val favorites = mutableListOf<String>()


    fun addFavorites(weather: Weather) : Boolean {
        val location = weather.locationName
        //kontrollieren, ob Ort schon vorhanden und Liste nicht grösser als 5, wenn nicht an freier Stelle hinzufügen
        if (favorites.contains(location)) {
            println("Ort bereits vorhanden!")
            return false
        }
        if (favorites.size >=5) {
            println("entferne ein Favoriten um einen Neuen zu erstellen")
            return false
        } else
            favorites.add(location)
            return true
    }

    fun removeFavorites(weather: Weather): Boolean{
        if (favorites.contains(weather.locationName)){
            favorites.remove(weather.locationName)
            println("Favorit wurde aus Liste entfernt")
            return true
        }
        else println("es konnte kein Favorit entfernt werden")
            return false

    }

    fun getFavorites(): List<String> {
        return favorites.toList()
    }

    fun showFavorites(weatherCode:WeatherCodes, ) {
        val sortedFavorites = favorites.sorted()
        for (index in sortedFavorites.indices) {
            val location = favorites[index]
                print(" ${location}")
                println(" ${weatherCode.description}${weatherCode.icon} ")
            }
        }
    }


