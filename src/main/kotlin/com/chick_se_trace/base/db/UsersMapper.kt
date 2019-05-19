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
class UsersMapper(
        @Autowired private val dataSource: DataSource
) {
    private val jstZoneId: ZoneId = TimeZone.getTimeZone("JST").toZoneId()

    fun selectByEmail(email: String): UserEntity? {
        var entity: UserEntity? = null
        val jdbcTemplate = JdbcTemplate(dataSource)
        jdbcTemplate.query(
                """
                SELECT *
                FROM users
                WHERE email = ?
                """.trimIndent(),
                arrayOf(email),
                ResultSetExtractor { resultSet: ResultSet? ->
                    val rowMapper = ColumnMapRowMapper()
                    while (resultSet!!.next()) {
                        val rowMap: Map<String, Any> = rowMapper.mapRow(resultSet, 0)
                        entity = UserEntity(
                                email = rowMap.get("email") as String,
                                name = rowMap.get("name") as String,
                                password = rowMap.get("password") as String,
                                createTime = LocalDateTime.ofInstant((rowMap.get("create_time") as Date).toInstant(), jstZoneId),
                                updateTime = LocalDateTime.ofInstant((rowMap.get("update_time") as Date).toInstant(), jstZoneId)
                        )
                    }
                }
        )
        return entity
    }
}