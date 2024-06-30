package ru.lab1.domain

import org.http4k.core.Uri
import org.http4k.core.query
import org.http4k.core.removeQuery
import kotlin.math.sign

class Paginator<Type>(private val objectsOnPage: Int, uriWithPage: Uri, private val objects: List<Type>) {
    val pageCount: Int = objects.size / objectsOnPage + (objects.size % objectsOnPage).sign
    val uriWithoutPage = uriWithPage.removeQuery("page")

    fun getObjectListByPageNumber(page: Int): List<Type> {
        if (page >= pageCount) {
            return listOf()
        }

        val start = objectsOnPage * page

        val stop: Int =
            if (start + objectsOnPage - 1 < objects.size) {
                start + objectsOnPage - 1
            } else {
                objects.size - 1
            }

        return objects.slice(start..stop)
    }

    fun getAllObjects(): List<Type> {
        return objects
    }

    fun getFirstPageToUri(): Uri {
        return uriWithoutPage.query("page", "0")
    }

    fun getNextPageToUri(page: Int): Uri? {
        if (pageCount == 0 || page >= pageCount - 1) {
            return null
        }

        return uriWithoutPage.query("page", "${page + 1}")
    }

    fun getPrevPageToUri(page: Int): Uri? {
        if (pageCount == 0 || page <= 0) {
            return null
        }

        return uriWithoutPage.query("page", "${page - 1}")
    }

    fun getLastPageToUri(): Uri? {
        if (pageCount <= 1) {
            return null
        }

        return uriWithoutPage.query("page", "${pageCount - 1}")
    }

    fun hasNext(page: Int): Boolean {
        return page < pageCount - 1
    }

    fun hasPrev(page: Int): Boolean {
        return page > 0
    }
}
