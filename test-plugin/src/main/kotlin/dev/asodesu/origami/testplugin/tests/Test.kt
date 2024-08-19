package dev.asodesu.origami.testplugin.tests

import dev.asodesu.origami.utilities.error
import dev.asodesu.origami.utilities.info
import dev.asodesu.origami.utilities.success
import dev.asodesu.origami.utilities.warning
import net.kyori.adventure.audience.Audience

class Test(val audience: Audience) {

    init {
        audience.info("\nStarting test...")
    }

    fun run(name: String, func: () -> Boolean?) {
        val success = try {
            func()
        } catch (e: Exception) {
            audience.error("Test <dark_red>'$name'</dark_red> threw an exception. -> ${e.message}")
            e.printStackTrace()
            return
        }
        if (success == null) audience.warning("Test <yellow>'$name'</yellow> was skipped.")
        else if (!success) audience.error("Test <dark_red>'$name'</dark_red> has failed.")
        else audience.success("Test <dark_green>'$name'</dark_green> has succeeded!")
    }

}