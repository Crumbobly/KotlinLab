package ru.lab1.security

import org.http4k.core.Filter
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookies
import org.http4k.core.with
import org.http4k.lens.RequestContextLens
import ru.lab1.db.MyAnimalsDB
import ru.lab1.domain.Permissions
import ru.lab1.domain.User
import ru.lab1.web.models.getErrorLoginPageVM
import ru.lab1.web.models.getErrorPermissionPageVM
import ru.lab1.web.templates.ContextAwareViewRender

fun jwtFilter(
    userKey: RequestContextLens<User?>,
    permissionKey: RequestContextLens<Permissions?>,
    jwtTools: JwtTools,
    db: MyAnimalsDB,
) = Filter { next ->
    { request ->
        val user =
            getUserFromCookie(
                request.cookies().find { it.name == "auth" },
                jwtTools,
                db,
            )
        next(
            request.with(
                userKey of user,
                permissionKey of user?.let { Permissions(it.role) },
            ),
        )
    }
}

fun loginFilter(
    htmlView: ContextAwareViewRender,
    userKey: RequestContextLens<User?>,
) = Filter { next ->
    { request ->
        val user = userKey(request)
        if (user == null) {
            next(request)
        } else {
            Response.invoke(Status.FORBIDDEN).with(htmlView(request) of getErrorLoginPageVM())
        }
    }
}

fun permissionsFilter(
    htmlView: ContextAwareViewRender,
    userKey: RequestContextLens<User?>,
    canUse: (Permissions) -> Boolean,
) = Filter { next ->
    { request ->
        val user = userKey(request)
        if (user != null && canUse(Permissions(user.role))) {
            next(request)
        } else {
            Response.invoke(Status.FORBIDDEN).with(htmlView(request) of getErrorPermissionPageVM())
        }
    }
}

private fun getUserFromCookie(
    cookie: Cookie?,
    jwtTools: JwtTools,
    db: MyAnimalsDB,
): User? {
    if (cookie != null) {
        return jwtTools.verifyJwtToken(cookie.value)?.let { db.getUserById(it.toInt()) }
    }
    return null
}
