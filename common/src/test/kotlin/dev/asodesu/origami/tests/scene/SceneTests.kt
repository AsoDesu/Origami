package dev.asodesu.origami.tests.scene

import dev.asodesu.origami.behaviours.Behaviour
import dev.asodesu.origami.objects.builder.GameObject
import dev.asodesu.origami.scene.GlobalScene
import dev.asodesu.origami.scene.Scene
import dev.asodesu.origami.scene.asChildOf
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class SceneTests {
    class GlobalTestBehaviour : Behaviour()
    class SceneOneTestBehaviour : Behaviour()
    class SceneOneTestBehaviour2 : Behaviour()
    class SceneTwoTestBehaviour : Behaviour()

    class TestScene(override val name: String) : Scene()

    @Test
    fun testScopedBehaviours() {
        val sceneOne = TestScene("scene1")
        val sceneTwo = TestScene("scene2")

        val gameObject = GameObject {}
        gameObject.global().add(GlobalTestBehaviour())
        gameObject.scene(sceneOne).add(SceneOneTestBehaviour())
        gameObject.scene(sceneOne).add(SceneOneTestBehaviour2())
        gameObject.scene(sceneTwo).add(SceneTwoTestBehaviour())

        assertEquals(gameObject.getAll().size, 4, "did not apply all 4 behaviours")
        assertDoesNotThrow("could not retrieve global scoped behaviour") { gameObject.get(GlobalTestBehaviour::class) }
        assertDoesNotThrow("could not retrieve scene1 scoped behaviour <1>") { gameObject.get(SceneOneTestBehaviour::class) }
        assertDoesNotThrow("could not retrieve scene1 scoped behaviour <2>") { gameObject.get(SceneOneTestBehaviour2::class) }
        assertDoesNotThrow("could not retrieve scene2 scoped behaviour") { gameObject.get(SceneTwoTestBehaviour::class) }

        gameObject.remove(sceneOne)
        assertEquals(gameObject.getAll().size, 2, "did not remove two scene1 behaviours")

        assertThrows<NoSuchElementException>("did not remove scene1 scoped behavior <1>") { gameObject.get(SceneOneTestBehaviour::class) }
        assertThrows<NoSuchElementException>("did not remove scene1 scoped behavior <2>") { gameObject.get(SceneOneTestBehaviour2::class) }

        assertDoesNotThrow("scene2 scoped behaviour was incorrectly removed") { gameObject.get(SceneTwoTestBehaviour::class) }
        assertDoesNotThrow("global scoped behaviour was incorrectly removed") { gameObject.get(GlobalTestBehaviour::class) }
    }
}