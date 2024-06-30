package ru.lab1.web.handlers.animalHandlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.body.form
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import org.http4k.routing.path
import ru.lab1.domain.Animal
import ru.lab1.domain.User
import ru.lab1.web.models.animals.NewAnimalVM
import ru.lab1.web.templates.ContextAwareViewRender
import ru.lab1.web.validation.NewAnimalFormValidator

class NewAnimalHandlerPost(
    private val htmlView: ContextAwareViewRender,
    private val dbSetAnimal: (Animal) -> Unit,
    private val dbAddAnimalAndGetId: (Animal) -> Int,
    private val userKey: BiDiLens<Request, User?>,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val form = request.form()
        val animalIdToEdit = request.path("id")?.toIntOrNull()
        val userId = userKey(request)?.id

        val validator = NewAnimalFormValidator(form)
        val animal = validator.getAnimal()

        if (animal != null && animalIdToEdit != null && userId != null) {
            dbSetAnimal(animal.copy(id = animalIdToEdit))
            return Response(Status.FOUND).header("Location", "/animals/animal$animalIdToEdit")
        } else if (animal != null && userId != null) {
            val ifOfAddedAnimal = dbAddAnimalAndGetId(animal.copy(userId = userId))
            return Response(Status.FOUND).header("Location", "/animals/animal$ifOfAddedAnimal")
        } else {
            val newAnimalVm =
                NewAnimalVM(
                    validator.name,
                    validator.type,
                    validator.getFamily()?.name?.lowercase().orEmpty(),
                    validator.getGender()?.name?.lowercase().orEmpty(),
                    validator.description,
                    validator.dateOfBirth,
                    validator.getCertaintyDateOfBirth()?.name?.lowercase().orEmpty(),
                    validator.getErrorMsg("type"),
                    validator.getErrorMsg("dateOfBirth"),
                )
            return Response.invoke(Status.FOUND).with(htmlView(request) of newAnimalVm)
        }
    }
}
