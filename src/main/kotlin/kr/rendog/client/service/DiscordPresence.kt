package kr.rendog.client.service

import meteordevelopment.discordipc.DiscordIPC
import meteordevelopment.discordipc.RichPresence
import java.time.Instant

object DiscordPresence {
    private const val CLIENT_ID = 1439884674720858196L
    private var presence: RichPresence? = null

    fun start() {
        val ok = DiscordIPC.start(CLIENT_ID) {
            println("Logged in as: ${DiscordIPC.getUser().username}")
        }

        if (!ok) {
            println("Failed to start Discord IPC")
            return
        }

        presence = RichPresence().apply {
            setDetails("RendogClient-1.20.4")
            setState("Playing Rendog.kr")
            setLargeImage("rendog_icon", "RendogClient")
            setStart(Instant.now().epochSecond)
        }

        DiscordIPC.setActivity(presence!!)
    }

    fun stop() {
        DiscordIPC.stop()
    }

    fun update(details: String? = null, state: String? = null) {
        presence?.apply {
            if (details != null) setDetails(details)
            if (state != null) setState(state)
        }
        presence?.let { DiscordIPC.setActivity(it) }
    }
}