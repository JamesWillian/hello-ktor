package com.jammes.schemas

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.coroutines.Dispatchers

@Serializable
data class ExposedEvent(val title: String, val description: String, val date: String, val time: String, val location: String)

class EventService(database: Database) {

    object Events : Table() {
        val id = integer("id").autoIncrement()
        val title = varchar("title", 100)
        val description = text("description")
        val date = varchar("date", 10)
        val time = varchar("date", 5)
        val location = varchar("location", 255)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Events)
        }
    }

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
                        it[Events.location])
                }
                .singleOrNull()
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