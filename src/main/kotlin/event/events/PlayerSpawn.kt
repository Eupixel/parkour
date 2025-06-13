package net.eupixel.event.events

import net.eupixel.game.GameManager
import net.minestom.server.event.player.PlayerSpawnEvent

class PlayerSpawn(event: PlayerSpawnEvent) {
    init {
        GameManager.resetGame(event.player)
    }
}