package net.eupixel.event.events

import net.eupixel.game.GameManager
import net.eupixel.util.PrefixLoader.loadPrefix
import net.minestom.server.event.player.PlayerSpawnEvent

class PlayerSpawn(event: PlayerSpawnEvent) {
    init {
        loadPrefix(event.player)
        GameManager.resetGame(event.player)
    }
}