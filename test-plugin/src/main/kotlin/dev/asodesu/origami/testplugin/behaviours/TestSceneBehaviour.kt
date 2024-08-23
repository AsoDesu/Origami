package dev.asodesu.origami.testplugin.behaviours

import dev.asodesu.origami.engine.Behaviour
import dev.asodesu.origami.engine.scene.Scene
import dev.asodesu.origami.engine.wiring.annotations.Subscribe
import dev.asodesu.origami.utilities.bukkit.debug
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

class TestSceneBehaviour(val scene: Scene) : Behaviour() {

    fun postApply() {
        debug("TestSceneBehaviour has been applied to $scene!")
    }

    @Subscribe
    fun chat(evt: AsyncChatEvent) {
        val str = PlainTextComponentSerializer.plainText().serialize(evt.message())
        debug("[${scene.id}] ${evt.player.name} -> $str")
        evt.isCancelled = true
    }

}