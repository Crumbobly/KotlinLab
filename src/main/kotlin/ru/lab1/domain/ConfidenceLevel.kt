package ru.lab1.domain

enum class ConfidenceLevel(val ru: String) {
    ABSOLUTE("Абсолютный"),
    HIGH("Высокий"),
    MEDIUM("Средний"),
    LOW("Низкий"),
    ;

    companion object {
        fun getFromString(string: String): ConfidenceLevel? {
            if (string.lowercase() == ABSOLUTE.name.lowercase()) return ABSOLUTE
            if (string.lowercase() == HIGH.name.lowercase()) return HIGH
            if (string.lowercase() == MEDIUM.name.lowercase()) return MEDIUM
            if (string.lowercase() == LOW.name.lowercase()) return LOW
            return null
        }
    }
}
