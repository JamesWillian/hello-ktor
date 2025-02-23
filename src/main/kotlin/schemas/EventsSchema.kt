package com.jammes.schemas

import com.jammes.models.Events
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers

@Serializable
data class ExposedEvent(
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val location: String
)

class EventService(database: Database) {

    suspend fun create(event: ExposedEvent): Int = dbQuery {
        Events.insert {
            it[title] = event.title
            it[description] = event.description
            it[date] = event.date
            it[time] = event.time
            it[location] = event.location
        }[Events.id]
    }

    suspend fun read(id: Int): ExposedEvent? {
        return dbQuery {
            Events.selectAll()
                .where { Events.id eq id }
                .map {
                    ExposedEvent(
                        it[Events.title],
                        it[Events.description],
                        it[Events.date],
                        it[Events.time],
                        it[Events.location]
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun readAll(
        event: String? = null,
        startAt: String? = null,
        endAt: String? = null
    ): List<ExposedEvent?> {
        return dbQuery {
            Events.selectAll()
                .where {
                    ((Events.title like ("%$event%")) or (Events.description like ("%$event%")))
                    ((Events.date greaterEq startAt.toString()) and
                            (Events.date lessEq endAt.toString()))
                }
                .orderBy(Events.date)
                .limit(10)
                .map {
                    ExposedEvent(
                        it[Events.title],
                        it[Events.description],
                        it[Events.date],
                        it[Events.time],
                        it[Events.location]
                    )
                }
        }
    }

    suspend fun update(id: Int, event: ExposedEvent) {
        dbQuery {
            Events.update({ Events.id eq id }) {
                it[title] = event.title
                it[description] = event.description
                it[date] = event.date
                it[time] = event.time
                it[location] = event.location
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Events.deleteWhere { Events.id.eq(id) }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

}