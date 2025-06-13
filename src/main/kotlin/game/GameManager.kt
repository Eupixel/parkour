package net.eupixel.game

import net.eupixel.vivlib.core.DBTranslator
import net.eupixel.vivlib.core.Messenger
import net.eupixel.save.Config.instance
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.Player
import net.minestom.server.entity.metadata.display.BlockDisplayMeta
import net.minestom.server.instance.block.Block
import net.minestom.server.network.packet.server.play.ParticlePacket
import net.minestom.server.particle.Particle
import net.minestom.server.sound.SoundEvent
import net.minestom.server.timer.TaskSchedule
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.util.Collections
import java.util.concurrent.ThreadLocalRandom

object  GameManager {
    private val random = ThreadLocalRandom.current()
    private val placed: MutableList<Pos> = Collections.synchronizedList(ArrayList())
    private val spawnedFrom: MutableList<Pos> = Collections.synchronizedList(ArrayList())
    private val startBlock = Pos(0.0, 0.0, 0.0)
    private val startPos = Pos(0.5, 1.0, 0.5)
    private lateinit var leaveItem: ItemStack

    fun handlePlayerMove(player: Player) {
        val pos = player.position
        if (pos.y < -20) {
            resetGame(player)
            return
        }
        if (spawnedFrom.isNotEmpty()) {
            player.sendActionBar(miniMessage().deserialize(DBTranslator.get("score_info", player.locale).replace("<score>", spawnedFrom.size.toString())))
        }
        val beneath = pos.withX(pos.blockY() - 1.0).withX(pos.blockX().toDouble()).withZ(pos.blockZ().toDouble()).withYaw(0f).withPitch(0f)
        if (beneath.blockZ() == placed[placed.size - 3].blockZ() && !spawnedFrom.contains(beneath)) {
            spawnedFrom.add(beneath)
            spawnNewBlock(player)
        }
    }

    fun resetGame(player: Player) {
        player.inventory.clear()
        spawnItems(player)
        instance.entities.forEach { entity ->
            if (entity.entityType == EntityType.BLOCK_DISPLAY) {
                entity.remove()
            }
        }
        placed.forEach { pos -> instance.setBlock(pos, Block.AIR) }
        placed.clear()
        spawnedFrom.clear()
        instance.setBlock(startBlock, Block.GOLD_BLOCK)
        placed.add(startBlock)
        player.teleport(startPos)
        repeat(3) { spawnNewBlock(player) }
    }

    private fun spawnNewBlock(player: Player) {
        val effect = placed.size >= 2
        val block = getABlock()
        val basePos = placed.last()
        var np: Pos
        do {
            val offX = random.nextInt(-1, 2)
            val offY = random.nextInt(-1, 2)
            val offZ = random.nextInt(3, 5)
            val zOffset = if (offZ >= 4 && offY == 1) offZ - 1 else offZ
            np = Pos(basePos.x + offX, basePos.y + offY, basePos.z + zOffset, 0f, 0f)
        } while (instance.isInVoid(np) || np.y < -10 || placed.contains(np))
        placed.add(np)
        if (!effect) {
            instance.setBlock(np, block)
            placed.add(np)
            return
        }
        player.playSound(Sound.sound(SoundEvent.BLOCK_AMETHYST_BLOCK_HIT, Sound.Source.MASTER, 999f, 1f))
        val e = Entity(EntityType.BLOCK_DISPLAY)
        e.setNoGravity(true)
        e.editEntityMeta(BlockDisplayMeta::class.java) { m ->
            m.setBlockState(block)
            m.posRotInterpolationDuration = 5
            m.isHasGlowingEffect = true
        }
        e.setInstance(instance, basePos).thenRun {
            MinecraftServer.getSchedulerManager().scheduleTask({
                if (e.isRemoved) return@scheduleTask TaskSchedule.stop()
                e.teleport(np)
                TaskSchedule.tick(1)
            }, TaskSchedule.tick(1))
            MinecraftServer.getSchedulerManager().scheduleTask({
                if (e.isRemoved) return@scheduleTask TaskSchedule.stop()
                instance.setBlock(np, block)
                player.sendPacket(ParticlePacket(Particle.POOF, np.add(Pos(0.5, 0.5, 0.5)), Pos(0.5, 0.5, 0.5), 0.1f, 20))
                e.remove()
                TaskSchedule.stop()
            }, TaskSchedule.tick(10))
        }
    }

    private fun getABlock(): Block {
        return when (random.nextInt(4)) {
            1 -> Block.GRASS_BLOCK
            2 -> Block.STONE_BRICKS
            3 -> Block.RED_MUSHROOM_BLOCK
            else -> Block.AZALEA_LEAVES
        }
    }

    fun spawnItems(player: Player) {
        leaveItem = ItemStack.builder(Material.BARRIER)
            .customName(miniMessage().deserialize(DBTranslator.get("leave_item_name", player.locale)).decoration(TextDecoration.ITALIC, false))
            .hideExtraTooltip()
            .build()
        player.inventory.setItemStack(0, leaveItem)
    }

    fun handleItem(player: Player, item: ItemStack) {
        if(item == leaveItem) {
            Messenger.send("entrypoint", "lobby", "${player.uuid}&${player.playerConnection.remoteAddress}")
        }
    }
}