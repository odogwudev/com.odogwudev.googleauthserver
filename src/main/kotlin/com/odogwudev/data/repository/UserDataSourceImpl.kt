package com.odogwudev.data.repository

import com.odogwudev.domain.model.User
import com.odogwudev.domain.repository.UserDataSource
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue


class UserDataSourceImpl(
    dataBase: CoroutineDatabase//its a version of mongo databsw that supports coroutines
) : UserDataSource {

    private val users = dataBase.getCollection<User>()//collection is a dataholder for multiple fields

    override suspend fun getUserInfo(userId: String): User? {
        return users.findOne(filter = User::id eq userId)//search collection for user id
        //read for more info on queries(http://litote.org/kmongo/)
    }

    override suspend fun saveUserInfo(user: User): Boolean {
        val existingUser =
            users.findOne(filter = User::id eq user.id)//if i pass this user which i want to save i want to get existing user from database
        return if (existingUser == null) {
            users.insertOne(document = user).wasAcknowledged()// insert user
        } else {
            true// if it exists return true so as to prevent duplicate users
        }
    }

    override suspend fun deleteUser(userId: String): Boolean {
        return users.deleteOne(filter = User::id eq userId)
            .wasAcknowledged()//if user gets deleted was acknowledge should return true
    }

    override suspend fun updateUserInfo(
        userId: String,
        firstName: String,
        lastName: String
    ): Boolean {
        return users.updateOne(
            filter = User::id eq userId,
            update = setValue(
                property = User::name,
                value = "$firstName $lastName"
            )
        ).wasAcknowledged()// return true if users update is successfull
    }
}
