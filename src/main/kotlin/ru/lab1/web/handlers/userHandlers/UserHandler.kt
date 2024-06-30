package ru.lab1.web.handlers.userHandlers

import org.http4k.core.*
import org.http4k.routing.path
import ru.lab1.domain.User
import ru.lab1.web.models.ErrorPageVM
import ru.lab1.web.models.users.UserVM
import ru.lab1.web.templates.ContextAwareViewRender
import kotlin.reflect.KFunction1

class UserHandler(private val htmlView: ContextAwareViewRender, private val dbGetUserById: KFunction1<Int, User?>) : HttpHandler {
    override fun invoke(request: Request): Response {
        val id = request.path("id")?.toIntOrNull()
        val user = id?.let { dbGetUserById(id) }

        if (user != null) {
            val userVM = UserVM(user)
            return Response.invoke(Status.OK).with(htmlView(request) of userVM)
        }

        return Response.invoke(Status.NOT_FOUND).with(htmlView(request) of ErrorPageVM())
    }
}
