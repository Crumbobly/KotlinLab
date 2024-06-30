package ru.lab1.web.models.users

import org.http4k.core.Uri
import org.http4k.template.ViewModel
import ru.lab1.domain.User

class ListUsersVM(
    val users: List<User>,
    val page: Int? = null,
    val hasPrevPage: Boolean,
    val hasNextPage: Boolean,
    val firstPageToUri: Uri?,
    val prevPageToUri: Uri?,
    val nextPageToUri: Uri?,
    val lastPageToUri: Uri?,
) : ViewModel
