package dev.asodesu.origami.testplugin.scenes

import dev.asodesu.origami.engine.add
import dev.asodesu.origami.engine.addBy
import dev.asodesu.origami.engine.player.container
import dev.asodesu.origami.engine.scene.OfflinePlayerScene
import dev.asodesu.origami.testplugin.behaviours.BossbarBehaviour
import dev.asodesu.origami.testplugin.behaviours.TestSceneBehaviour
import org.bukkit.OfflinePlayer

class TestScopedScene(id: String) : OfflinePlayerScene(id) {
    override fun setupComponents() {
        add<TestSceneBehaviour>()
    }

    override fun setupComponents(player: OfflinePlayer) {
        player.container.addBy { BossbarBehaviour(id, it) }
    }
}