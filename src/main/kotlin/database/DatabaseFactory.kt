package com.jammes.database

import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    private lateinit var database: Database

    fun init() {
        database = Database.connect(
            url = "jdbc:h2:file:./database;AUTO_SERVER=TRUE;MODE=MYSQL",
            user = "root",
            driver = "org.h2.Driver",
            password = "",
        )
    }

    fun getDatabase(): Database {
        if (!::database.isInitialized) {
            throw IllegalStateException("Database not initialized. Call DatabaseFactory.init() first.")
        }
        return database
    }

}