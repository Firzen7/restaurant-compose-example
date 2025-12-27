package net.firzen.android.restaurantcomposeexample


data class Restaurant(val id: Int,
                      val title: String,
                      val description: String,
                      var isFavourite: Boolean = false)

val dummyRestaurants = listOf(
    Restaurant(1, "The Matrix Diner", "Welcome to the desert of the real food"),
    Restaurant(2, "Nebuchadnezzar Café", "Fuel for unplugged minds"),
    Restaurant(3, "Zion Kitchen", "Home-grown flavors from the last human city"),
    Restaurant(4, "The Oracle’s Bakery", "You’ll love these cookies — already did"),
    Restaurant(5, "Red Pill Bistro", "Once you taste it, there’s no going back"),
    Restaurant(6, "Blue Pill Lounge", "Ignorance is bliss on the menu"),
    Restaurant(7, "Agent Smith Steakhouse", "Inevitable. Perfectly cooked."),
    Restaurant(8, "The Construct", "Anything you want, prepared instantly"),
    Restaurant(9, "Bullet Time Bar", "Drinks served impossibly fast"),
    Restaurant(10, "Morpheus Grill", "Free your mind, feed your body"),
    Restaurant(11, "Trinity Noodles", "Fast, sharp, and precise"),
    Restaurant(12, "Cypher’s Betrayal Brasserie", "Steaks that feel real enough"),
    Restaurant(13, "Sentinel Sushi", "They’re watching. Eat quickly."),
    Restaurant(14, "Reloaded Ramen", "Upgraded flavors, sequel-level heat"),
    Restaurant(15, "End of the Line Café", "Last stop before Zion")
)
