import kotlinx.coroutines.sync.Mutex
import java.beans.XMLDecoder
import java.beans.XMLEncoder
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        Tobias Graf

  Beschreibung: Arbeitsdaten werden in Files Remanent abgespeichert
 */

data class WeatherData(
    var timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd 'T' HH:mm:ss")).toString(),
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var id: Int = 0,
    var temperature: Double = 0.0,
    var hourlyForecasts: MutableList<HourlyWrapper> = mutableListOf(),
    var weatherCode: Int = 0) : Storabledata {
    private val mutex = Mutex()

    override fun removeStoredWeatherData(from: Location) {
        try {
            val file = getStorageFileReference() ?: return
            if (!file.exists()) return

            val history = loadHistory(file)
            history.dataList.removeIf { it.id == from.id }

            val encoder = XMLEncoder(BufferedOutputStream(FileOutputStream(file)))
            encoder.writeObject(history)
            encoder.close()
        } catch (e: Exception) {
            System.err.println("Fehler bei der Löschung von Wetterobjekten aus dem XML-Storagefile")
            e.printStackTrace()
        }
    }

    override fun storeWeatherData(weather: Weather?) {
            try {
                if (weather != null) {
                    val dataset = WeatherData(
                        LocalDateTime.now().toString(),
                        weather.getLatitude(),
                        weather.getLongitude(),
                        weather.getLocationID(),
                        weather.getTemperature(),
                        weather.getHourlyForecasts()
                    )
                    val file = getStorageFileReference()
                    if (file != null) {
                        val history = if (file.exists()) loadHistory(file) else FileWrapper()
                        val allEntriesForLocation = history.dataList
                            .filter { it.id == dataset.id }
                            .sortedBy { it.timestamp }
                        val alreadyStoredToday = allEntriesForLocation.any { it.timestamp.startsWith(LocalDate.now().toString()) }
                        if (alreadyStoredToday) return

                        // Maximal-Anzahl von Ortswetterdaten, die gespeichert werden = 4
                        if (allEntriesForLocation.size >= 4) {
                            history.dataList.remove(allEntriesForLocation.first())
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
            } catch (e: Exception) {
                System.err.println("Fehler bei der Speicherung eines Wetterobjekts ins XML-Storagefile: ${e.message}")
                e.printStackTrace()
            }
    }

    override fun getEntriesForLocation(locationID: Int): List<WeatherData> {
        try {
            val file = getStorageFileReference() ?: return emptyList()
            if (!file.exists()) return emptyList()
            return loadHistory(file).dataList
                .filter { it.id == locationID }
                .sortedBy { it.timestamp }
        } catch (e: NullPointerException) {
            System.err.println("Fehler bei der XML-Abfrage: ${e.message}")
            e.printStackTrace()
            return emptyList()
        }
    }

    private fun getStorageFileReference(): File? {
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
            System.err.println("Fehler beim XML-Datenzugriff: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    private fun loadHistory(file: File): FileWrapper {
        return try {
            val decoder = XMLDecoder(
                BufferedInputStream(
                    FileInputStream(file)))

            val storedObject = decoder.readObject() as FileWrapper
            storedObject
        } catch (e: Exception) {
            e.printStackTrace()
            FileWrapper()
        }
    }


    private fun getFavoritesStorageFile(): File {
        // Holt den Pfad des globalen Benutzerordners
        val userHome = System.getProperty("user.home")
        // erstellt einen Ordner im Dateiensystem des Nutzers. Der Punkt steht für einen versteckten Ordner "XmlTest".
        val storageDirectory = Paths.get(userHome, ".Weather2b", "storage")

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
//                println("Favorit gespeichert in XML: ${favorite.location.name}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return favorite
        }

    }

