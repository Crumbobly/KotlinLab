package ru.lab1.web.models.users

import org.http4k.template.ViewModel

class NewUserVM(
    val name: String = "",
    val password: String = "",
    val role: String = "",
) : ViewModel
