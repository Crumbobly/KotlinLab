package ru.lab1.domain

enum class Role(val ru: String) {
    ADMIN("Администратор"),
    MODERATOR("Модератор"),
    WORKER("Работник приюта"),
    VET("Ветеринар"),
    AUTHUSER("Авторизированный пользователь"),
    NOAUTHUSER("Не авторизированный пользователь"),
    ;

    companion object {
        fun getFromString(string: String): Role? {
            if (string.lowercase() == ADMIN.name.lowercase()) return ADMIN
            if (string.lowercase() == MODERATOR.name.lowercase()) return MODERATOR
            if (string.lowercase() == WORKER.name.lowercase()) return WORKER
            if (string.lowercase() == VET.name.lowercase()) return VET
            if (string.lowercase() == AUTHUSER.name.lowercase()) return AUTHUSER
            if (string.lowercase() == NOAUTHUSER.name.lowercase()) return NOAUTHUSER

            return null
        }
    }
}
