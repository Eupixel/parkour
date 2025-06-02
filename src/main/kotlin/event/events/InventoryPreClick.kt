package net.eupixel.event.events

import net.eupixel.game.GameManager
import net.minestom.server.event.inventory.InventoryPreClickEvent

class InventoryPreClick(event: InventoryPreClickEvent) {
    init {
        event.isCancelled = true
        GameManager.handleItem(event.player, event.clickedItem)
    }
}