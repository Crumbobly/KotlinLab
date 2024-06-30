package ru.lab1.web.handlers.animalHandlers

import org.http4k.core.*
import org.http4k.core.body.form
import ru.lab1.web.models.getErrorUnknownPageVM
import ru.lab1.web.templates.ContextAwareViewRender

class AnimalHandlerPost(
    private val htmlView: ContextAwareViewRender,
    private val dbDeleteExam: (Int, Int) -> Unit,
    private val dbAddExam: (Int, String, String, String, String) -> Unit,
    private val dbDeleteAnimal: (Int) -> Unit,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val form = request.form()

        val idToDeleteExamAnimal = form.findSingle("idToDeleteExamAnimal")?.toIntOrNull()
        val idToDeleteAnimal = form.findSingle("idToDeleteAnimal")?.toIntOrNull()
        val idToAddExamAnimal = form.findSingle("idToAddExamAnimal")?.toIntOrNull()
        val idToDeleteExam = form.findSingle("idToDeleteExam")?.toIntOrNull()

        val clinicName = form.findSingle("clinicName")
        val doctor = form.findSingle("doctor")
        val examinationResult = form.findSingle("examinationResult").orEmpty()
        val conclusion = form.findSingle("conclusion").orEmpty()

        try {
            if (idToDeleteExam != null && idToDeleteExamAnimal != null) {
                dbDeleteExam(idToDeleteExam, idToDeleteExamAnimal)
                return Response.invoke(Status.FOUND).header("Location", "/animals/animal$idToDeleteExamAnimal")
            } else if (idToDeleteAnimal != null) {
                dbDeleteAnimal(idToDeleteAnimal)
                return Response(Status.FOUND).header("Location", "/animals?page=0")
            } else if (idToAddExamAnimal != null && clinicName != null && doctor != null) {
                dbAddExam(idToAddExamAnimal, clinicName, doctor, examinationResult, conclusion)
                return Response.invoke(Status.FOUND).header("Location", "/animals/animal$idToAddExamAnimal")
            }
        } catch (_: IllegalArgumentException) {
        }

        return Response.invoke(Status.BAD_REQUEST).with(htmlView(request) of getErrorUnknownPageVM())
    }
}
