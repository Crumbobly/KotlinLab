package ru.lab1.domain

import ru.lab1.utils.unixTimestampToLocalDateTime
import java.time.format.DateTimeFormatter

data class Animal(
    val family: Family,
    val type: String,
    val name: String,
    val gender: Gender,
    val dateOfBirthUnix: Long?,
    val certaintyDateOfBirth: ConfidenceLevel?,
    val description: String?,
    private val vetExamResults: MutableList<VetExamResult> = mutableListOf(),
    val datetimeOfAddingUnix: Long,
    val id: Int,
    val userId: Int,
) {
    fun getVetExamResults(): MutableList<VetExamResult> {
        return vetExamResults
    }

    fun getBirthDateString(): String {
        return dateOfBirthUnix?.let { unixTimestampToLocalDateTime(it).toLocalDate().toString() } ?: "Нет информации"
    }

    fun getCertaintyDateOfBirthString(): String {
        return certaintyDateOfBirth?.ru ?: "Нет информации"
    }

    fun getDatetimeOfAddingString(): String {
        return unixTimestampToLocalDateTime(datetimeOfAddingUnix).format(DateTimeFormatter.ofPattern("dd-MM-YYYY, HH:mm:ss"))
    }
}
