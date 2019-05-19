package com.chick_se_trace.domain

import com.chick_se_trace.base.db.ItemEntity
import java.io.Serializable
import java.time.LocalDateTime

data class Item(
        val id: Int?,
        var title: String,
        var content: String,
        var createTime: LocalDateTime?,
        var updateTime: LocalDateTime?
): Serializable {
    companion object {
        fun fromEntity(entity: ItemEntity): Item {
            return Item(
                    id = entity.itemId,
                    title = entity.title!!,
                    content = entity.content!!,
                    createTime = entity.createTime,
                    updateTime = entity.updateTime
            )
        }
    }
}