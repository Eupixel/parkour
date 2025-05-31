package net.eupixel.event.events

import net.eupixel.gm
import net.minestom.server.event.player.PlayerMoveEvent

class PlayerMove(event: PlayerMoveEvent) {
    init {
        gm.handlePlayerMove(event.player)
    }
}