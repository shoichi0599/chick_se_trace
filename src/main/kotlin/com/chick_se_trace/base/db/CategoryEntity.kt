package com.chick_se_trace.base.db

import java.time.LocalDateTime

data class CategoryEntity(
        var categoryId: Int?,
        var categoryName: String?,
        var createTime: LocalDateTime?,
        var updateTime: LocalDateTime?
) {
    constructor(): this(null, null, null, null)
}