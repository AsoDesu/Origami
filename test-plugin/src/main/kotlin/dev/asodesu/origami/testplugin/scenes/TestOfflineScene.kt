package dev.asodesu.origami.testplugin.scenes

import dev.asodesu.origami.engine.add
import dev.asodesu.origami.engine.player.container
import dev.asodesu.origami.engine.scene.OfflineGlobalScene
import dev.asodesu.origami.testplugin.behaviours.DashBehaviour
import dev.asodesu.origami.testplugin.behaviours.DashRestoreBehaviour
import dev.asodesu.origami.testplugin.behaviours.InstantHealthBehaviour
import dev.asodesu.origami.testplugin.behaviours.TestSceneBehaviour
import org.bukkit.OfflinePlayer

class TestOfflineScene : OfflineGlobalScene() {
    override fun setupComponents() {
        add<TestSceneBehaviour>()
    }

    override fun setupComponents(player: OfflinePlayer) {
        player.container.add<DashBehaviour>()
        player.container.add<DashRestoreBehaviour>()
        player.container.add<InstantHealthBehaviour>()
    }
}