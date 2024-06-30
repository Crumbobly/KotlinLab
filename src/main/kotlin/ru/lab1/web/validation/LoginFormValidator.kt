package ru.lab1.web.validation

import org.http4k.core.body.Form
import org.http4k.core.findSingle
import ru.lab1.domain.User

class LoginFormValidator(
    form: Form,
    private val dbCheckPassword: (String, String) -> Boolean,
    private val dbGetUserByName: (String) -> User?,
) : Validator() {
    val username = form.findSingle("username").orEmpty()
    val password = form.findSingle("password").orEmpty()
    val user = dbGetUserByName(username)

    init {
        validation()
    }

    fun getUserIfValid(): User? {
        if (!isValid()) return null
        return user
    }

    override fun validation() {
        // name
        if (username.isEmpty()) errors["username"] = "Имя не может быть пустым."

        if (user == null) errors["password"] = "Пароль не верный."

        // password
        if (!dbCheckPassword(username, password)) errors["password"] = "Пароль не верный."
    }
}
