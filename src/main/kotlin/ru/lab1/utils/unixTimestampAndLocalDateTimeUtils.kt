package ru.lab1.utils

import java.time.*

fun unixTimestampToLocalDateTime(unixTimestamp: Long): LocalDateTime {
    return Instant.ofEpochSecond(unixTimestamp).atZone(ZoneOffset.UTC).toLocalDateTime()
}

fun localDateTimeToUnix(dateTime: LocalDateTime): Long {
    return dateTime.toEpochSecond(ZoneOffset.UTC)
}

fun localDateToUnix(date: LocalDate): Long {
    return date.toEpochSecond(LocalTime.NOON, ZoneOffset.UTC)
}
