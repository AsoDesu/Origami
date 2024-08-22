package dev.asodesu.origami.testplugin.commands

import cloud.commandframework.kotlin.MutableCommandBuilder
import dev.asodesu.origami.engine.add
import dev.asodesu.origami.engine.get
import dev.asodesu.origami.engine.getOrAdd
import dev.asodesu.origami.engine.impl.BehaviourContainer
import dev.asodesu.origami.engine.player.container
import dev.asodesu.origami.engine.remove
import dev.asodesu.origami.engine.replace
import dev.asodesu.origami.engine.scene.Scene
import dev.asodesu.origami.engine.scopes.global
import dev.asodesu.origami.engine.scopes.scope
import dev.asodesu.origami.testplugin.behaviours.DashBehaviour
import dev.asodesu.origami.testplugin.behaviours.DashRestoreBehaviour
import dev.asodesu.origami.testplugin.behaviours.InstantHealthBehaviour
import dev.asodesu.origami.testplugin.behaviours.TestBehaviour
import dev.asodesu.origami.testplugin.scenes.TestOnlineScene
import dev.asodesu.origami.testplugin.tests.Test
import org.bukkit.Material
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
                container.get<TestBehaviour>()?.test() == true
            }
            test.run("BehaviourContainer#get has applied") {
                container.get<TestBehaviour>()?.applied == container
            }

            // remove
            test.run("BehaviourContainer#remove") {
                container.remove<TestBehaviour>()?.test() == true
            }
            test.run("BehaviourContainer#get after remove") {
                container.get<TestBehaviour>() == null
            }

            // add
            test.run("BehaviourContainer#add") {
                container.add<TestBehaviour>().test()
            }
            test.run("BehaviourContainer#get after add") {
                container.get<TestBehaviour>()?.test() == true
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
                didAdd && container.get<TestBehaviour>()?.number == 2
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
                container.remove(TestBehaviour::class) != null
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

    var scene: Scene? = null
    registerCopy("init_scene") {
        handler {
            scene?.destroy()
            val testScene = TestOnlineScene()
            testScene.init()
            scene = testScene
        }
    }
    registerCopy("destory_scene") {
        handler {
            scene?.destroy()
            scene = null
        }
    }
}