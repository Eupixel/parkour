package net.eupixel

import net.eupixel.core.DirectusClient
import net.eupixel.command.CommandManager
import net.eupixel.core.DBTranslator
import net.eupixel.core.Messenger
import net.eupixel.event.EventManager
import net.eupixel.save.Config
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.MojangAuth
import net.minestom.server.instance.anvil.AnvilLoader
import net.minestom.server.network.packet.server.common.TransferPacket

fun main() {
    DirectusClient.initFromEnv()
    Config.translator = DBTranslator(arrayOf("whereami", "flight_true", "flight_false", "prefix"))
    val server = MinecraftServer.init()

    Messenger.bind("0.0.0.0", 2905)
    Messenger.registerTarget("entrypoint", "entrypoint", 2905)
    Messenger.addListener("transfer") { msg ->
        val name = msg.split("?")[0]
        val target = msg.split("?")[1]
        MinecraftServer.getConnectionManager().onlinePlayers.forEach {
            if(it.username == name) {
                it.sendPacket(TransferPacket(target.split("&")[0], target.split("&")[1].toInt()))
            }
        }
    }
    Messenger.addListener("message") { msg ->
        val name = msg.split("?")[0]
        val msg = msg.split("?")[1]
        MinecraftServer.getConnectionManager().onlinePlayers.forEach {
            if(it.username == name) {
                it.sendActionBar(MiniMessage.miniMessage().deserialize(msg))
            }
        }
    }

    Config.instance = MinecraftServer.getInstanceManager()
        .createInstanceContainer()
        .apply { chunkLoader = AnvilLoader("world"); timeRate = 0 }
    Config.init()
    EventManager.init()
    CommandManager.init()
    MojangAuth.init()
    server.start("0.0.0.0", 25565)
}