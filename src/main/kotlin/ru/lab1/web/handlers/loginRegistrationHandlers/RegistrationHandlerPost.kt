package ru.lab1.web.handlers.loginRegistrationHandlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.body.form
import org.http4k.core.with
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.lab1.domain.User
import ru.lab1.web.models.loginRegistration.RegistrationPageVM
import ru.lab1.web.templates.ContextAwareViewRender
import ru.lab1.web.validation.RegistrationFormValidator

class RegistrationHandlerPost(
    private val htmlView: ContextAwareViewRender,
    private val dbAddUser: (User) -> Unit,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val form = request.form()
        val validator = RegistrationFormValidator(form)
        val user = validator.getUser()

        if (user != null) {
            try {
                dbAddUser(user)
                return Response(Status.FOUND).header("Location", "/login")
            } catch (e: ExposedSQLException) {
                val registrationPageVM =
                    RegistrationPageVM(
                        validator.username,
                        validator.password,
                        "Пользователь с таким именем уже существует.",
                        validator.getErrorMsg("password"),
                    )
                return Response(Status.FOUND).with(htmlView(request) of registrationPageVM)
            }
        } else {
            val registrationPageVM =
                RegistrationPageVM(
                    validator.username,
                    validator.password,
                    validator.getErrorMsg("user"),
                    validator.getErrorMsg("password"),
                )
            return Response(Status.FOUND).with(htmlView(request) of registrationPageVM)
        }
    }
}
