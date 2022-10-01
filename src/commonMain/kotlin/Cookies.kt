package io.github.landgrafhomyak.chatwars.wiki

class Cookies {
    val sessionId: String?

    constructor() {
        this.sessionId = null
    }

    constructor(raw: String) {
        var sessionId: String? = null
        for (entry in raw.split("; ")) {
            val sepIndex = entry.indexOf('=')
            if (sepIndex < 0) continue
            when (entry.slice(0 until sepIndex).trim()) {
                "session" -> sessionId = entry.slice((sepIndex + 1) until entry.length)
            }
        }
        this.sessionId = sessionId
    }
}

inline fun Cookies(raw: String?) =
    if (raw == null) Cookies()
    else Cookies(raw)