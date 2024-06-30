package ru.lab1.web.models.users

import org.http4k.template.ViewModel
import ru.lab1.domain.User

class UserVM(val user: User) : ViewModel
