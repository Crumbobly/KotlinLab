package ru.lab1.web.handlers.tests

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.kotest.haveStatus
import org.http4k.routing.bind
import org.http4k.routing.routes
import ru.lab1.web.handlers.HandlerData
import ru.lab1.web.handlers.NotFoundHandler

class NotFoundHandlerTest : FunSpec({

    val handlerData = HandlerData()
    val routes =
        routes(
            "{uri}" bind Method.GET to
                NotFoundHandler(
                    handlerData.htmlView,
                ),
        )

    test("Not found page test") {
        val request = Request(Method.GET, "/error")

        val result = routes(request)

        result.should(
            haveStatus(Status.NOT_FOUND),
        )
    }
})
