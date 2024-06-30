package ru.lab1.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.string

class SecurityConfig(
    val salt: String,
    val jwtKey: String,
) {
    companion object {
        val saltLens = EnvironmentKey.string().required("security.salt", "Password's master salt")
        val jwtKeyLens = EnvironmentKey.string().required("security.jwtKey", "Секретная строка для формирования алгоритма")

        fun createFromEnvironment(environment: Environment): SecurityConfig {
            val salt = saltLens(environment)
            val jwtKey = jwtKeyLens(environment)
            return SecurityConfig(salt, jwtKey)
        }
    }
}
