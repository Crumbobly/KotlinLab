package ru.lab1.web.handlers.userHandlers

import org.http4k.core.*
import ru.lab1.web.models.users.NewUserVM
import ru.lab1.web.templates.ContextAwareViewRender

class NewUserHandler(private val htmlView: ContextAwareViewRender) : HttpHandler {
    override fun invoke(request: Request): Response {
        return Response.invoke(Status.OK).with(htmlView(request) of NewUserVM())
    }
}
