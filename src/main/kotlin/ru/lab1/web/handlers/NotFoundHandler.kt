package ru.lab1.web.handlers

import org.http4k.core.*
import ru.lab1.web.models.ErrorPageVM
import ru.lab1.web.templates.ContextAwareViewRender

class NotFoundHandler(private val htmlView: ContextAwareViewRender) : HttpHandler {
    override fun invoke(request: Request): Response {
        return Response.invoke(Status.NOT_FOUND).with(htmlView(request) of ErrorPageVM())
    }
}
