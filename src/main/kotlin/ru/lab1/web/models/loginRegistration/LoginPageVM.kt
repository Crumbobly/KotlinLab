package ru.lab1.web.models.loginRegistration

import org.http4k.template.ViewModel

class LoginPageVM(
    val name: String = "",
    val password: String = "",
    val nameError: String? = null,
    val passwordError: String? = null,
) : ViewModel
