package com.jammes.schemas

import com.jammes.database.tables.Events
import com.jammes.database.tables.Users
import com.jammes.models.EventDetail
import com.jammes.models.EventRequest
import com.jammes.models.EventSummary
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers

class EventService() {

    suspend fun create(event: EventRequest): Int = dbQuery {
        Events.insert {
            it[title] = event.title
            it[description] = event.description
            it[date] = event.date
            it[time] = event.time
            it[location] = event.location
            it[createdBy] = event.createdBy
        }[Events.id]
    }

    suspend fun read(id: Int): EventDetail? {
        return dbQuery {
            (Events innerJoin Users)
                .selectAll()
                .where { Events.id eq id }
                .map {
                    EventDetail(
                        title = it[Events.title],
                        description = it[Events.description],
                        date = it[Events.date],
                        time = it[Events.time],
                        location = it[Events.location],
                        creatorName = it[Users.name]
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun readAll(
        event: String? = null,
        startAt: String? = null,
        endAt: String? = null
    ): List<EventSummary?> {
        return dbQuery {
            Events.selectAll().apply {
                if (!event.isNullOrEmpty()) {
                    andWhere {
                        ((Events.title like ("%$event%")) or (Events.description like ("%$event%")))
                    }
                }

                if (!startAt.isNullOrEmpty()) {
                    andWhere {
                        (Events.date greaterEq startAt.toString())
                    }
                }
                
                if (!endAt.isNullOrEmpty()) {
                    andWhere {
                        (Events.date lessEq endAt.toString())
                    }
                }
            }
                .orderBy(Events.date)
                .limit(10)
                .map {
                    EventSummary(
                        it[Events.id],
                        it[Events.title],
                        it[Events.date],
                        it[Events.location]
                    )
                }

        }
    }

    suspend fun update(id: Int, event: EventRequest) {
        dbQuery { //Corrigir quando não passar algum campo
            Events.update({ Events.id eq id }) {
                if (event.title.isNotEmpty()) it[title] = event.title
                if (event.description.isNotEmpty()) it[description] = event.description
                if (event.date.isNotEmpty()) it[date] = event.date
                if (event.time.isNotEmpty()) it[time] = event.time
                if (event.location.isNotEmpty()) it[location] = event.location
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