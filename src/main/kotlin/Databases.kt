package com.jammes

import com.jammes.database.DatabaseFactory
import com.jammes.models.Events
import com.jammes.models.Users
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
