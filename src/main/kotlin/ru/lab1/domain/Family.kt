package ru.lab1.domain

enum class Family(val ru: String) {
    MAMMAL("Млекопитающие"),
    BIRD("Птицы"),
    REPTILE("Рептилии"),
    FISH("Рыбы"),
    AMPHIBIAN("Амфибии"),
    INVERTEBRATE("Беспозвоночные"),
    ;

    companion object {
        fun getFromString(string: String): Family? {
            if (string.lowercase() == MAMMAL.name.lowercase()) return MAMMAL
            if (string.lowercase() == BIRD.name.lowercase()) return BIRD
            if (string.lowercase() == REPTILE.name.lowercase()) return REPTILE
            if (string.lowercase() == FISH.name.lowercase()) return FISH
            if (string.lowercase() == AMPHIBIAN.name.lowercase()) return AMPHIBIAN
            if (string.lowercase() == INVERTEBRATE.name.lowercase()) return INVERTEBRATE
            return null
        }

        fun getFromList(list: List<String?>): List<Family?> {
            val families: MutableList<Family?> = mutableListOf()

            for (s in list) {
                if (s == null) {
                    families.add(null)
                } else {
                    families.add(getFromString(s))
                }
            }

            return families
        }
    }
}
