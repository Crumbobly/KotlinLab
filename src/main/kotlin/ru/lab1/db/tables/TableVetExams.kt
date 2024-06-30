package ru.lab1.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object TableVetExams : IntIdTable() {
    val date = long("date")
    val clinicName = varchar("clinicName", 50)
    val doctor = varchar("doctor", 50)
    val examinationResult = varchar("examinationResult", 250).nullable()
    val conclusion = varchar("conclusion", 250).nullable()
    val animalId = reference("animalId", TableAnimals.id, onDelete = ReferenceOption.CASCADE)
}
