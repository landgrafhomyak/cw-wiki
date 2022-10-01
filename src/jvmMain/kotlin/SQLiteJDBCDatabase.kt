package io.github.landgrafhomyak.chatwars.wiki

import org.sqlite.JDBC
import org.sqlite.SQLiteConnection
import java.time.Instant

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

    constructor(path: String) : this(SQLiteConnection(JDBC.PREFIX + path, path))

    override fun getUserBySession(session: String): User {
        use(this._connection.prepareStatement(SQLiteRequests.getUserBySession)) { stmt ->
            stmt.setString(1, session)
            val result = stmt.executeQuery()
            if (!result.next()) return User.Anonymous
            val until = Instant.ofEpochSecond(result.getLong(1))
            if (until < Instant.now()) {
                return User.SessionExpired(
                    result.getLong(2).toUserId(),
                    result.getString(3)
                )
            } else {
                return User.Authorized(
                    result.getLong(2).toUserId(),
                    result.getString(3),
                    result.getBoolean(4),
                    result.getBoolean(5),
                )
            }
        }
    }
}