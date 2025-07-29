package com.wannacry.auth_service.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID


@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(name = "id")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "f_name", nullable = false)
    val fullName: String,

    @Column(name = "user_name", unique = true)
    val userName: String,

    @Column(name = "userEmail", unique = true, nullable = false)
    val email: String,

    @Column(name = "userPassword", nullable = true)
    var userPassword: String?,

    @Column(name = "phone_no", length = 13)
    val phoneNumber: String,

    @Column(name = "role", nullable = false)
    val role: String,

    @Column(name = "first_time_login")
    var firstTimeLogin: Boolean = true,
): UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return listOf(SimpleGrantedAuthority("ROLE_$role"))
    }

    override fun getPassword(): String? {
        return userPassword
    }

    override fun getUsername(): String? {
        return userName
    }

//    fun getEmail(): String? {
//        return email
//    }

}
