package com.odogwudev.domain.repository

import com.odogwudev.domain.model.User

interface UserDataSource {
    suspend fun getUserInfo(userId: String): User?// query if user exists or return null
    suspend fun saveUserInfo(user: User): Boolean//save user info to database which is fetched from token id just verify if its save ot not
    suspend fun deleteUser(userId: String): Boolean
    suspend fun updateUserInfo(userId: String, firstName: String, lastName: String): Boolean
}