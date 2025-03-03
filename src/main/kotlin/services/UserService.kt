package com.jammes.services

import com.jammes.database.tables.Users
import com.jammes.hashPassword
import com.jammes.models.UserDetail
import com.jammes.models.UserRequest
import com.jammes.models.UserSummary
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
            it[password] = hashPassword(user.password!!)
        }[Users.id]
    }

    suspend fun read(id: Int): UserSummary? {
        return dbQuery {
            Users.selectAll()
                .where { Users.id eq id }
                .map {
                    UserSummary(
                        it[Users.id],
                        it[Users.name]
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun findUserByEmail(email: String): UserDetail? {
        return dbQuery {
            Users.selectAll()
                .where { Users.email eq email }
                .map {
                    UserDetail(
                        it[Users.id],
                        it[Users.name],
                        it[Users.email],
                        it[Users.bornAt],
                        it[Users.password]
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, user: UserRequest) {
        dbQuery {
            Users.update({ Users.id eq id }) { update ->
                update[name] = user.name
                update[email] = user.email
                update[bornAt] = user.bornAt
                if (user.password != null)
                    update[password] = hashPassword(user.password)

            }
        }
    }

    suspend fun changePassword(id: Int, newPassword: String) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[password] = hashPassword(newPassword)
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

