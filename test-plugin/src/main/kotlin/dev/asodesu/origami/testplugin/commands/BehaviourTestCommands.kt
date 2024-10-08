package dev.asodesu.origami.testplugin.commands

import cloud.commandframework.arguments.standard.StringArgument
import cloud.commandframework.kotlin.MutableCommandBuilder
import dev.asodesu.origami.engine.add
import dev.asodesu.origami.engine.get
import dev.asodesu.origami.engine.getOrAdd
import dev.asodesu.origami.engine.getOrNull
import dev.asodesu.origami.engine.impl.BehaviourContainer
import dev.asodesu.origami.engine.player.container
import dev.asodesu.origami.engine.removeAll
import dev.asodesu.origami.engine.replace
import dev.asodesu.origami.engine.scene.PlayerScene
import dev.asodesu.origami.engine.scene.Scenes
import dev.asodesu.origami.engine.scopes.global
import dev.asodesu.origami.engine.scopes.scope
import dev.asodesu.origami.testplugin.behaviours.DashBehaviour
import dev.asodesu.origami.testplugin.behaviours.DashRestoreBehaviour
import dev.asodesu.origami.testplugin.behaviours.InstantHealthBehaviour
import dev.asodesu.origami.testplugin.behaviours.TestBehaviour
import dev.asodesu.origami.testplugin.scenes.TestOfflineScene
import dev.asodesu.origami.testplugin.scenes.TestOnlineScene
import dev.asodesu.origami.testplugin.scenes.TestScopedScene
import dev.asodesu.origami.testplugin.tests.Test
import dev.asodesu.origami.utilities.error
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun MutableCommandBuilder<CommandSender>.applyBehaviourTests() {
    registerCopy("create_and_apply") {
        senderType(Player::class.java)
        handler {
            val container = BehaviourContainer()
            val test = Test(it.sender)

            // init
            test.run("BehaviourContainer#get initially") {
                container.get<TestBehaviour>() == null
            }

            // getOrAdd
            test.run("BehaviourContainer#getOrAdd") {
                container.getOrAdd<TestBehaviour>().test()
            }
            test.run("BehaviourContainer#get after getOrAdd") {
                container.getOrNull<TestBehaviour>()?.test()
            }
            test.run("BehaviourContainer#get has applied") {
                container.get<TestBehaviour>().applied == container
            }

            // remove
            test.run("BehaviourContainer#remove") {
                container.removeAll<TestBehaviour>().isEmpty()
            }
            test.run("BehaviourContainer#get after remove") {
                container.getOrNull<TestBehaviour>() == null
            }

            // add
            test.run("BehaviourContainer#add") {
                container.add<TestBehaviour>().test()
            }
            test.run("BehaviourContainer#get after add") {
                container.getOrNull<TestBehaviour>()?.test() == true
            }

            // list
            test.run("BehaviourContainer.behaviours list") {
                container.behaviours.size == 1
            }

            // duplicate / replace
            test.run("BehaviourContainer#add throws on duplicate") {
                try {
                    container.add<TestBehaviour>()
                    false
                } catch (e: Exception) {
                    true
                }
            }
            test.run("BehaviourContainer#replace") {
                val didAdd = container.replace<TestBehaviour>(
                    TestBehaviour(container, 2)
                ).test()
                didAdd && container.getOrNull<TestBehaviour>()?.number == 2
            }

            // list
            test.run("BehaviourContainer.behaviours still single") {
                container.behaviours.size == 1
            }
        }
    }
    registerCopy("player_tests") {
        senderType(Player::class.java)
        handler {
            val player = it.sender as Player
            val container = player.container
            val test = Test(player)

            test.run("PlayerBehaviourContainer is empty") {
                container.behaviours.isEmpty()
            }

            test.run("PlayerBehaviourContainer add behaviour") {
                container.replace(TestBehaviour::class).test()
            }
            test.run("PlayerBehaviourContainer access behaviour") {
                container.get(TestBehaviour::class)?.test() == true
            }
            test.run("new PlayerBehaviourContainer access behaviour") {
                player.container.get(TestBehaviour::class)?.test() == true
            }
            test.run("PlayerBehaviourContainer remove behaviour") {
                container.removeAll(TestBehaviour::class).isNotEmpty()
            }
        }
    }

    registerCopy("apply_test_behaviour") {
        senderType(Player::class.java)
        handler { ctx ->
            val player = ctx.sender as Player
            val container = player.container

            // extendable, can pass in any scope
            scope(global) {
                container.replace<DashBehaviour>()
                container.replace<DashRestoreBehaviour>()
                container.replace<InstantHealthBehaviour>()
            }

            player.inventory.setItem(7, InstantHealthBehaviour.getInstantHealth(amplifier = 2))
            player.inventory.setItem(8, ItemStack(Material.PHANTOM_MEMBRANE, 64))
        }
    }

    registerCopy("init_online_scene") {
        handler {
            Scenes.register(TestOnlineScene())
        }
    }
    registerCopy("init_offline_scene") {
        handler {
            Scenes.register(TestOfflineScene())
        }
    }
    registerCopy("destory_scene") {
        handler {
            Scenes.map.values.toList().forEach {
                Scenes.unregister(it)
            }
        }
    }

    registerCopy("init_world_scenes") {
        handler {
            Scenes.register(TestScopedScene("test1"))
            Scenes.register(TestScopedScene("two"))
        }
    }
    registerCopy("leave_scene") {
        senderType(Player::class.java)
        argument(StringArgument.of("id"))
        handler {
            val id = it.get<String>("id")
            val scene = Scenes.map[id] ?: return@handler it.sender.error("no scene.")
            val scopedScene = (scene as? PlayerScene<OfflinePlayer>) ?: return@handler it.sender.error("not player scene")
            scopedScene.removePlayer(it.sender as OfflinePlayer)
        }
    }
    registerCopy("join_scene") {
        senderType(Player::class.java)
        argument(StringArgument.of("id"))
        handler {
            val id = it.get<String>("id")
            val scene = Scenes.map[id] ?: return@handler it.sender.error("no scene.")
            val scopedScene = (scene as? PlayerScene<OfflinePlayer>) ?: return@handler it.sender.error("not player scene")
            scopedScene.addPlayer(it.sender as OfflinePlayer)
        }
    }
}