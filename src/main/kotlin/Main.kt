package net.eupixel

import net.eupixel.core.DirectusClient
import net.eupixel.command.CommandManager
import net.eupixel.core.DBTranslator
import net.eupixel.core.MessageHandler
import net.eupixel.event.EventManager
import net.eupixel.save.Config
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.MojangAuth

fun main() {
    DirectusClient.initFromEnv()
    DBTranslator.loadFromDB()
    MessageHandler.start()

    val server = MinecraftServer.init()
    Config.instance = MinecraftServer.getInstanceManager().createInstanceContainer()
    Config.init()
    EventManager.init()
    CommandManager.init()
    MojangAuth.init()
    server.start("0.0.0.0", 25565)
}