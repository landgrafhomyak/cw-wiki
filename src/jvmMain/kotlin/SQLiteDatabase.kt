package io.github.landgrafhomyak.chatwars.wiki

import org.sqlite.SQLiteConnection

class SQLiteDatabase(
    private val _connection: SQLiteConnection
) : Database {
    init {
        use(this._connection.createStatement()) { stmt ->
            SQLiteDatabase::class.java.getResourceAsStream("/create_tables.sql")!!
                .readAllBytes()
                .decodeToString()
                .split(";")
                .mapNotNull { raw -> if (raw.isBlank()) null else raw.strip() }
                .forEach { raw ->
                    stmt.addBatch(raw)
                }
            stmt.executeBatch()
        }
    }
}