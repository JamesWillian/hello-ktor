package com.jammes.schemas

import com.jammes.database.tables.Users
import com.jammes.models.UserRequest
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserService {

    suspend fun create(user: UserRequest): Int = dbQuery {
        Users.insert {
            it[name] = user.name
            it[email] = user.email
            it[bornAt] = user.bornAt
        }[Users.id]
    }

    suspend fun read(id: Int): UserRequest? {
        return dbQuery {
            Users.selectAll()
                .where { Users.id eq id }
                .map {
                    UserRequest(
                        it[Users.name],
                        it[Users.email],
                        it[Users.bornAt]
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, user: UserRequest) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[email] = user.email
                it[bornAt] = user.bornAt
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

