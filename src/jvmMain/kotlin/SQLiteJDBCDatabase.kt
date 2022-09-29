package io.github.landgrafhomyak.chatwars.wiki

import org.sqlite.SQLiteConnection

class SQLiteJDBCDatabase(
    private val _connection: SQLiteConnection
) : Database {
    init {
        use(this._connection.createStatement()) { stmt ->
            for (raw in SQLiteRequests.createTables)
                stmt.addBatch(raw)
            stmt.executeBatch()
        }
    }
}