package ru.lab1.web.handlers.animalHandlers

import org.http4k.core.*
import org.http4k.routing.path
import ru.lab1.domain.Animal
import ru.lab1.web.models.ErrorPageVM
import ru.lab1.web.models.animals.AnimalVM
import ru.lab1.web.templates.ContextAwareViewRender

class AnimalHandler(
    private val htmlView: ContextAwareViewRender,
    private val dbGetAnimalById: (Int) -> Animal?,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val id = request.path("id")?.toIntOrNull()
        val animal = id?.let { dbGetAnimalById(id) }

        if (animal != null) {
            val animalVM = AnimalVM(animal)
            return Response.invoke(Status.OK).with(htmlView(request) of animalVM)
        }

        return Response.invoke(Status.NOT_FOUND).with(htmlView(request) of ErrorPageVM())
    }
}
