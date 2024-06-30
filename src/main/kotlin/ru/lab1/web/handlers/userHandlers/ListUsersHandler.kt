package ru.lab1.web.handlers.userHandlers

import org.http4k.core.*
import org.http4k.lens.Query
import org.http4k.lens.int
import org.http4k.template.ViewModel
import ru.lab1.ITEMS_ON_PAGE
import ru.lab1.domain.Paginator
import ru.lab1.domain.User
import ru.lab1.web.models.users.ListUsersVM
import ru.lab1.web.templates.ContextAwareViewRender

class ListUsersHandler(private val htmlView: ContextAwareViewRender, private val dbGetAllUsers: () -> List<User>) : HttpHandler {
    companion object {
        private val pageLens = Query.int().defaulted("page", 0)
    }

    override fun invoke(request: Request): Response {
        val page = pageLens.extract(request)

        val paginator = Paginator(ITEMS_ON_PAGE, request.uri, dbGetAllUsers())

        val users: List<User> = paginator.getObjectListByPageNumber(page)
        val hasPrev: Boolean = paginator.hasPrev(page)
        val hasNext: Boolean = paginator.hasNext(page)
        val firstPageToUri: Uri = paginator.getFirstPageToUri()
        val prevPageToUri: Uri? = paginator.getPrevPageToUri(page)
        val nextPageToUri: Uri? = paginator.getNextPageToUri(page)
        val lastPageToUri: Uri? = paginator.getLastPageToUri()

        val model: ViewModel =
            ListUsersVM(
                users,
                page,
                hasPrev,
                hasNext,
                firstPageToUri,
                prevPageToUri,
                nextPageToUri,
                lastPageToUri,
            )

        return Response.invoke(Status.OK).with(htmlView(request) of model)
    }
}
