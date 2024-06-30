package ru.lab1.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.string

class InfoConfig(
    val organizationName: String,
) {
    companion object {
        val organizationNameLens = EnvironmentKey.string().required("info.organizationName", "Название организации")

        fun createFromEnvironment(environment: Environment): InfoConfig {
            val organizationName = organizationNameLens(environment)
            return InfoConfig(organizationName)
        }
    }
}
