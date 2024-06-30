package ru.lab1.web.handlers.userHandlers

import org.http4k.core.*
import org.http4k.core.body.form
import ru.lab1.domain.Role
import ru.lab1.web.models.getErrorUnknownPageVM
import ru.lab1.web.templates.ContextAwareViewRender

class UserHandlerPost(
    private val htmlView: ContextAwareViewRender,
    private val dbSetUserRole: (Int, Role) -> Unit,
    private val dbDeleteUser: (Int) -> Unit,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val form = request.form()

        val id = form.findSingle("userId")?.toIntOrNull()
        val idToDeleteUser = form.findSingle("idToDeleteUser")?.toIntOrNull()
        val stringRole = form.findSingle("role").orEmpty()
        val role = Role.getFromString(stringRole)

        try {
            if (idToDeleteUser != null) {
                dbDeleteUser(idToDeleteUser)
                return Response.invoke(Status.FOUND).header("Location", "/users")
            }

            if (id != null && role != null) {
                dbSetUserRole(id, role)
                return Response.invoke(Status.FOUND).header("Location", "/users/user$id")
            }
        } catch (_: IllegalArgumentException) {
        }

        return Response.invoke(Status.FOUND).with(htmlView(request) of getErrorUnknownPageVM())
    }
}
