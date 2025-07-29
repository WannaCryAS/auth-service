package com.wannacry.auth_service.repository

import com.wannacry.auth_service.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository: JpaRepository< User, UUID> {
    fun findByEmail(email: String): User?

    fun findByUserName(userName: String): User?
}