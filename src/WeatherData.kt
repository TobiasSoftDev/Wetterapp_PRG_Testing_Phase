
import java.beans.XMLDecoder
import java.beans.XMLEncoder
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Arbeitsdaten werden hier gespeichert
  und die Guetepruefung durchgeführt
 */

data class WeatherData(
    var timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd 'T' HH:mm:ss")).toString(),
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var id: Int = 0,
    var temperature: Double = 0.0,
    var hourlyForecasts: MutableList<HourlyWrapper> = mutableListOf(),
    var weatherCode: Int = 0) : Storabledata {

    override fun storeWeatherData(weather: Weather?) {

        if (weather != null) {
            val dataset = WeatherData(
                LocalDateTime.now().toString(),
                weather.getLatitude(),
                weather.getLongitude(),
                weather.getLocationID(),
                weather.getTemperature(),
                weather.getHourlyForecasts()
            )
            val file = getStorageFile()
            if (file != null) {
                val history = if (file.exists()) loadHistory(file) else FileWrapper()
                val entriesForLocation = history.dataList
                    .filter { it.id == dataset.id }
                    .sortedBy { it.timestamp }
                // Maxmal-Anzahl von Ortswetterdaten, die gespeichert werden = 4
                if (entriesForLocation.size >= 4) {
                    history.dataList.remove(entriesForLocation.first())
                }
                history.dataList.add(dataset)

                val encoder = XMLEncoder(               // Stream bereitstellen
                    BufferedOutputStream(
                        FileOutputStream(file)
                    )
                )
                encoder.writeObject(history)            // Objekt speichern
                encoder.close()                         // Stream schliessen
            }
        }
    }

    override fun getEntriesForLocation(locationID: Int): List<WeatherData> {
        val file = getStorageFile() ?: return emptyList()
        if (!file.exists()) return emptyList()
        return loadHistory(file).dataList
            .filter { it.id == locationID }
            .sortedBy { it.timestamp }
    }

    private fun getStorageFile(): File? {
        return try {
            // Holt den Pfad des globalen Benutzerordners (bspw. Mac: /users/peterkoch)
            val userHome = System.getProperty("user.home")
            // erstellt einen Ordner im Dateiensystem des Nutzers. Der Punkt steht für einen versteckten Ordner "XmlTest".
            val storageDirectory = Paths.get(userHome, ".Weather2b", "storage")

            if (!Files.exists(storageDirectory)) {
                Files.createDirectories(storageDirectory)
            }
            storageDirectory.resolve("storageFile.xml").toFile()
        } catch (e: Exception) {
            System.err.println("Fehler beim Datenzugriff: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    private fun loadHistory(file: File): FileWrapper {
        try {
            val decoder = XMLDecoder(
                BufferedInputStream(
                    FileInputStream(file)))

            val storedObject = decoder.readObject() as FileWrapper
            return storedObject
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return FileWrapper()
    }

        override fun getWeatherHistoryFromLocation(locationID: Int): List<WeatherData> {
            val file = getStorageFile()
            if (file == null || !file.exists()) return emptyList()
            val history = loadHistory(file)
            println("History des gesuchten Orts: ${history.dataList.filter { it.id == locationID }}")
            return history.dataList.filter { it.id == locationID }
        }

        private fun getFavoritesStorageFile(): File {
            // Holt den Pfad des globalen Benutzerordners
            val userHome = System.getProperty("user.home")
            // erstellt einen Ordner im Dateiensystem des Nutzers. Der Punkt steht für einen versteckten Ordner "XmlTest".
            val storageDirectory = Paths.get(userHome, ".Weather2b", "storage", "favorites")

            if (!Files.exists(storageDirectory)) {
                Files.createDirectories(storageDirectory)
            }
            return storageDirectory.resolve("favoritesList.xml").toFile()
        }

        private fun loadFavorites(file: File): FavoriteWrapper {
            try {
                val decoder = XMLDecoder(
                    BufferedInputStream(
                        FileInputStream(file)
                    )
                )

                val storedObject = decoder.readObject() as FavoriteWrapper
                return storedObject
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return FavoriteWrapper()
        }

        override fun readFavorites() {
            val file = getFavoritesStorageFile()
            if (!file.exists()) {
                return println("keine Favoriten-Datei gefunden")
            }

            val wrapper = loadFavorites(file)
            print("Gespeicherte Favoriten :")
            wrapper.favorites.forEach { fav ->
                println("ID: ${fav.location.id}---Name: ${fav.location.name} ")
            }
        }

        override fun getAllFavorites(): List<Favorite> {
            val file = getFavoritesStorageFile()
            if (!file.exists()) {
                return emptyList()
            } else return loadFavorites(file).favorites
        }

        override fun storeFavorites(favorite: Favorite): Favorite {
            val file = getFavoritesStorageFile()
            val wrapper = if (file.exists()) {
                loadFavorites(file)
            } else {
                FavoriteWrapper()
            }
            if (wrapper.favorites.none { it.location.id == favorite.location.id }) {
                wrapper.favorites.add(favorite)
            }

            try {
                val encoder = XMLEncoder(               // Stream bereitstellen
                    BufferedOutputStream(
                        FileOutputStream(file)
                    )
                )
                encoder.writeObject(wrapper)            // Objekt speichern
                encoder.close()                         // Stream schliessen
                println("Favorit gespeichert in XML: ${favorite.location.name}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return favorite
        }

    }


//    override fun readWeatherDataDaily() {
//        val file = File("resources/dailyData/DailyWeatherData$date.txt")
//
//        val lines = file.readLines()
//        for (line in lines) {
//            print(line)
//        }
//        return emptyList()
//    }

  /*  override fun readFavorites() {
        val file = File("resources/favoriteLocationData/Favorites.xml")

        val lines = file.readLines()
        for (line in lines) {
            print (line)
        }
    } */

//    override fun checkAccuracy() {
//
//    }

