package ru.lab1.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int

data class WebConfig(
    val webPort: Int,
) {
    companion object {
        val webPortLens = EnvironmentKey.int().required("web.port", "Application web port")

        fun createFromEnvironment(environment: Environment): WebConfig {
            val webPort = webPortLens(environment)
            return WebConfig(webPort)
        }

        val defaultEnvironment = Environment.defaults(webPortLens of 9000)
    }
}
