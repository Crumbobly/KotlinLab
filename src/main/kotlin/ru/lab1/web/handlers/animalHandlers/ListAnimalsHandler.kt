package ru.lab1.web.handlers.animalHandlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Uri
import org.http4k.core.with
import org.http4k.lens.Query
import org.http4k.lens.int
import org.http4k.lens.string
import org.http4k.template.ViewModel
import ru.lab1.ITEMS_ON_PAGE
import ru.lab1.domain.Animal
import ru.lab1.domain.Family
import ru.lab1.domain.Gender
import ru.lab1.domain.Paginator
import ru.lab1.web.models.animals.ListAnimalsVM
import ru.lab1.web.templates.ContextAwareViewRender

class ListAnimalsHandler(
    private val htmlView: ContextAwareViewRender,
    private val dbFilter: (String?, String?, List<Family?>, List<Gender?>) -> List<Animal>,
) : HttpHandler {
    companion object {
        private val pageLens = Query.int().optional("page")
        private val nameLens = Query.string().optional("name")
        private val typeLens = Query.string().optional("type")
        private val familyLens = Query.multi.optional("family")
        private val genderLens = Query.multi.optional("gender")
    }

    override fun invoke(request: Request): Response {
        val page = pageLens.extract(request)
        val name = nameLens.extract(request)
        val type = typeLens.extract(request)
        val familiesStrings = familyLens.extract(request).orEmpty()
        val gendersStrings = genderLens.extract(request).orEmpty()

        val families = Family.getFromList(familiesStrings)
        val genders = Gender.getFromList(gendersStrings)

        val paginator = Paginator(ITEMS_ON_PAGE, request.uri, dbFilter(name, type, families, genders))

        val animals: List<Animal>
        val hasPrev: Boolean
        val hasNext: Boolean
        val firstPageToUri: Uri = paginator.getFirstPageToUri()
        val prevPageToUri: Uri?
        val nextPageToUri: Uri?
        val lastPageToUri: Uri?

        if (page == null) {
            animals = paginator.getAllObjects()
            hasPrev = false
            hasNext = false
            prevPageToUri = null
            nextPageToUri = null
            lastPageToUri = null
        } else {
            animals = paginator.getObjectListByPageNumber(page)
            hasPrev = paginator.hasPrev(page)
            hasNext = paginator.hasNext(page)
            prevPageToUri = paginator.getPrevPageToUri(page)
            nextPageToUri = paginator.getNextPageToUri(page)
            lastPageToUri = paginator.getLastPageToUri()
        }

        val model: ViewModel =
            ListAnimalsVM(
                animals,
                page,
                name,
                type,
                page,
                familiesStrings,
                gendersStrings,
                hasPrev,
                hasNext,
                firstPageToUri,
                prevPageToUri,
                nextPageToUri,
                lastPageToUri,
                paginator.uriWithoutPage,
            )

        return Response.invoke(Status.OK).with(htmlView(request) of model)
    }
}
