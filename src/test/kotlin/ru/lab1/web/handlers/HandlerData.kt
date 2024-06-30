package ru.lab1.web.handlers

import org.http4k.core.ContentType
import ru.lab1.domain.Animal
import ru.lab1.domain.Family
import ru.lab1.domain.Gender
import ru.lab1.domain.Role
import ru.lab1.domain.User
import ru.lab1.domain.VetExamResult
import ru.lab1.web.templates.ContextAwarePebbleTemplates
import ru.lab1.web.templates.ContextAwareViewRender

class HandlerData {
    fun reset() {
        val authUser =
            User(
                "Логин1",
                "Не важно",
                123,
                Role.AUTHUSER,
                1,
            )

        val worker =
            User(
                "Логин2",
                "Не важно",
                123,
                Role.WORKER,
                2,
            )

        val vet =
            User(
                "Логин3",
                "Не важно",
                123,
                Role.VET,
                3,
            )
        val admin =
            User(
                "Логин4",
                "Не важно",
                123,
                Role.ADMIN,
                4,
            )

        val animal1 =
            Animal(
                Family.REPTILE,
                "Тип",
                "Имя1",
                Gender.FEMALE,
                null,
                null,
                "Описание",
                mutableListOf(
                    VetExamResult(
                        123,
                        "",
                        "",
                        "",
                        "",
                        1,
                    ),
                ),
                234234324,
                1,
                2,
            )

        val animal2 =
            Animal(
                Family.FISH,
                "Тип",
                "Имя2",
                Gender.UNDEFINED,
                null,
                null,
                "Описание",
                mutableListOf(),
                234234324,
                2,
                2,
            )

        val animal3 =
            Animal(
                Family.MAMMAL,
                "Тип",
                "Имя1",
                Gender.FEMALE,
                null,
                null,
                "Описание",
                mutableListOf(),
                234234324,
                3,
                3,
            )

        val animals: MutableList<Animal> = mutableListOf()
        val users: MutableList<User> = mutableListOf()

        animals.add(animal1)
        animals.add(animal2)
        animals.add(animal3)

        users.add(authUser)
        users.add(worker)
        users.add(vet)
        users.add(admin)

        db.setAnimals(animals)
        db.setUsers(users)
    }

    val db = FakeDB()
    val emptyDB = FakeDB()
    val templates = ContextAwarePebbleTemplates().HotReload("src/main/resources")
    val htmlView = ContextAwareViewRender.withContentType(templates, ContentType.TEXT_HTML)

    val animalPages = 1
    val usersPages = 1

    init {
        reset()
    }
}
