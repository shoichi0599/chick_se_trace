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
class ItemsCategoriesMapper(
        @Autowired private val dataSource: DataSource
) {
    val jstZoneId: ZoneId = TimeZone.getTimeZone("JST").toZoneId()

    fun selectByCategoryId(categoryId: Int): List<ItemCategoryEntity> {
        val entities: MutableList<ItemCategoryEntity> = mutableListOf()
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.query(
                """
                SELECT *
                FROM items_categories
                WHERE category_id = ?
                """.trimIndent(),
                arrayOf(categoryId),
                ResultSetExtractor { resultSet: ResultSet? ->
                    val rowMapper = ColumnMapRowMapper()
                    while (resultSet!!.next()) {
                        val rowMap: Map<String, Any> = rowMapper.mapRow(resultSet, 0)
                        entities.add(
                                ItemCategoryEntity().apply {
                                    this.itemId = (rowMap.get("item_id") as Long).toInt()
                                    this.categoryId = (rowMap.get("category_id") as Long).toInt()
                                    createTime = LocalDateTime.ofInstant((rowMap.get("create_time") as Date).toInstant(), jstZoneId)
                                    updateTime = LocalDateTime.ofInstant((rowMap.get("update_time") as Date).toInstant(), jstZoneId)
                                }
                        )
                    }
                }
        )
        return entities
    }

    fun selectByItemId(itemId: Int): List<ItemCategoryEntity> {
        val entities: MutableList<ItemCategoryEntity> = mutableListOf()
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.query(
                """
                SELECT *
                FROM items_categories
                WHERE item_id = ?
                """.trimIndent(),
                arrayOf(itemId),
                ResultSetExtractor { resultSet: ResultSet? ->
                    val rowMapper = ColumnMapRowMapper()
                    while (resultSet!!.next()) {
                        val rowMap: Map<String, Any> = rowMapper.mapRow(resultSet, 0)
                        entities.add(
                            ItemCategoryEntity().apply {
                                this.itemId = (rowMap.get("item_id") as Long).toInt()
                                this.categoryId = (rowMap.get("category_id") as Long).toInt()
                                createTime = LocalDateTime.ofInstant((rowMap.get("create_time") as Date).toInstant(), jstZoneId)
                                updateTime = LocalDateTime.ofInstant((rowMap.get("update_time") as Date).toInstant(), jstZoneId)
                            }
                        )
                    }
                }
        )
        return entities
    }

    fun insert(entity: ItemCategoryEntity) {
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.update(
                """
                INSERT INTO items_categories
                (
                item_id,
                category_id,
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
                entity.itemId,
                entity.categoryId,
                entity.createTime,
                entity.updateTime
        )
    }

    fun deleteByItemId(itemId: Int) {
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.update(
                """
                DELETE FROM items_categories
                WHERE item_id = ?
                """.trimIndent(),
                itemId
        )
    }
}