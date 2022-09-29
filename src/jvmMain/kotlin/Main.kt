package io.github.landgrafhomyak.chatwars.wiki

import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

object Main {
    @JvmStatic
    fun main(vararg args: String) {
        val s = HttpServer.create(InetSocketAddress(8089), 0)
        s.createContext("/", HttpHandler)
        s.executor = null;
        s.start()
//        SQLiteDatabase(JDBC.createConnection(JDBC.PREFIX + "test1.sqlite", Properties()) as SQLiteConnection)
    }
}