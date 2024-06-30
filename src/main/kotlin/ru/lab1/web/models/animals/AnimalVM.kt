package ru.lab1.web.models.animals

import org.http4k.template.ViewModel
import ru.lab1.domain.Animal

class AnimalVM(val animal: Animal) : ViewModel
