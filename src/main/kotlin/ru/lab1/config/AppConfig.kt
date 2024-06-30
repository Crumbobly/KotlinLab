package ru.lab1.config

import org.http4k.cloudnative.env.Environment
import ru.lab1.config.WebConfig.Companion.defaultEnvironment

class AppConfig() {
    private val appEnv =
        Environment.fromResource("/ru/lab1/config/app.properties") overrides
            Environment.JVM_PROPERTIES overrides
            Environment.ENV overrides
            defaultEnvironment

    fun readConfiguration(): Environment {
        return appEnv
    }
}
