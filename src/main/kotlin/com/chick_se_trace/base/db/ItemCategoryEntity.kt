package com.chick_se_trace.base.db

import java.time.LocalDateTime

data class ItemCategoryEntity(
        var itemId: Int?,
        var categoryId: Int?,
        var createTime: LocalDateTime?,
        var updateTime: LocalDateTime?
) {
    constructor(): this(null, null, null, null)
}