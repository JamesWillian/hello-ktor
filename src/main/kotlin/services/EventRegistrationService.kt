package com.jammes.services

import com.jammes.database.tables.EventRegistrations
import com.jammes.database.tables.Events
import com.jammes.database.tables.Users
import com.jammes.models.EventSummary
import com.jammes.models.UserSummary
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class EventRegistrationService {

    suspend fun registerUser(userId: Int, eventId: Int) = dbQuery {
        EventRegistrations.insertIgnore {
            it[this.userId] = userId
            it[this.eventId] = eventId
        }
    }

    suspend fun unregisterUser(userId: Int, eventId: Int) = dbQuery {
        EventRegistrations.deleteWhere { (this.userId eq userId) and (this.eventId eq eventId) }
    }

    suspend fun getUsersByEvent(eventId: Int): List<UserSummary> = dbQuery {
        (EventRegistrations innerJoin Users)
            .selectAll().where { EventRegistrations.eventId eq eventId }
            .map { UserSummary(it[Users.id], it[Users.name]) }
    }

    suspend fun getEventsByUser(userId: Int): List<EventSummary> = dbQuery {
        (EventRegistrations innerJoin Events)
            .selectAll().where { EventRegistrations.userId eq userId }
            .map {
                EventSummary(
                    it[Events.id],
                    it[Events.title],
                    it[Events.date],
                    it[Events.location]
                )
            }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
