package ru.lab1.web.handlers.loginRegistrationHandlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.body.form
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.core.with
import ru.lab1.domain.User
import ru.lab1.security.JwtTools
import ru.lab1.web.models.MainPageVM
import ru.lab1.web.models.loginRegistration.LoginPageVM
import ru.lab1.web.templates.ContextAwareViewRender
import ru.lab1.web.validation.LoginFormValidator

class LoginPageHandlerPost(
    private val htmlView: ContextAwareViewRender,
    private val dbGetUserById: (String) -> User?,
    private val dbCheckPassword: (String, String) -> Boolean,
    private val jwtTools: JwtTools,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val form = request.form()
        val validator = LoginFormValidator(form, dbCheckPassword, dbGetUserById)
        val user = validator.getUserIfValid()

        if (user == null) {
            val loginPageVM =
                LoginPageVM(
                    validator.username,
                    validator.password,
                    validator.getErrorMsg("username"),
                    validator.getErrorMsg("password"),
                )
            return Response.invoke(Status.NOT_FOUND).with(htmlView(request) of loginPageVM)
        }

        val token = jwtTools.createJwtToken(user.id)
        return Response.invoke(Status.FOUND)
            .header("Location", "/")
            .cookie(Cookie("auth", token))
            .with(htmlView(request) of MainPageVM())
    }
}
