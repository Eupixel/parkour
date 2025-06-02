package net.eupixel.event.events

import net.eupixel.core.Messenger
import net.minestom.server.event.player.PlayerDisconnectEvent
import java.nio.file.Files
import java.nio.file.Paths

class PlayerDisconnect(event: PlayerDisconnectEvent) {
    init {
        event.player.passengers.forEach { it.remove() }
        Messenger.broadcast("rmme", getContainerId().toString())
    }

    fun getContainerId(): String? {
        val lines = Files.readAllLines(Paths.get("/proc/self/cgroup"))
        for (line in lines) {
            val parts = line.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (parts.size > 2) {
                val id = parts[parts.size - 1]
                if (id.matches("[0-9a-f]{64}".toRegex())) {
                    return id
                }
            }
        }
        return null
    }
}