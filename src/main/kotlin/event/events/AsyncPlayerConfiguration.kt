package net.eupixel.event.events

import net.eupixel.save.Config
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent

class AsyncPlayerConfiguration(event: AsyncPlayerConfigurationEvent) {
    init {
        if(MinecraftServer.getConnectionManager().onlinePlayers.isNotEmpty()) {
            event.player.kick("The server is already in use!")
        }
        event.spawningInstance = Config.instance
        event.player.respawnPoint = Config.spawnPosition
    }
}