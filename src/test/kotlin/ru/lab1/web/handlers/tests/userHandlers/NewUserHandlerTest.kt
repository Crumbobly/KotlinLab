package ru.lab1.web.handlers.tests.userHandlers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.kotest.haveStatus
import org.http4k.routing.bind
import org.http4k.routing.routes
import ru.lab1.web.handlers.HandlerData
import ru.lab1.web.handlers.userHandlers.NewUserHandler

class NewUserHandlerTest : FunSpec({

    val handlerData = HandlerData()
    val routes =
        routes(
            "/users/new" bind Method.GET to
                NewUserHandler(
                    handlerData.htmlView,
                ),
        )

    test("New User Test") {
        val request = Request(Method.GET, "/users/new")
        val result = routes(request)

        result.should(
            haveStatus(Status.OK),
        )
    }
})
