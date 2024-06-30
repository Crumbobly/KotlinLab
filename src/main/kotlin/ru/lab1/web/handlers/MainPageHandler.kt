package ru.lab1.web.handlers

import org.http4k.core.*
import ru.lab1.web.models.MainPageVM
import ru.lab1.web.templates.ContextAwareViewRender

class MainPageHandler(private val htmlView: ContextAwareViewRender) : HttpHandler {
    override fun invoke(request: Request): Response {
        return Response.invoke(Status.OK).with(htmlView(request) of MainPageVM())
    }
}
