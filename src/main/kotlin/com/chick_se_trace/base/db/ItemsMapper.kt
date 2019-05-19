package com.chick_se_trace.base.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.ColumnMapRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.TimeZone
import javax.sql.DataSource

@Repository
class ItemsMapper(
        @Autowired private var dataSource: DataSource
) {
    private val jstZoneId: ZoneId = TimeZone.getTimeZone("JST").toZoneId()

    fun selectByItemId(id: Int): ItemEntity? {
        var entity: ItemEntity? = null
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.query(
                """
                SELECT *
                FROM items
                WHERE item_id = ?
                """.trimIndent(),
                arrayOf(id),
                ResultSetExtractor { resultSet: ResultSet? ->
                    val rowMapper = ColumnMapRowMapper()
                    while (resultSet!!.next()) {
                        val rowMap: Map<String, Any> = rowMapper.mapRow(resultSet, 0)
                        entity = ItemEntity(
                                itemId =  (rowMap.get("item_id") as Long).toInt(),
                                title = rowMap.get("title") as String,
                                content = rowMap.get("content") as String,
                                createTime = LocalDateTime.ofInstant((rowMap.get("create_time") as Date).toInstant(), jstZoneId),
                                updateTime = LocalDateTime.ofInstant((rowMap.get("update_time") as Date).toInstant(), jstZoneId)
                        )
                    }
                }
        )
        return entity
    }

    fun selectByItemIds(ids: List<Int>): List<ItemEntity> {
        if (ids.isEmpty()) {
            return emptyList()
        }
        val entities: MutableList<ItemEntity> = mutableListOf()
        val questionMarks = buildString {
            for (i in 1..ids.size) {
                if (i == 1) {
                    this.append("?")
                    continue
                }
                this.append(", ?")
            }
        }
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.query(
                """
                SELECT *
                FROM items
                WHERE item_id IN ($questionMarks)
                """.trimIndent(),
                ids.toTypedArray(),
                ResultSetExtractor { resultSet: ResultSet? ->
                    val rowMapper = ColumnMapRowMapper()
                    while (resultSet!!.next()) {
                        val rowMap: Map<String, Any> = rowMapper.mapRow(resultSet, 0)
                        entities.add(ItemEntity(
                                itemId = (rowMap.get("item_id") as Long).toInt(),
                                title = rowMap.get("title") as String,
                                content = rowMap.get("content") as String,
                                createTime = LocalDateTime.ofInstant((rowMap.get("create_time") as Date).toInstant(), jstZoneId),
                                updateTime = LocalDateTime.ofInstant((rowMap.get("update_time") as Date).toInstant(), jstZoneId)
                        ))
                    }
                }

        )
        return entities
    }

    fun insert(entity: ItemEntity): Int {
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.update(
                """
                    INSERT INTO items
                    (
                    title,
                    content,
                    create_time,
                    update_time
                    )
                    VALUES (
                    ?,
                    ?,
                    ?,
                    ?
                    )
                    """.trimIndent(),
                entity.title,
                entity.content,
                entity.createTime,
                entity.updateTime
        )
        // item ID
        return jdbcTemplate.queryForObject(
                """
                SELECT LAST_INSERT_ID()
                """.trimIndent(),
                Integer::class.java
        ) as Int
    }

    fun update(entity: ItemEntity) {
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.update(
                """
                    UPDATE items
                    SET title = ?,
                        content = ?,
                        update_time  = ?
                    WHERE item_id = ?
                    """.trimIndent(),
                entity.title,
                entity.content,
                entity.updateTime,
                entity.itemId
        )
    }

    fun delete(itemId: Int) {
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.update(
                """
                DELETE FROM items
                WHERE item_id = ?
                """.trimIndent(),
                arrayOf(itemId)
        )
    }
}