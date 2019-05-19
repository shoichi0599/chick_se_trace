package com.chick_se_trace.domain

import com.chick_se_trace.base.db.UserEntity
import java.io.Serializable

data class User(
        val email: String,
        val name: String,
        val password: String
): Serializable {
    companion object {
        fun fromEntity(entity: UserEntity): User {
            return User(
                    email = entity.email,
                    name = entity.name,
                    password = entity.password
            )
        }
    }
}