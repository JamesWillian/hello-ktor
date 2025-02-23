package com.jammes.models

import org.jetbrains.exposed.sql.Table

object Events : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 100)
    val description = text("description")
    val date = varchar("date", 10)
    val time = varchar("time", 5)
    val location = varchar("location", 255)

    override val primaryKey = PrimaryKey(id)
}