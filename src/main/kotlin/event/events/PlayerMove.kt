package net.eupixel.event.events

import net.eupixel.game.GameManager
import net.minestom.server.event.player.PlayerMoveEvent

class PlayerMove(event: PlayerMoveEvent) {
    init {
        GameManager.handlePlayerMove(event.player)
    }
}