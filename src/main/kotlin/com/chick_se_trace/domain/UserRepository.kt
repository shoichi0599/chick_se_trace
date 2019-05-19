package com.chick_se_trace.domain

import com.chick_se_trace.base.db.UsersMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.slf4j.LoggerFactory

@Repository
class UserRepository(
        @Autowired private val usersMapper: UsersMapper
) {
    private val logger = LoggerFactory.getLogger(UserRepository::class.java)

    fun findByEmail(email: String): User? {
        val userEntity = usersMapper.selectByEmail(email)
        if (userEntity == null) {
            logger.info("user is not found by email=$email")
            return null
        }
        return User.fromEntity(userEntity)
    }
}