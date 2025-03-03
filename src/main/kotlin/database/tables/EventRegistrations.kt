package com.jammes.database.tables

import org.jetbrains.exposed.sql.Table

object EventRegistrations : Table() {
    val userId = reference("userId", Users.id)
    val eventId = reference("eventId", Events.id)
    override val primaryKey = PrimaryKey(userId, eventId)
}