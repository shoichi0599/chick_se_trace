package com.chick_se_trace.domain

import com.chick_se_trace.base.db.CategoryEntity

data class Category(
        var id: Int?,
        val name: String
) {
    companion object {
        fun fromEntity(entity: CategoryEntity): Category {
            return Category(
                    id = entity.categoryId,
                    name = entity.categoryName!!
            )
        }
    }
}