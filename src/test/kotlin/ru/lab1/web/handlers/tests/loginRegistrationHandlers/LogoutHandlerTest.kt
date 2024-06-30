package ru.lab1.web.handlers.tests.loginRegistrationHandlers

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
import ru.lab1.web.handlers.loginRegistrationHandlers.LogoutHandler

class LogoutHandlerTest : FunSpec({

    val handlerData = HandlerData()
    val routes =
        routes(
            "/logout" bind Method.GET to LogoutHandler(),
        )

    test("Login") {

        val request = Request(Method.GET, "/logout")

        val result = routes(request)

        result.should(
            haveStatus(Status.FOUND).and(haveHeader("Location", "/")),
        )
    }
})
