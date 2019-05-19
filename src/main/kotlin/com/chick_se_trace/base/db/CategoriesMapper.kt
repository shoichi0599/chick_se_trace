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
class CategoriesMapper(
        @Autowired private val dataSource: DataSource
) {
    val jstZoneId: ZoneId = TimeZone.getTimeZone("JST").toZoneId()

    fun selectByCategoryId(id: Int): CategoryEntity? {
        var entity: CategoryEntity? = null
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.query(
                """
                SELECT *
                FROM categories
                WHERE category_id = ?
                """.trimIndent(),
                arrayOf(id),
                ResultSetExtractor { resultSet: ResultSet? ->
                    val rowMapper = ColumnMapRowMapper()
                    while (resultSet!!.next()) {
                        val rowMap: Map<String, Any> = rowMapper.mapRow(resultSet, 0)
                        entity = CategoryEntity(
                                categoryId = (rowMap.get("category_id") as Long).toInt(),
                                categoryName = rowMap.get("category_name") as String,
                                createTime = LocalDateTime.ofInstant((rowMap.get("create_time") as Date).toInstant(), jstZoneId),
                                updateTime = LocalDateTime.ofInstant((rowMap.get("update_time") as Date).toInstant(), jstZoneId)
                        )
                    }
                }
        )
        return entity
    }
    fun selectByCategoryIds(ids: List<Int>): List<CategoryEntity> {
        if (ids.isEmpty()) {
            return emptyList()
        }
        val questionMarks = buildString {
            for (i in 1..ids.size) {
                if (i == 1) {
                    this.append("?")
                    continue
                }
                this.append(", ?")
            }
        }

        val entities: MutableList<CategoryEntity> = mutableListOf()
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.query(
                """
                SELECT *
                FROM categories
                WHERE category_id = $questionMarks
                """.trimIndent(),
                ids.toTypedArray(),
                ResultSetExtractor { resultSet: ResultSet? ->
                    val rowMapper = ColumnMapRowMapper()
                    while (resultSet!!.next()) {
                        val rowMap: Map<String, Any> = rowMapper.mapRow(resultSet, 0)
                        entities.add(
                            CategoryEntity(
                                categoryId = (rowMap.get("category_id") as Long).toInt(),
                                categoryName = rowMap.get("category_name") as String,
                                createTime = LocalDateTime.ofInstant((rowMap.get("create_time") as Date).toInstant(), jstZoneId),
                                updateTime = LocalDateTime.ofInstant((rowMap.get("update_time") as Date).toInstant(), jstZoneId)
                            )
                        )
                    }
                }
        )
        return entities
    }

    fun selectByCategoryName(categoryName: String): CategoryEntity? {
        var entity: CategoryEntity? = null
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.query(
                """
                SELECT *
                FROM categories
                WHERE category_name = ?
                """.trimIndent(),
                arrayOf(categoryName),
                ResultSetExtractor { resultSet: ResultSet? ->
                    val rowMapper = ColumnMapRowMapper()
                    while (resultSet!!.next()) {
                        val rowMap: Map<String, Any> = rowMapper.mapRow(resultSet, 0)
                        entity = CategoryEntity(
                                categoryId = (rowMap.get("category_id") as Long).toInt(),
                                categoryName = rowMap.get("category_name") as String,
                                createTime = LocalDateTime.ofInstant((rowMap.get("create_time") as Date).toInstant(), jstZoneId),
                                updateTime = LocalDateTime.ofInstant((rowMap.get("update_time") as Date).toInstant(), jstZoneId)
                        )
                    }
                }
        )
        return entity
    }

    fun insert(entity: CategoryEntity): Int {
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.update(
                """
                INSERT INTO items
                (
                category_name,
                create_time,
                update_time
                )
                VALUES (
                ?,
                ?,
                ?
                )
                """.trimIndent(),
                entity.categoryName,
                entity.createTime,
                entity.updateTime
        )
        // category ID
        return jdbcTemplate.queryForObject(
                """"
                SELECT LAST_INSERT_ID()
                """.trimIndent(),
                Integer::class.java
        ) as Int
    }
}