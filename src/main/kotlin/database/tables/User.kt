package com.jammes.database.tables

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val bornAt = varchar("bornAt", 10)

    override val primaryKey = PrimaryKey(id)
}