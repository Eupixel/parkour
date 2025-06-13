package net.eupixel

import core.Vivlib
import net.eupixel.command.CommandManager
import net.eupixel.core.MessageHandler
import net.eupixel.event.EventManager
import net.eupixel.save.Config
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.MojangAuth

fun main() {
    val server = MinecraftServer.init()
    MessageHandler.start()
    Vivlib.init()

    Config.instance = MinecraftServer.getInstanceManager().createInstanceContainer()

    Config.init()
    EventManager.init()
    CommandManager.init()
    MojangAuth.init()
    server.start("0.0.0.0", 25565)
}