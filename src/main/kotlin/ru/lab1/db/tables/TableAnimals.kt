package ru.lab1.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import ru.lab1.domain.ConfidenceLevel
import ru.lab1.domain.Family
import ru.lab1.domain.Gender

object TableAnimals : IntIdTable() {
    val name = varchar("name", 50)
    val family = enumerationByName("family", 25, Family::class)
    val type = varchar("type", 50)
    val gender = enumerationByName("gender", 25, Gender::class)
    val dateOfBirth = long("dateOfBirth").nullable()
    val certaintyDateOfBirth = enumerationByName("certaintyDateOfBirth", 25, ConfidenceLevel::class).nullable()
    val description = varchar("description", 500).nullable()
    val datetimeOfAdding = long("datetimeOfAdding")
    val userId = reference("userId", TableUsers.id)
}
