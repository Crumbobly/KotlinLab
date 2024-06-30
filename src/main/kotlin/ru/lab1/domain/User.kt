package ru.lab1.domain

import ru.lab1.utils.localDateTimeToUnix
import ru.lab1.utils.unixTimestampToLocalDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class User(
    val name: String,
    val password: String,
    val date: Long,
    val role: Role,
    val id: Int = 0,
) {
    companion object {
        fun createNewAuthUser(
            name: String,
            password: String,
        ): User {
            return User(name, password, localDateTimeToUnix(LocalDateTime.now()), Role.AUTHUSER)
        }

        fun createNewUser(
            name: String,
            password: String,
            role: Role,
        ): User {
            return User(name, password, localDateTimeToUnix(LocalDateTime.now()), role)
        }
    }

    fun getDatetimeOfAddingString(): String {
        return unixTimestampToLocalDateTime(date).format(DateTimeFormatter.ofPattern("dd-MM-YYYY, HH:mm:ss"))
    }
}
