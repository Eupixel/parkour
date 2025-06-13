package net.eupixel.save

import net.eupixel.vivlib.core.DirectusClient.getData
import net.eupixel.vivlib.util.Helper.convertToPos
import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.Instance

object Config {
    lateinit var instance: Instance

    var spawnPosition: Pos = Pos(0.0, 0.0, 0.0)

    fun init() {
        instance.time = getData("lobby_values", "name", "time", "data")?.toLong() ?: 1000
        spawnPosition = convertToPos(getData("lobby_values", "name", "spawn_position", "data"))
    }
}