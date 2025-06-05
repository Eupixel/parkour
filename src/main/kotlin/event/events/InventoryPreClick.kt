package net.eupixel.event.events

import net.minestom.server.event.inventory.InventoryPreClickEvent

class InventoryPreClick(event: InventoryPreClickEvent) {
    init {
        event.isCancelled = true
    }
}