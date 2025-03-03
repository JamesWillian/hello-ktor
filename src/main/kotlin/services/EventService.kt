package com.jammes.services

import com.jammes.database.tables.Events
import com.jammes.database.tables.Users
import com.jammes.models.EventDetail
import com.jammes.models.EventRequest
import com.jammes.models.EventSummary
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers

class EventService {

    suspend fun create(event: EventRequest): Int = dbQuery {
        Events.insert { insert ->
            insert[title] = event.title!!
            insert[description] = event.description!!
            insert[date] = event.date!!
            insert[time] = event.time!!
            insert[location] = event.location!!
            insert[createdBy] = event.createdBy!!
        }[Events.id]
    }

    suspend fun read(id: Int): EventDetail? {
        return dbQuery {
            (Events innerJoin Users)
                .selectAll()
                .where { Events.id eq id }
                .map { get ->
                    EventDetail(
                        title = get[Events.title],
                        description = get[Events.description],
                        date = get[Events.date],
                        time = get[Events.time],
                        location = get[Events.location],
                        creatorName = get[Users.name]
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
                .map { get ->
                    EventSummary(
                        get[Events.id],
                        get[Events.title],
                        get[Events.date],
                        get[Events.location]
                    )
                }

        }
    }

    suspend fun update(id: Int, event: EventRequest) {
        dbQuery {
            Events.update({ Events.id eq id }) { update ->
                update[title] = event.title!!
                update[description] = event.description!!
                update[date] = event.date!!
                update[time] = event.time!!
                update[location] = event.location!!
            }
        }
    }

    suspend fun updatePartial(id: Int, event: EventRequest) {
        dbQuery {
            Events.update({ Events.id eq id }) { update ->
                event.title?.let { update[title] = it }
                event.description?.let { update[description] = it }
                event.date?.let { update[date] = it }
                event.time?.let { update[time] = it }
                event.location?.let { update[location] = it }
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