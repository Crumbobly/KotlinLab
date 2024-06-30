package ru.lab1.web.handlers.loginRegistrationHandlers

import org.http4k.core.*
import ru.lab1.web.models.loginRegistration.LoginPageVM
import ru.lab1.web.templates.ContextAwareViewRender

class LoginHandler(private val htmlView: ContextAwareViewRender) : HttpHandler {
    override fun invoke(request: Request): Response {
        return Response.invoke(Status.OK).with(htmlView(request) of LoginPageVM())
    }
}
