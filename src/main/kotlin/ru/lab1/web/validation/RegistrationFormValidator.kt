package ru.lab1.web.validation

import org.http4k.core.body.Form
import org.http4k.core.findSingle
import ru.lab1.domain.User

class RegistrationFormValidator(form: Form) : Validator() {
    val username = form.findSingle("username").orEmpty()
    val password = form.findSingle("password").orEmpty()
    val confirmPassword = form.findSingle("confirmPassword").orEmpty()

    init {
        validation()
    }

    fun getUser(): User? {
        if (!isValid()) return null
        return User.createNewAuthUser(username, password)
    }

    override fun validation() {
        // name
        if (username.isEmpty()) errors["username"] = "Имя не может быть пустым."

        // password
        if (password != confirmPassword) errors["password"] = "Пароли не совпадают."

        if (password.isEmpty()) errors["password"] = "Пароль не может быть пустым."
    }
}
