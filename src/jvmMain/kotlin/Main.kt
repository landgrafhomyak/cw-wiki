package io.github.landgrafhomyak.chatwars.wiki

import com.sun.net.httpserver.HttpServer
import io.github.landgrafhomyak.chatwars.wiki.http_handlers.MainPageHandler
import org.sqlite.JDBC
import org.sqlite.SQLiteConnection
import java.net.InetSocketAddress
import java.util.*

object Main {
    @JvmStatic
    fun main(vararg args: String) {
        val s = HttpServer.create(InetSocketAddress(8089), 0)
        s.createContext("/", MainPageHandler)
        s.executor = null;
        s.start()
//        SQLiteDatabase(JDBC.createConnection(JDBC.PREFIX + "test1.sqlite", Properties()) as SQLiteConnection)
    }
}