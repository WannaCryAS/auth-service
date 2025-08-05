package com.wannacry.auth_service.util.config

import com.wannacry.auth_service.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        println("Authorization header: $authHeader")

        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            println("Invalid or missing token. Skipping...")
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(7)
        println("Extracted token: $token")
        try {
            val username = jwtService.extractUserName(token)
            println("Extracted username: $username")

            if (username.isNotEmpty() && SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userDetailsService.loadUserByUsername(username)
                println("Loaded user: $userDetails")
                println("Authorities: ${userDetails.authorities}")

                if (jwtService.validationToken(token)) {
                    println("Token validated successfully")
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                    println("Authentication set in context.")
                } else {
                    println("Token validation failed.")
                }
            }
        } catch (e: Exception) {
            println("Exception in filter: ${e.message}")
            e.printStackTrace()
        }
        filterChain.doFilter(request, response)
    }

}
