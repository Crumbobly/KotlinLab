package ru.lab1.web.models

import org.http4k.template.ViewModel

class ErrorPageVM(val text: String = "Страница не найдена", val subtext: String = "") : ViewModel

fun getErrorLoginPageVM(): ErrorPageVM {
    return ErrorPageVM("Вы уже вошли в аккаунт.", "Для выполнения этого действия необходимо выйти из аккаунта.")
}

fun getErrorPermissionPageVM(): ErrorPageVM {
    return ErrorPageVM(
        "Недостаточно прав для просмотра страницы.",
        "Для выполнения этого действия необходимо иметь аккаунт с соответсвующее правами.",
    )
}

fun getErrorUnknownPageVM(): ErrorPageVM {
    return ErrorPageVM("Что-то пошло не так", "Если проблема не исчезнет, свяжитесь с владельцем ресурса.")
}
