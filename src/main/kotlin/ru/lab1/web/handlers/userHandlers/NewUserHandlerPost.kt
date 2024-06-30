package ru.lab1.web.handlers.userHandlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.body.form
import org.http4k.core.findSingle
import org.http4k.core.with
import ru.lab1.domain.Role
import ru.lab1.domain.User
import ru.lab1.web.models.users.NewUserVM
import ru.lab1.web.templates.ContextAwareViewRender

class NewUserHandlerPost(private val htmlView: ContextAwareViewRender, private val dbAddUser: (User) -> Unit) : HttpHandler {
    override fun invoke(request: Request): Response {
        val form = request.form()

        val name = form.findSingle("name")
        val password = form.findSingle("password")
        val stringRole = form.findSingle("role").orEmpty()
        val role = Role.getFromString(stringRole)

        if (name == null || password == null || role == null) {
            val model =
                NewUserVM(
                    name.orEmpty(),
                    password.orEmpty(),
                    stringRole,
                )
            return Response.invoke(Status.FOUND).with(htmlView(request) of model)
        }

        val user = User.createNewUser(name, password, role)
        dbAddUser(user)

        return Response.invoke(Status.FOUND).header("Location", "/users")
    }
}
