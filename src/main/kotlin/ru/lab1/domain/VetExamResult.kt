package ru.lab1.domain

import ru.lab1.utils.unixTimestampToLocalDateTime
import java.time.format.DateTimeFormatter

data class VetExamResult(
    val date: Long,
    val clinicName: String,
    val doctor: String,
    val examinationResult: String?,
    val conclusion: String?,
    val id: Int = 0,
) {
    fun getDateOfString(): String {
        return unixTimestampToLocalDateTime(date).format(DateTimeFormatter.ofPattern("dd-MM-YYYY, HH:mm:ss"))
    }
}
