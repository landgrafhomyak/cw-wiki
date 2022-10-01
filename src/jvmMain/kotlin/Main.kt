package io.github.landgrafhomyak.chatwars.wiki

import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

object Main {
    @JvmStatic
    fun main(vararg args: String) {
        val database = SQLiteJDBCDatabase("test.sqlite")
        val server = HttpServer.create(InetSocketAddress(8089), 0)
        server.createContext("/", HttpHandler(database))
        server.executor = null;
        server.start()
    }
}