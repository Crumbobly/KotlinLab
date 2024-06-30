package ru.lab1.web.handlers.tests.animalHandlers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.and
import io.kotest.matchers.should
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.kotest.haveHeader
import org.http4k.kotest.haveStatus
import org.http4k.routing.bind
import org.http4k.routing.routes
import ru.lab1.web.handlers.HandlerData
import ru.lab1.web.handlers.animalHandlers.AnimalHandlerPost

class AnimalHandlerPostTest : FunSpec({

    val handlerData = HandlerData()
    val routes =
        routes(
            "/animals/animal{id}" bind Method.POST to
                AnimalHandlerPost(
                    handlerData.htmlView,
                    handlerData.db::deleteExam,
                    handlerData.db::addExam,
                    handlerData.db::deleteAnimal,
                ),
        )

    beforeEach {
        handlerData.reset()
    }

    test("Add exam to exist animal") {

        val request = Request(Method.POST, "/animals/animal1").body("idToAddExamAnimal=1&clinicName=123&doctor=123")
        val result = routes(request)

        result.should(
            haveStatus(Status.FOUND).and(haveHeader("Location", "/animals/animal1")),
        )
    }

    test("Add exam to not exist animal") {

        val request = Request(Method.POST, "/animals/animal1").body("idToAddExamAnimal=1234&clinicName=123&doctor=123")
        val result = routes(request)

        result.should(
            haveStatus(Status.BAD_REQUEST),
        )
    }

    test("Delete exist exam") {

        val request = Request(Method.POST, "/animals/animal1").body("idToDeleteExam=1&idToDeleteExamAnimal=1")
        val result = routes(request)

        result.should(
            haveStatus(Status.FOUND).and(haveHeader("Location", "/animals/animal1")),
        )
    }

    test("Delete not exist exam from exist animal") {

        val request = Request(Method.POST, "/animals/animal1").body("idToDeleteExam=4242&idToDeleteExamAnimal=1")
        val result = routes(request)

        result.should(
            haveStatus(Status.BAD_REQUEST),
        )
    }

    test("Delete not exist exam from not exist animal") {

        val request = Request(Method.POST, "/animals/animal12312").body("idToDeleteExam=4242&idToDeleteExamAnimal=12312")
        val result = routes(request)

        result.should(
            haveStatus(Status.BAD_REQUEST),
        )
    }

    test("Delete exist animal") {

        val request = Request(Method.POST, "/animals/animal1").body("idToDeleteAnimal=1")
        val result = routes(request)

        result.should(
            haveStatus(Status.FOUND).and(haveHeader("Location", "/animals?page=0")),
        )
    }
})
