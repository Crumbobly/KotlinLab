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
import ru.lab1.web.handlers.animalHandlers.ListAnimalsHandler

class ListAnimalsHandlerTest : FunSpec({

    val handlerData = HandlerData()
    val routes =
        routes(
            "/animals" bind Method.GET to ListAnimalsHandler(handlerData.htmlView, handlerData.db::filter),
        )

    test("All animals view without filter") {
        val request = Request(Method.GET, "/animals")
        val result = routes(request)

        result.should(
            haveStatus(Status.OK),
        )
    }

    test("View page without filter") {
        val request = Request(Method.GET, "/animals?page=0")
        val result = routes(request)

        result.should(
            haveStatus(Status.OK),
        )
    }

    test("Empty animal list test") {
        val routesEmpty =
            routes(
                "/animals" bind Method.GET to ListAnimalsHandler(handlerData.htmlView, handlerData.emptyDB::filter),
            )
        val request = Request(Method.GET, "/animals?page=0")
        val result = routesEmpty(request)

        result.should(
            haveStatus(Status.OK),
        )
    }
})
