package ru.lab1.web.models.animals

import org.http4k.template.ViewModel
import ru.lab1.domain.Animal

class NewAnimalVM(
    val name: String = "",
    val type: String = "",
    val family: String = "",
    val gender: String = "",
    val description: String? = null,
    val dateOfBirth: String = "",
    val certaintyDateOfBirth: String = "",
    val typeError: String? = null,
    val dateOfBirthError: String? = null,
) : ViewModel {
    constructor(animal: Animal, typeError: String? = null, dateOfBirthError: String? = null) : this(
        animal.name,
        animal.type,
        animal.family.name.lowercase(),
        animal.gender.name.lowercase(),
        animal.description,
        animal.getBirthDateString(),
        animal.certaintyDateOfBirth?.name?.lowercase().orEmpty(),
        typeError = typeError,
        dateOfBirthError = dateOfBirthError,
    )
}
