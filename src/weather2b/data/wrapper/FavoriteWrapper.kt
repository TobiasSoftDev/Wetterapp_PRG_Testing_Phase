/*
  Projekt:      Wetterapp
  Firma:        ABB Technikerschule
  Autor:        P.Theiler

  Beschreibung: Hier wird die Favoriten Klasse für das Speichern in den Files in Listen gebuendelt und abgepackt.
 */

package weather2b.data.wrapper

import weather2b.data.favorites.Favorite

class FavoriteWrapper {
    var favorites: MutableList<Favorite> = mutableListOf()
}