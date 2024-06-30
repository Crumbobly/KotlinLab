package ru.lab1.web.models.animals

import org.http4k.core.Uri
import org.http4k.template.ViewModel
import ru.lab1.domain.Animal

class ListAnimalsVM(
    val animals: List<Animal>,
    val page: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val currPageNumber: Int?,
    val familiesStrings: List<String?>,
    val gendersString: List<String?>,
    val hasPrevPage: Boolean,
    val hasNextPage: Boolean,
    val firstPageToUri: Uri?,
    val prevPageToUri: Uri?,
    val nextPageToUri: Uri?,
    val lastPageToUri: Uri?,
    val uriWithoutPage: Uri,
) : ViewModel
