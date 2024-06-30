package ru.lab1

import org.http4k.core.ContentType
import org.http4k.core.RequestContexts
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.lens.RequestContextKey
import org.http4k.server.Netty
import org.http4k.server.asServer
import ru.lab1.config.*
import ru.lab1.db.MyAnimalsDB
import ru.lab1.domain.Permissions
import ru.lab1.domain.User
import ru.lab1.security.JwtTools
import ru.lab1.security.jwtFilter
import ru.lab1.web.templates.ContextAwarePebbleTemplates
import ru.lab1.web.templates.ContextAwareViewRender

const val ITEMS_ON_PAGE = 10

fun main() {
    // Конфигурация приложения
    val env = AppConfig().readConfiguration()
    val webConfig = WebConfig.createFromEnvironment(env)
    val dbConfig = DatabaseConfig.createFromEnvironment(env)
    val securityConfig = SecurityConfig.createFromEnvironment(env)
    val infoConfig = InfoConfig.createFromEnvironment(env)

    // база данных
    val db = MyAnimalsDB(dbConfig.dbUrl, securityConfig.salt)

    // JWT
    val jwtTools = JwtTools(securityConfig.jwtKey, infoConfig.organizationName)

    val contexts = RequestContexts()
    val userLens = RequestContextKey.optional<User>(contexts, name = "user")
    val permissionLens = RequestContextKey.optional<Permissions>(contexts, name = "permission")

    val templates = ContextAwarePebbleTemplates().HotReload("src/main/resources")
    val htmlView = ContextAwareViewRender.withContentType(templates, ContentType.TEXT_HTML)
    val htmlViewWithContext =
        htmlView
            .associateContextLens("user", userLens)
            .associateContextLens("permission", permissionLens)
    val appRouters = Router().router(db, jwtTools, userLens, htmlViewWithContext)

    val printingApp =
        ServerFilters
            .InitialiseRequestContext(contexts)
            .then(jwtFilter(userLens, permissionLens, jwtTools, db))
            .then(appRouters)

    val server = printingApp.asServer(Netty(webConfig.webPort)).start()
    println("Server started on http://localhost:" + server.port())
}
