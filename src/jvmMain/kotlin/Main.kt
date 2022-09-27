package io.github.landgrafhomyak.chatwars.wiki

import org.sqlite.JDBC
import org.sqlite.SQLiteConnection
import java.util.*

object Main {
    @JvmStatic
    fun main(vararg args: String) {
        SQLiteDatabase(JDBC.createConnection(JDBC.PREFIX + "test1.sqlite", Properties()) as SQLiteConnection)
    }
}