package net.eupixel.core

import net.eupixel.vivlib.core.Messenger
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.MinecraftServer
import net.minestom.server.network.packet.server.common.TransferPacket

object MessageHandler {
    fun start() {
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
        Messenger.addListener("action") { msg ->
            val name = msg.split("?")[0]
            val msg = msg.split("?")[1]
            MinecraftServer.getConnectionManager().onlinePlayers.forEach {
                if(it.username == name) {
                    it.sendActionBar(MiniMessage.miniMessage().deserialize(msg))
                }
            }
        }
        Messenger.addListener("message") { msg ->
            val name = msg.split("?")[0]
            val msg = msg.split("?")[1]
            MinecraftServer.getConnectionManager().onlinePlayers.forEach {
                if(it.username == name) {
                    it.sendMessage(MiniMessage.miniMessage().deserialize(msg))
                }
            }
        }
    }
}