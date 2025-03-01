package com.jammes

import com.jammes.database.DatabaseFactory
import com.jammes.database.tables.Events
import com.jammes.database.tables.Users
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {

    DatabaseFactory.init()

    transaction(DatabaseFactory.getDatabase())
    {
        SchemaUtils.create(Users, Events)
    }

}
