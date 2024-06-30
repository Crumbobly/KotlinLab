package ru.lab1

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.then
import org.http4k.lens.BiDiLens
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import ru.lab1.db.MyAnimalsDB
import ru.lab1.domain.Permissions
import ru.lab1.domain.User
import ru.lab1.security.JwtTools
import ru.lab1.security.loginFilter
import ru.lab1.security.permissionsFilter
import ru.lab1.web.handlers.MainPageHandler
import ru.lab1.web.handlers.NotFoundHandler
import ru.lab1.web.handlers.animalHandlers.*
import ru.lab1.web.handlers.loginRegistrationHandlers.*
import ru.lab1.web.handlers.userHandlers.*
import ru.lab1.web.templates.ContextAwareViewRender

class Router {
    fun router(
        db: MyAnimalsDB,
        jwtTools: JwtTools,
        userLens: BiDiLens<Request, User?>,
        htmlViewWithContext: ContextAwareViewRender,
    ) = routes(
        "/" bind Method.GET to MainPageHandler(htmlViewWithContext),
        "/logout" bind Method.GET to LogoutHandler(),
        "/login" bind Method.GET to
            loginFilter(htmlViewWithContext, userLens)
                .then(LoginHandler(htmlViewWithContext)),
        "/login" bind Method.POST to LoginPageHandlerPost(htmlViewWithContext, db::getUserByName, db::checkPassword, jwtTools),
        "/registration" bind Method.GET to
            loginFilter(htmlViewWithContext, userLens)
                .then(RegistrationHandler(htmlViewWithContext)),
        "/registration" bind Method.POST to RegistrationHandlerPost(htmlViewWithContext, db::addUserWithSQL),
        "/users" bind Method.GET to
            permissionsFilter(htmlViewWithContext, userLens, Permissions::canSeeListUser)
                .then(
                    ListUsersHandler(htmlViewWithContext, db::getAllUsers),
                ),
        "/users/new" bind Method.GET to
            permissionsFilter(htmlViewWithContext, userLens, Permissions::canAddDelUser)
                .then(
                    NewUserHandler(htmlViewWithContext),
                ),
        "/users/new" bind Method.POST to NewUserHandlerPost(htmlViewWithContext, db::addUserWithSQL),
        "/users/user{id}" bind Method.GET to
            permissionsFilter(htmlViewWithContext, userLens, Permissions::canSeeListUser)
                .then(
                    UserHandler(htmlViewWithContext, db::getUserById),
                ),
        "/users/user{id}" bind Method.POST to
            permissionsFilter(htmlViewWithContext, userLens, Permissions::canAddDelUser)
                .then(
                    UserHandlerPost(htmlViewWithContext, db::setUserRoleWithSQL, db::deleteUserWithSQL),
                ),
        "/animals" bind Method.GET to ListAnimalsHandler(htmlViewWithContext, db::filter),
        "/animals/new" bind Method.GET to
            permissionsFilter(htmlViewWithContext, userLens, Permissions::canAddModDelHisAnimal)
                .then(
                    NewAnimalHandler(htmlViewWithContext, db::getAnimalById),
                ),
        "/animals/new" bind Method.POST to
            permissionsFilter(htmlViewWithContext, userLens, Permissions::canAddModDelHisAnimal)
                .then(
                    NewAnimalHandlerPost(htmlViewWithContext, db::setAnimalWithSQL, db::addAnimalAndGetIdWithSQL, userLens),
                ),
        "/animals/animal{id}" bind Method.GET to AnimalHandler(htmlViewWithContext, db::getAnimalById),
        "/animals/animal{id}" bind Method.POST to AnimalHandlerPost(htmlViewWithContext, db::deleteExamWithSQL, db::addExamWithSQL, db::deleteAnimalWithSQL),
        "/animals/animal{id}/edit" bind Method.GET to
            permissionsFilter(htmlViewWithContext, userLens, Permissions::canAddModDelHisAnimal)
                .then(
                    NewAnimalHandler(htmlViewWithContext, db::getAnimalById),
                ),
        "/animals/animal{id}/edit" bind Method.POST to
            permissionsFilter(htmlViewWithContext, userLens, Permissions::canAddModDelHisAnimal)
                .then(
                    NewAnimalHandlerPost(htmlViewWithContext, db::setAnimalWithSQL, db::addAnimalAndGetIdWithSQL, userLens),
                ),
        "{other_uri}" bind Method.GET to NotFoundHandler(htmlViewWithContext),
        static(ResourceLoader.Classpath("/ru/lab1/public")),
    )
}
