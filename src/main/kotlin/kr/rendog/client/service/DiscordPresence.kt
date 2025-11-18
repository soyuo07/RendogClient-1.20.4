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
            setDetails("Playing Rendog.kr")
            setState("RendogClient-Epsilon")
            setLargeImage("rendog_icon", "RendogClient")
            setStart(Instant.now().epochSecond)
            try {
                val method = javaClass.getMethod("addButton", String::class.java, String::class.java)
                method.invoke(this, "Discord", "https://discord.gg/aKhYsfm")
            } catch (ignored: Throwable) {
                // Method not present or invocation failed — silently ignore to remain compatible
            }
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