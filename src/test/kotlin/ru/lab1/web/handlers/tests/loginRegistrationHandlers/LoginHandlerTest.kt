package ru.lab1.web.handlers.tests.loginRegistrationHandlers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.kotest.haveStatus
import org.http4k.routing.bind
import org.http4k.routing.routes
import ru.lab1.web.handlers.HandlerData
import ru.lab1.web.handlers.loginRegistrationHandlers.LoginHandler

class LoginHandlerTest : FunSpec({

    val handlerData = HandlerData()
    val routes =
        routes(
            "/login" bind Method.GET to LoginHandler(handlerData.htmlView),
        )

    test("Login") {

        val request = Request(Method.GET, "/login")

        val result = routes(request)

        result.should(
            haveStatus(Status.OK),
        )
    }
})
