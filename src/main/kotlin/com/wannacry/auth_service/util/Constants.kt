package com.wannacry.auth_service.util

object ResponseCode {
    val SUCCESS = Pair("00000", "Success")
    val USERNAME_ALREADY_EXIST = Pair("SASR-10001", "Username already exist.")
    val USER_NOT_FOUND_LOGIN = Pair("SASL-10001", "Invalid credentials / username or password incorrect")
    val FIRST_TIME_LOGIN = Pair("SASL-10002", "Please set your password before login")
    val LOGIN_PASSWORD_INCORRECT = Pair("SASL-10003", "Invalid credentials / username or password incorrect")
    val REGISTER_BY_ADMIN_ALREADY_EXIST = Pair("SASRBA-10001", "Username already exist.")
    val USER_NOT_FOUND = Pair("SAS-10001", "User not found")
    val PASSWORD_NOT_SET = Pair("SASRP-10002", "Password not set yet. Please set your password first.")
    val PASSWORD_SAME = Pair("SASRP-10003", "New password cannot be same as current password.")
    val PASSWORD_ALREADY_SET_FIRST_SETUP = Pair("SASSP-10002", "Password already set. Please login with your password")
}