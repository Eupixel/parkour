package net.eupixel.event.events

import net.eupixel.game.GameManager
import net.minestom.server.event.player.PlayerUseItemEvent

class PlayerUseItem(event: PlayerUseItemEvent) {
    init {
        event.isCancelled = true
        GameManager.handleItem(event.player, event.itemStack)
    }
}