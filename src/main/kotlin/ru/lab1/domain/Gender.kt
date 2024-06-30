package ru.lab1.domain

enum class Gender(val ru: String) {
    MALE("М"),
    FEMALE("Ж"),
    UNDEFINED("Не определён"),
    ;

    companion object {
        fun getFromString(string: String): Gender? {
            if (string.lowercase() == MALE.name.lowercase()) return MALE
            if (string.lowercase() == FEMALE.name.lowercase()) return FEMALE
            if (string.lowercase() == UNDEFINED.name.lowercase()) return UNDEFINED
            return null
        }

        fun getFromList(list: List<String?>): List<Gender?> {
            val genders: MutableList<Gender?> = mutableListOf()

            for (s in list) {
                if (s == null) {
                    genders.add(null)
                } else {
                    genders.add(getFromString(s))
                }
            }

            return genders
        }
    }
}
