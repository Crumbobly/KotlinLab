package ru.lab1.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.*

class JwtTools(
    private val secret: String,
    private val organizationName: String,
    private val expirationTime: Long = 604800000, // 7 day in milliseconds
) {
    private val algorithm: Algorithm = Algorithm.HMAC512(secret)
    private val verifier =
        JWT.require(algorithm)
            .withIssuer(organizationName)
            .build()

    fun createJwtToken(userId: Int): String {
        return JWT.create()
            .withIssuer(organizationName)
            .withSubject(userId.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
            .sign(algorithm)
    }

    fun verifyJwtToken(token: String): String? {
        try {
            val decodedJWT: DecodedJWT = verifier.verify(token)
            return decodedJWT.subject
        } catch (e: JWTVerificationException) {
            return null
        }
    }
}
