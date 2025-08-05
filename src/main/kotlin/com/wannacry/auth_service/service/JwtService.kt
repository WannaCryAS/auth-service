package com.wannacry.auth_service.service

import com.wannacry.auth_service.model.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import kotlin.Exception
import java.util.Base64
import java.time.Duration
import java.util.Date

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val jwtSecret: String
) {
    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))
    private val expiration = Duration.ofDays(30)

    fun generateToken(user: User): String {
        return Jwts.builder()
            .subject(user.userName)
            .claim("role", user.role)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expiration.toMillis()))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validationToken(token: String): Boolean {
        return try {
            println("Token validated successfully.")
            val jwt = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)

            // Optionally check expiration
            val expiration = jwt.payload.expiration
            expiration.after(Date())
        } catch (e: Exception) {
            println("Token validation failed: ${e.message}")
            false
        }
    }

    fun extractUserName(token: String): String {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun extractRole(token: String): String {
        val claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims["role"] as String
    }
}