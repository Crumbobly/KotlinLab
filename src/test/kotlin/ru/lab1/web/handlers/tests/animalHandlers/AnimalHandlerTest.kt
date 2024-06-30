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
import ru.lab1.web.handlers.animalHandlers.AnimalHandler

class AnimalHandlerTest : FunSpec({

    val handlerData = HandlerData()
    val routes =
        routes(
            "/animals/animal{id}" bind Method.GET to
                AnimalHandler(
                    handlerData.htmlView,
                    handlerData.db::getAnimalById,
                ),
        )

    test("Found Animal") {
        val request1 = Request(Method.GET, "/animals/animal1")
        val request3 = Request(Method.GET, "/animals/animal3")

        val result1 = routes(request1)
        val result3 = routes(request3)

        result1.should(
            haveStatus(Status.OK),
        )

        result3.should(
            haveStatus(Status.OK),
        )
    }

    test("Not Found Animal") {

        val request0 = Request(Method.GET, "/animals/animal0")
        val request4 = Request(Method.GET, "/animals/animal4")

        val result0 = routes(request0)
        val result4 = routes(request4)

        result0.should(
            haveStatus(Status.NOT_FOUND),
        )

        result4.should(
            haveStatus(Status.NOT_FOUND),
        )
    }
})
