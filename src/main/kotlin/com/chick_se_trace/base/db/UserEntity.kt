package com.chick_se_trace.base.db

import java.time.LocalDateTime

data class UserEntity(
        var email: String,
        var name: String,
        var password: String,
        var createTime: LocalDateTime,
        var updateTime: LocalDateTime
)