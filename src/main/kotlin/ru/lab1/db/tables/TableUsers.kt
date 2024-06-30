package ru.lab1.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import ru.lab1.domain.Role

object TableUsers : IntIdTable() {
    val name = varchar("name", 50)
    val password = varchar("password", 1000)
    val date = long("date")
    val role = enumerationByName("role", 50, Role::class)
    val isDeleted = integer("isDeleted")
}
