package ru.lab1.web.handlers.tests.animalHandlers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.kotest.haveStatus
import org.http4k.routing.bind
import org.http4k.routing.routes
import ru.lab1.web.handlers.HandlerData
import ru.lab1.web.handlers.animalHandlers.NewAnimalHandler

class Delete()

class NewAnimalFormHandlerTest : FunSpec({

    val handlerData = HandlerData()

    val routes =
        routes(
            "/animals/new" bind Method.GET to
                NewAnimalHandler(
                    handlerData.htmlView,
                    handlerData.db::getAnimalById,
                ),
            "/animals/animal{id}/edit" bind Method.GET to
                NewAnimalHandler(
                    handlerData.htmlView,
                    handlerData.db::getAnimalById,
                ),
        )

    test("Add new animal") {
        val request = Request(Method.GET, "/animals/new")
        val result = routes(request)

        result.should(haveStatus(Status.OK))
    }

    test("Edit animal") {
        val request = Request(Method.GET, "/animals/animal1/edit?name=Имя&type=Тип1")
        val result = routes(request)

        result.should(haveStatus(Status.OK))
    }

    test("Not exist animal") {
        val notExistId = 10
        val request = Request(Method.GET, "/animals/animal$notExistId")
        val result = routes(request)

        result.should(haveStatus(Status.NOT_FOUND))
    }
})
