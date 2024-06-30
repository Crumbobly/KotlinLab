package ru.lab1.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.string

class DatabaseConfig(
    val dbUrl: String,
) {
    companion object {
        val dbUrlLens = EnvironmentKey.string().required("db.url", "Database url")

        fun createFromEnvironment(environment: Environment): DatabaseConfig {
            val dbUrl = dbUrlLens(environment)
            return DatabaseConfig(dbUrl)
        }
    }
}
