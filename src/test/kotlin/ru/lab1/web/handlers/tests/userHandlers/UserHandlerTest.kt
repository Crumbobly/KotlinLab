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
import ru.lab1.web.handlers.userHandlers.UserHandler

class UserHandlerTest : FunSpec({

    val handlerData = HandlerData()
    val routes =
        routes(
            "/users/user{id}" bind Method.GET to
                UserHandler(
                    handlerData.htmlView,
                    handlerData.db::getUserById,
                ),
        )

    test("Found User") {
        val request = Request(Method.GET, "/users/user1")
        val result = routes(request)

        result.should(
            haveStatus(Status.OK),
        )
    }

    test("Not found User") {
        val request = Request(Method.GET, "/users/user2232")
        val result = routes(request)

        result.should(
            haveStatus(Status.NOT_FOUND),
        )
    }
})
