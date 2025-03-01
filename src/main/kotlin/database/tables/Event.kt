package com.jammes.database.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Events : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 100)
    val description = text("description")
    val date = varchar("date", 10)
    val time = varchar("time", 5)
    val location = varchar("location", 255)
    val createdBy = reference(
        "createdBy",
        Users.id,
        onUpdate = ReferenceOption.CASCADE,
        onDelete = ReferenceOption.CASCADE
    )

    override val primaryKey = PrimaryKey(id)
}