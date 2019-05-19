package com.chick_se_trace.base.db

import java.time.LocalDateTime

data class ItemEntity(
        var itemId: Int?,
        var title: String?,
        var content: String?,
        var createTime: LocalDateTime?,
        var updateTime: LocalDateTime?
) {
    constructor(): this(null, null, null, null, null)
}