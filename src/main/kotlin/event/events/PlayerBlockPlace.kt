package net.eupixel.event.events

import net.minestom.server.event.player.PlayerBlockPlaceEvent

class PlayerBlockPlace(event: PlayerBlockPlaceEvent) {
    init {
        event.isCancelled = true
    }
}