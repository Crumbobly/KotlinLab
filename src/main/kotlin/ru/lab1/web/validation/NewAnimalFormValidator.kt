package ru.lab1.web.validation

import org.http4k.core.body.Form
import org.http4k.core.findSingle
import ru.lab1.domain.Animal
import ru.lab1.domain.ConfidenceLevel
import ru.lab1.domain.Family
import ru.lab1.domain.Gender
import ru.lab1.utils.localDateTimeToUnix
import ru.lab1.utils.localDateToUnix
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class NewAnimalFormValidator(form: Form) : Validator() {
    val name = form.findSingle("name").orEmpty()
    val type = form.findSingle("type").orEmpty()
    val familyString = form.findSingle("family").orEmpty()
    val genderString = form.findSingle("gender").orEmpty()
    val dateOfBirth = form.findSingle("dateOfBirth").orEmpty()
    val certaintyDateOfBirthString = form.findSingle("certaintyDateOfBirth").orEmpty()
    val description = form.findSingle("description")

    init {
        validation()
    }

    fun getAnimal(): Animal? {
        if (!isValid()) return null

        val family: Family = getFamily() ?: return null
        val type: String = type
        val name: String = name
        val gender: Gender = getGender() ?: return null
        val dateOfBirthUnix: Long? = getDateOfBirth()
        val certaintyDateOfBirth: ConfidenceLevel? = getCertaintyDateOfBirth()
        val description: String? = description

        return Animal(
            family, type, name, gender, dateOfBirthUnix, certaintyDateOfBirth, description, mutableListOf(),
            localDateTimeToUnix(
                LocalDateTime.now(),
            ),
            0, 0,
        )
    }

    override fun validation() {
        // name -
        if (name.isEmpty()) errors["name"] = "Имя не может быть пустым"

        // type
        if (type.contains(
                regex = Regex("[^а-яА-Яa-zA-Z-]"),
            )
        ) {
            errors["type"] = "Вид содержит запрещённый символ. Разрешены только буквы и знак '-'"
        }

        // family
        if (getFamily() == null) errors["family"] = "Недопустимое семейство"

        // gender
        if (getGender() == null) errors["gender"] = "Недопустимый пол"

        // date
        val parsedDateOfBirth = getDateOfBirth()
        if (parsedDateOfBirth == null && dateOfBirth != "") {
            errors["dateOfBirth"] = "Дата рождения не может быть больше текущего времени (UTC+0)"
        } else if (parsedDateOfBirth != null && parsedDateOfBirth > localDateToUnix(LocalDate.now())) {
            errors["dateOfBirth"] = "Дата рождения не может быть больше текущего времени (UTC+0)"
        }

        // certainty
        if (getCertaintyDateOfBirth() == null && certaintyDateOfBirthString != "") {
            errors["certainty"] = "Недопустимый уровень достоверности"
        }

        // description -
    }

    fun getFamily(): Family? = Family.getFromString(familyString)

    fun getGender(): Gender? = Gender.getFromString(genderString)

    fun getCertaintyDateOfBirth(): ConfidenceLevel? = ConfidenceLevel.getFromString(certaintyDateOfBirthString)

    private fun getDateOfBirth(): Long? {
        return try {
            localDateToUnix(LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        } catch (_: DateTimeParseException) {
            null
        }
    }
}
