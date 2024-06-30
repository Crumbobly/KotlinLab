package ru.lab1.web.handlers.animalHandlers

import org.http4k.core.*
import org.http4k.routing.path
import ru.lab1.domain.Animal
import ru.lab1.web.models.ErrorPageVM
import ru.lab1.web.models.animals.NewAnimalVM
import ru.lab1.web.templates.ContextAwareViewRender

class NewAnimalHandler(
    private val htmlView: ContextAwareViewRender,
    private val dbGetAnimalById: (Int) -> Animal?,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val id = request.path("id")?.toIntOrNull()
        val animal = id?.let { dbGetAnimalById(it) }

        if (id == null) {
            return Response.invoke(Status.OK).with(htmlView(request) of NewAnimalVM())
        } else if (animal == null) {
            return Response.invoke(Status.NOT_FOUND).with(htmlView(request) of ErrorPageVM())
        } else {
            return Response.invoke(Status.OK).with(htmlView(request) of NewAnimalVM(animal))
        }
    }
}
